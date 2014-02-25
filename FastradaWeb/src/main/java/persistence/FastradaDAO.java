package persistence;

import app.SessionData;
import com.datastax.driver.core.*;

import java.util.HashMap;

/**
 * Created by Jonathan on 21/02/14.
 */
public class FastradaDAO {
    private String serverIP;
    private String keyspace;
    private Session session;

    public FastradaDAO(String serverIP, String keyspace) {
        this.serverIP = serverIP;
        this.keyspace = keyspace;
        makeConnection();
    }

    public int makeConnection() {
        try {
            Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();
            session = cluster.connect(keyspace);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public int createNextSession(SessionData sessionData) {
        String sessionName = sessionData.getName();
        PreparedStatement insertStatement = session.prepare("INSERT INTO metadata " + "(sessionid, parameter, value) " + "VALUES (?, ?, ?);");
        BoundStatement boundStatement = new BoundStatement(insertStatement);
        session.execute(boundStatement.bind("1", "name", sessionName));
        String statement = "select sessionid from metadata;";
        System.out.println(session.execute(statement));
        return 0;
    }

    public HashMap<Integer, SessionData> getAllSessionsData() {
        return new HashMap<Integer, SessionData>();
    }
}