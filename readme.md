[![Build Status](https://travis-ci.org/svenakela/Cassandra-Testbase.png)](https://travis-ci.org/svenakela/Cassandra-Testbase)
[![Coverage Status](https://coveralls.io/repos/github/svenakela/Cassandra-Testbase/badge.svg?branch=master)](https://coveralls.io/github/svenakela/Cassandra-Testbase?branch=master)

# Cassandra Testbase #

Library wrapping the [Cassandra Unit](https://github.com/jsevellec/cassandra-unit/wiki) framework and takes care of port and directory mapping automatically.

## Main Features ##

* Creates an embedded Cassandra node for unit testing internally
* Checks for open ports at startup
* Runs the embedded instance within your build or target directory

## How to use ##

Add the library as a test dependency and extend `CassandraTestBase` in your unit test class.

### Dependency ###

Add following to the build.gradle, with the latest version number:

`testCompile('se.primenta.common:cassandra-test:+')`

### Connect ###

To create a session with the CassandraSession you must get the port where the embedded instance is listening:

```Java

private String contactpoint = "localhost:" + getCassandraPort();

CassandraSession session = new CassandraSession.SessionBuilder(contactpoint)
                .forDataCenter(datacenter)
                .usingKeyspace(keyspace)
                .andReplication(replication)
                ...
```

### Write tests ###

Example of a Java Junit test using the [CassandraSession library](https://github.com/svenakela/Cassandra-Session)

```Java
public class CassandraExecutorTest extends CassandraTestBase {

    static CassandraSession session;

    @BeforeClass
    public static void init() {

        session = new CassandraSession.SessionBuilder("localhost:" + getCassandraPort())
                .usingKeyspace("executortest")
                .andReplication("{'class': 'SimpleStrategy', 'replication_factor': '1'}")
                .asUser("cassandra")
                .andPassword("cassandra")
                .preprocessTheseStatements(Collections.emptyList())
                .asPreprocessUserName("cassandra")
                .andPreprocessPassword("cassandra")
                .build();

        session.getExecutor().execute(new SimpleStatement(
                "create table testexecutor(id int primary key, test text)"));
    }

    @Test
    public void executeSync() {

        final CassandraExecutor exec = session.getExecutor();

        assertEquals(0, exec.execute(new SimpleStatement(
                "select * from testexecutor where id = 666")).all().size());
        assertEquals(0, exec.execute(new SimpleStatement(
                "insert into testexecutor(id, test) values (1, 'x')")).all().size());
    }
}
```

Running a Groovy Spock test:

```Groovy
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
```
