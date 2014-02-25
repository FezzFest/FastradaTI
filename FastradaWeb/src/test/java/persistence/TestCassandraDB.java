/*
package persistence;
import app.SessionData;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.*;

import java.util.HashMap;

*/
/**
 * Created by Jonathan on 21/02/14.
 *//*

public class TestCassandraDB {

    private static FastradaDAO fastradaDAO;

    @BeforeClass
    public static void init() {
        Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
        Session session = cluster.connect("fastradatest");
        session.execute("TRUNCATE metadata;");
        String serverIP = "127.0.0.1";
        String keyspace = "fastradatest";
        fastradaDAO = new FastradaDAO(serverIP, keyspace);
    }


    @Test
    public void testConnection() {
        Assert.assertEquals("Connection with fastrada keyspace failed", 0, fastradaDAO.makeConnection());
    }

    @Test
    public void testGetASessionId() {
        Assert.assertEquals("Next session id must be integer >= 0", true, fastradaDAO.createNextSession(new SessionData("Run1")) >= 0);
    }

    @Test
    public void testUniqueSessionId() {
        boolean unique = true;
        int nextSessionId = fastradaDAO.createNextSession(new SessionData("Run1"));
        HashMap<Integer, SessionData> sessionDataHashMap = fastradaDAO.getAllSessionsData();
        for (Integer id : sessionDataHashMap.keySet()) {
            if (nextSessionId == id) {
                unique = false;
            }
        }
        Assert.assertEquals("Next session id must be unique", true, unique);
    }

    @Test
    public void testCreate2Sessions() {
        int sessionId1 = fastradaDAO.createNextSession(new SessionData("Run1"));
        int sessionId2 = fastradaDAO.createNextSession(new SessionData("Run2"));

        Assert.assertEquals("Session ids must be consecutive", sessionId1 + 1, sessionId2);
    }


}
*/
