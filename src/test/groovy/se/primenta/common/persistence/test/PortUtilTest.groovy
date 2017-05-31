package se.primenta.common.persistence.test

import spock.lang.Specification

class PortUtilTest extends Specification {

    def 'do we get a port'() {

        when:
        def port = PortUtil.getNextAvailable()
        then:
        port != 0
        and:
        PortUtil.available(port)
    }

    def 'port availability'() {

        setup:
        def fromPort = 10_000 + new Random().nextInt(5_000)
        when:
        def port = PortUtil.getNextAvailable(fromPort)
        then:
        PortUtil.available(port)
    }

    def 'expect Illegal Arg Exception'() {

        when:
        PortUtil.available(25);
        then:
        IllegalArgumentException e = thrown()
    }
}
