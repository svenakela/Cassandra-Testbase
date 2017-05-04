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

`testCompile('se.primenta.common:cassandra-testbase:+')`

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

Example of a unit test, a live example can be seen ...

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

        session.getExecutor().execute(new SimpleStatement("create table testexecutor(id int primary key, test text)"));
    }

    @Test
    public void executeSync() {

        final CassandraExecutor exec = session.getExecutor();

        assertEquals(0, exec.execute(new SimpleStatement("select * from testexecutor where id = 666")).all().size());
        assertEquals(0,
                exec.execute(new SimpleStatement("insert into testexecutor(id, test) values (1, 'x')")).all().size());
    }
}
```