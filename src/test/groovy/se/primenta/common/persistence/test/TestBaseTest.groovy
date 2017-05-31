package se.primenta.common.persistence.test

import spock.lang.Specification

class TestBaseTest extends Specification {

    def 'verify that a port is taken'() {

        when:
        def testBase = new CassandraTestBase()
        then:
        testBase.cassandraPort != 0
    }
}
