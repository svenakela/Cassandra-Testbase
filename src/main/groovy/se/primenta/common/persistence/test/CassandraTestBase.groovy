package se.primenta.common.persistence.test

import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

import org.apache.cassandra.service.StorageService
import org.cassandraunit.utils.EmbeddedCassandraServerHelper

class CassandraTestBase {

    static int cassandraPort = 0

    static {

        Path tmpPath = Files.createTempDirectory('embeddedcassandra_' + Instant.now().toEpochMilli())

        cassandraPort = PortUtil.getNextAvailable()
        def content = this.getClass().getResourceAsStream('/utils-test-cassandra.yml').text
        content = content.replaceAll('__PORT__', String.valueOf(cassandraPort))
        content = content.replaceAll('__DIR__', tmpPath.toString())

        File file = File.createTempFile('cassandra', '.yaml').with {
            deleteOnExit()
            write content
            return it
        }

        EmbeddedCassandraServerHelper.startEmbeddedCassandra(file, tmpPath.toString(), 20000L)
        // Unfortunately we have no way to check if C* is up and temp
        // files must be deleted last or we will get stack traces.
        StorageService.instance.addPostShutdownHook([run: { tmpPath.toFile().deleteDir() }] as Runnable)

    }
}
