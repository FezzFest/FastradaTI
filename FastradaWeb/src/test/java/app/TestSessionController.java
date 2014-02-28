package app;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jonathan on 26/02/14.
 */
public class TestSessionController {
    private static SessionController sessioncontroller;
    private static Session session;

    @BeforeClass
    public static void before() {
        makeConnection();
        sessioncontroller = new SessionController();

        //reinitialize DB
        reinitializeDB();
    }


    @Test
    public void testNewSessionPost() {
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        SessionData run2 = new SessionData("Run2", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        Gson gson = new Gson();

        String json1 = gson.toJson(run1);
        String json2 = gson.toJson(run2);

        int sessionId1 = sessioncontroller.getNewSessionId(json1).getSessionId();
        int sessionId2 = sessioncontroller.getNewSessionId(json2).getSessionId();

        Assert.assertEquals("SessionIds must be consecutive", sessionId1 + 1, sessionId2);
    }

    @Test
    public void testCreateColumnFamily() {
        int rowCount = 0;
        SessionData run1 = new SessionData("Run3", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        Gson gson = new Gson();

        String json1 = gson.toJson(run1);

        int sessionId1 = sessioncontroller.getNewSessionId(json1).getSessionId();

        String cqlStatement = "SELECT columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name='fastradatest' and columnfamily_name='s" + sessionId1 + "';";
        for (Row row : session.execute(cqlStatement)) {
            rowCount++;
        }
        Assert.assertEquals("New columnfamily with new sessionId as its name must be made", 1, rowCount);
    }

    @Test
    public void testGetAllSessionsData() {
        reinitializeDB();
        List<SessionData> sessions = new ArrayList<SessionData>();
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        SessionData run2 = new SessionData("Run2", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        sessions.add(run1);
        sessions.add(run2);
        Gson gson = new Gson();
        String json1 = gson.toJson(run1);
        String json2 = gson.toJson(run2);
        run1.setSessionId(sessioncontroller.getNewSessionId(json1).getSessionId());
        run2.setSessionId(sessioncontroller.getNewSessionId(json2).getSessionId());

        Assert.assertTrue("Metadata must be the same as inserted metadata", sessions.equals(sessioncontroller.getAllSessionData()));
    }

    @Test
    public void testGetSessionParameters() throws InterruptedException {
        reinitializeDB();
        boolean check = true;

        List<String> sendParams = new ArrayList<>();
        List<String> receivedParams;
        String speed = "speed";
        String gear = "gear";
        String throttle = "throttle";
        sendParams.add(speed);
        sendParams.add(throttle);
        sendParams.add(gear);
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        Gson gson = new Gson();
        String json1 = gson.toJson(run1);
        run1.setSessionId(sessioncontroller.getNewSessionId(json1).getSessionId());

        String insertCql = "INSERT INTO s" + run1.getSessionId() + "(time, parameter, value) values(dateOf(now()),'" + speed + "',25);";
        String insertCql2 = "INSERT INTO s" + run1.getSessionId() + "(time, parameter, value) values(dateOf(now()),'" + gear + "',25);";
        String insertCql3 = "INSERT INTO s" + run1.getSessionId() + "(time, parameter, value) values(dateOf(now()),'" + throttle + "',25);";
        for (int i = 0; i < 20; i++) {
            session.execute(insertCql);
            session.execute(insertCql2);
        }
        session.execute(insertCql3);

        receivedParams = sessioncontroller.getParametersBySessionId(run1.getSessionId());

        check = sendParams.containsAll(receivedParams);
        if (sendParams.size() != receivedParams.size()) {
            check = false;
        }
        Assert.assertTrue("3 parameters must be returned", check);
    }

    @Test
    public void testGetParameterValues() {
        SessionData run1 = new SessionData("Run1", "Spa Francorchamps", "FastradaMobiel", "Zalig ritje met mooi weer", System.currentTimeMillis());
        Gson gson = new Gson();
        String json1 = gson.toJson(run1);
        run1.setSessionId(sessioncontroller.getNewSessionId(json1).getSessionId());

        String insertCql = "INSERT INTO s" + run1.getSessionId() + "(time, parameter, value) values(dateOf(now()),'" + "speed" + "',25);";
    }

    public static void makeConnection() {
        try {
            Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
            session = cluster.connect("fastradatest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reinitializeDB() {
        session.execute("TRUNCATE metadata;");
        String cqlStatement = "SELECT columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name='fastradatest';";
        for (Row row : session.execute(cqlStatement)) {
            if (!row.getString("columnfamily_name").equals("metadata")) {
                session.execute("drop table " + row.getString("columnfamily_name"));
            }
        }
    }
}
