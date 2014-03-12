package persistence;

import app.GpsValue;
import json.SessionData;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.junit.*;

import java.util.List;

/**
 * Created by Jonathan on 21/02/14.
 */

public class TestCassandraDB {

    private static FastradaDAO fastradaDAO;
    static Session session;

    @BeforeClass
    public static void init() {
        //reinitialize DB
        Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
        session = cluster.connect("fastradatest");
        session.execute("TRUNCATE metadata;");
        String cqlStatement = "SELECT columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name='fastradatest';";
        for (Row row : session.execute(cqlStatement)) {
            if (!row.getString("columnfamily_name").equals("metadata")) {
                session.execute("drop table " + row.getString("columnfamily_name"));
            }
        }
        String serverIP = "127.0.0.1";
        String keyspace = "fastradatest";
        fastradaDAO = new FastradaDAO(serverIP, keyspace);
    }


    @Test
    public void testConnection() {
        Assert.assertEquals("Connection with fastrada keyspace failed", FastradaDAO.CONNECTION_SUCCESFULL, fastradaDAO.makeConnection());
    }

    @Test
    public void testGetASessionId() {
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        Assert.assertEquals("Next session id must be integer >= 0", true, fastradaDAO.createNextSession(run1) >= 0);
    }

    @Test
    public void testUniqueSessionId() {
        SessionData run1 = new SessionData("Run2", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        boolean unique = true;
        List<SessionData> sessionDatas = fastradaDAO.getAllSessionsData();

        int nextSessionId = fastradaDAO.createNextSession(run1);

        for (SessionData sessionData : sessionDatas) {
            if (sessionData.getSessionId() == nextSessionId) {
                unique = false;
            }
        }
        Assert.assertEquals("Next session id must be unique", true, unique);
    }

    @Test
    public void testCreate2Sessions() {
        SessionData run1 = new SessionData("Run3", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        SessionData run2 = new SessionData("Run4", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        int sessionId1 = fastradaDAO.createNextSession(run1);
        int sessionId2 = fastradaDAO.createNextSession(run2);

        Assert.assertEquals("Session ids must be consecutive", sessionId1 + 1, sessionId2);
    }

    @Test
    public void testGetSingleSessionParam() {
        SessionData run = new SessionData("Run5", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        int sessionId = fastradaDAO.createNextSession(run);
        String insertCql = "INSERT INTO s" + sessionId + "(time, parameter, value) values(dateOf(now()),'" + "speed" + "',25);";
        session.execute(insertCql);
        String parameter = fastradaDAO.getParametersBySessionId(sessionId).get(0);

        Assert.assertEquals("Returned parameter must be speed", "speed", parameter);
    }

    @Test
    public void testGetOneParameterValue() {
        SessionData run = new SessionData("Run5", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        int sessionId = fastradaDAO.createNextSession(run);
        String insertCql = "INSERT INTO s" + sessionId + "(time, parameter, value) values(dateOf(now()),'" + "speed" + "',25);";
        session.execute(insertCql);
        Double speed = fastradaDAO.getParameterValuesBySessionId(sessionId, "speed").get(0).getValue();

        Assert.assertEquals("Returned speed must be 25", 25, speed, 0);
    }

    @Test
    public void testDeleteSession() {
        SessionData run1 = new SessionData("Run3", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        SessionData run2 = new SessionData("Run4", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        int sessionId1 = fastradaDAO.createNextSession(run1);
        fastradaDAO.deleteSession(sessionId1);
        int sessionId2 = fastradaDAO.createNextSession(run2);

        Assert.assertEquals("Session ids must be the same", sessionId1, sessionId2);
    }

    @Test
       public void testGpsCoordinate() throws InterruptedException {
           SessionData run1 = new SessionData("Run7", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
           int sessionId = fastradaDAO.createNextSession(run1);
           long time = System.currentTimeMillis();
           double latitude = 51.222998;
           double longitude = 4.508343;

           String insertCoordinate = "INSERT INTO s" + sessionId + "(time, parameter, value) values(" + time + ",'" + "latitude" + "'," + latitude + ");";
           String insertCoordinate2 = "INSERT INTO s" + sessionId + "(time, parameter, value) values(" + time + ",'" + "longitude" + "'," + longitude + ");";

           session.execute(insertCoordinate);
           session.execute(insertCoordinate2);

           List<GpsValue> receivedGpsValues = fastradaDAO.getGpsById(sessionId);
           double sum = receivedGpsValues.get(0).getCoordinate().getLatitude() + receivedGpsValues.get(0).getCoordinate().getLongitude();

           Assert.assertEquals("Gps coordinates must be the same as inserted", sum, latitude + longitude, 0);
       }


}
