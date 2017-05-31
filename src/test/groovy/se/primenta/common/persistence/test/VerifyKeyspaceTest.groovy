package se.primenta.common.persistence.test

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ResultSet

import spock.lang.Specification

class VerifyKeyspaceTest extends Specification {

    def 'verify that a cluster is created'() {

        setup:
        def cassandra = new CassandraTestBase()
        def session = Cluster.builder()
                .addContactPoint('localhost')
                .withPort(cassandra.cassandraPort)
                .build()
                .connect()
        when:
        ResultSet rs = session.execute('select release_version from system.local')
        then:
        rs.one().getString('release_version') != ''
    }
}
