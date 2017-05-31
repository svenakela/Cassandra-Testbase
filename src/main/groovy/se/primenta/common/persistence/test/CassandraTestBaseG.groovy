package se.primenta.common.persistence.test

import java.nio.file.Files
import java.nio.file.Paths

import org.cassandraunit.utils.EmbeddedCassandraServerHelper

class CassandraTestBaseG {

    static int cassandraPort = 0;

    static {

        cassandraPort = PortUtil.getNextAvailable();
        def content = this.getClass().getResourceAsStream('/utils-test-cassandra.yml').text;
        content = content.replaceAll('__PORT__', String.valueOf(cassandraPort));
        content = content.replaceAll('__DIR__', String.valueOf(getStartDir()));

        File file = File.createTempFile('cassandra', '.yaml').with {
            deleteOnExit()
            write content
            return it
        }

        EmbeddedCassandraServerHelper.startEmbeddedCassandra(file, System.properties.'java.io.tmpdir', 20000L);
    }

    static def getStartDir() {
        Files.exists(Paths.get('build'))  ? 'build' : Files.exists(Paths.get('target')) ? 'target' : 'tmp'
    }
}
