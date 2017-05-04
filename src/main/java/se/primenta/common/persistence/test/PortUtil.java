package se.primenta.common.persistence.test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.NoSuchElementException;

/**
 * Class for unit tests running without port conflict. Original port lookup stolen from Apache Camel.
 *
 * @author Sven Wesley
 *
 */
public final class PortUtil {

    public static final int MIN_PORT_NUMBER = 1100;

    public static final int MAX_PORT_NUMBER = 65535;

    /**
     * Finds next available port starting at the lowest port number.
     *
     * @throws NoSuchElementException
     *             if there are no ports available
     */
    public static int getNextAvailable() {
        return getNextAvailable(MIN_PORT_NUMBER);
    }

    /**
     * Finds next available port starting from port given.
     *
     * @param fromPort
     *            the port to scan for availability
     * @throws NoSuchElementException
     *             if there are no ports available
     */
    public static int getNextAvailable(final int fromPort) {
        if (fromPort < MIN_PORT_NUMBER || fromPort > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + fromPort);
        }

        for (int i = fromPort; i <= MAX_PORT_NUMBER; i++) {
            if (available(i)) {
                return i;
            }
        }

        throw new NoSuchElementException("Could not find an available port above " + fromPort);
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port
     *            the port to check for availability
     */
    public static boolean available(final int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
            // Do nothing
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (final IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

}
