package persistence;

import app.SessionData;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.*;

import java.util.Date;
import java.util.HashMap;


/**
 * Created by Jonathan on 21/02/14.
 */

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
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", new Date(System.currentTimeMillis()));
        Assert.assertEquals("Next session id must be integer >= 0", true, fastradaDAO.createNextSession(run1) >= 0);
    }

    @Test
    public void testUniqueSessionId() {
        SessionData run1 = new SessionData("Run2", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", new Date(System.currentTimeMillis()));
        boolean unique = true;
        int nextSessionId = fastradaDAO.createNextSession(run1);
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
        SessionData run1 = new SessionData("Run3", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", new Date(System.currentTimeMillis()));
        SessionData run2 = new SessionData("Run4", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", new Date(System.currentTimeMillis()));
        int sessionId1 = fastradaDAO.createNextSession(run1);
        int sessionId2 = fastradaDAO.createNextSession(run2);

        Assert.assertEquals("Session ids must be consecutive", sessionId1 + 1, sessionId2);
    }
}
