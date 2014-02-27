package persistence;

import app.SessionData;
import com.datastax.driver.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Jonathan on 21/02/14.
 */
@Component
public class FastradaDAO implements Serializable {
    private String serverIP;
    private String keyspace;
    private Session session;
    public static final int CONNECTION_SUCCESFULL = 0;
    public static final int CONNECTION_FAILED = 1;

    public FastradaDAO(String serverIP, String keyspace) {
        this.serverIP = serverIP;
        this.keyspace = keyspace;
        makeConnection();
    }

    public FastradaDAO() {
        this.serverIP = "127.0.0.1";
        this.keyspace = "fastradatest";
        makeConnection();
    }

    //@PostConstruct
    public int makeConnection() {
        try {
            Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();
            session = cluster.connect(keyspace);
        } catch (Exception e) {
            return CONNECTION_FAILED;
        }
        return CONNECTION_SUCCESFULL;
    }

    public int createNextSession(SessionData sessionData) {
        //TODO iterate over all fields from SessionData
        int sessionId = 0;
        String name = sessionData.getSessionName();
        String date = sessionData.getDate().toString();
        String comment = sessionData.getComment();
        String track = sessionData.getTrackName();

        PreparedStatement insertStatement = session.prepare("INSERT INTO metadata" + "(sessionid, parameter, value) " + "VALUES (?, ?, ?);");
        BoundStatement boundStatement = new BoundStatement(insertStatement);

        //TODO get max sessionId refactoren
        String cqlStatement = "SELECT sessionid FROM metadata;";
        for (Row row : session.execute(cqlStatement)) {
            if (Integer.parseInt(row.getString(0)) >= sessionId) {
                sessionId = Integer.parseInt(row.getString(0)) + 1;
            }
        }
        session.execute(boundStatement.bind(String.format("%d", sessionId), "name", name));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "date", date));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "comment", comment));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "track", track));

        createSessionTable(sessionId);
        return sessionId;
    }

    public void createSessionTable(int sessionId) {
        String cqlStatement = "CREATE TABLE s" + sessionId + " ( sessionid text, parameter text, value text, PRIMARY KEY (sessionid, parameter) ) WITH COMPACT STORAGE;";
        session.execute(cqlStatement);
    }

    public HashMap<Integer, SessionData> getAllSessionsData() {
        return new HashMap<Integer, SessionData>();
    }
}