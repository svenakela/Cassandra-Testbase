package se.primenta.common.persistence.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

/**
 * Base class for automatic creation of a cassandra-unit node.
 *
 * @author Sven Wesley
 *
 */
public class CassandraTestBase {

    private static int cassandraPort = 0;

    static {
        try {

            final File file = File.createTempFile("cassandra", ".yaml");
            file.deleteOnExit();

            final InputStream input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("utils-test-cassandra.yml");
            String content = new BufferedReader(new InputStreamReader(input))
                    .lines().collect(Collectors.joining("\n"));

            cassandraPort = PortUtil.getNextAvailable();
            content = content.replaceAll("__PORT__", String.valueOf(cassandraPort));
            content = content.replaceAll("__DIR__", String.valueOf(getStartDir()));
            Files.write(Paths.get(file.getPath()), content.getBytes());

            EmbeddedCassandraServerHelper.startEmbeddedCassandra(file, System.getProperty("java.io.tmpdir"), 20000L);

        } catch (TTransportException | IOException | ConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * The port the cassandra node is listening on.
     *
     * @return int port number
     */
    public static int getCassandraPort() {
        return cassandraPort;
    }

    private static String getStartDir() {

        if (Files.exists(Paths.get("build"))) {
            return "build";
        } else if (Files.exists(Paths.get("target"))) {
            return "target";
        } else {
            return "tmp";
        }
    }

}
