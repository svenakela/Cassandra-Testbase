package se.primenta.common.persistence.test;

import org.junit.Assert;
import org.junit.Test;

public class WrapperTest extends CassandraTestBase {

    @Test
    public void isTherePort() {
        Assert.assertTrue(getCassandraPort() != 0);
    }

}
