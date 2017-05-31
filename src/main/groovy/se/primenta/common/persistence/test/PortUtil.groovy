package se.primenta.common.persistence.test

class PortUtil {

    static int MIN_PORT_NUMBER = 1100

    static int MAX_PORT_NUMBER = 65535

    /**
     * Finds next available port starting at the lowest port number.
     *
     * @throws NoSuchElementException
     *             if there are no ports available
     */
    static int getNextAvailable() {
        return getNextAvailable(MIN_PORT_NUMBER)
    }

    /**
     * Finds next available port starting from port given.
     *
     * @param fromPort
     *            the port to scan for availability
     * @throws NoSuchElementException
     *             if there are no ports available
     */
    static int getNextAvailable(final int fromPort) {

        checkPortInterval(fromPort)

        for (port in fromPort..MAX_PORT_NUMBER)
            if (available(port)) return port

        throw new NoSuchElementException('Could not find an available port above ' + fromPort)
    }

    /**
     * Check if a specific port is available.
     */
    def static available(final int port) {

        checkPortInterval(port)

        ServerSocket ss
        DatagramSocket ds
        try {
            ss = new ServerSocket(port)
            ss.setReuseAddress(true)
            ds = new DatagramSocket(port)
            ds.setReuseAddress(true)
            return true
        } catch (e) {
            print e
        } finally {
            if (ds != null) ds.close()
            if (ss != null) ss.close()
        }

        return false
    }

    def static checkPortInterval(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException('Invalid start port: ' + port)
        }
    }
}
