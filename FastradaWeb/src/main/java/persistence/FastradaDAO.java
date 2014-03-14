package persistence;

import app.Coordinate;
import app.GpsValue;
import app.Parameter;
import com.datastax.driver.core.*;
import json.SessionData;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

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
        String track = sessionData.getTrackName();
        String vehicleName = sessionData.getVehicleName();
        String comment = sessionData.getComment();
        String date = "" + sessionData.getDate();

        PreparedStatement insertStatement = session.prepare("INSERT INTO metadata" + "(sessionid, parameter, value) " + "VALUES (?, ?, ?);");
        BoundStatement boundStatement = new BoundStatement(insertStatement);

        //TODO get max sessionId refactoren
        String cqlStatement = "SELECT sessionid FROM metadata;";
        for (Row row : session.execute(cqlStatement)) {
            if (Integer.parseInt(row.getString(0)) >= sessionId) {
                sessionId = Integer.parseInt(row.getString(0)) + 1;
            }
        }
        session.execute(boundStatement.bind(String.format("%d", sessionId), "sessionName", name));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "trackName", track));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "vehicleName", vehicleName));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "comment", comment));
        session.execute(boundStatement.bind(String.format("%d", sessionId), "date", date));

        createSessionTable(sessionId);
        return sessionId;
    }

    public void createSessionTable(int sessionId) {
        String cqlStatement = "CREATE TABLE s" + sessionId + " ( time timestamp, parameter text, value double, PRIMARY KEY (time, parameter) ) WITH COMPACT STORAGE;";
        session.execute(cqlStatement);
    }

    public List<SessionData> getAllSessionsData() {
        Set<Integer> ids = new HashSet<Integer>();
        List<SessionData> sessions = new ArrayList<SessionData>();

        String cqlSelectIds = "SELECT sessionid FROM metadata;";
        for (Row row : session.execute(cqlSelectIds)) {
            ids.add(Integer.parseInt(row.getString(0)));
        }
        for (Integer id : ids) {
            String cqlSelectData = "SELECT parameter, value FROM metadata WHERE sessionid='" + id + "';";
            SessionData sessionData = new SessionData();
            for (Row row : session.execute(cqlSelectData)) {
                switch (row.getString("parameter")) {
                    case "sessionName":
                        sessionData.setSessionName(row.getString("value"));
                        break;
                    case "trackName":
                        sessionData.setTrackName(row.getString("value"));
                        break;
                    case "vehicleName":
                        sessionData.setVehicleName(row.getString("value"));
                        break;
                    case "comment":
                        sessionData.setComment(row.getString("value"));
                        break;
                    case "date":
                        sessionData.setDate(Long.parseLong(row.getString("value")));
                        break;
                }
            }
            sessionData.setSessionId(id);
            sessions.add(sessionData);
        }
        return sessions;
    }

    public List<String> getParametersBySessionId(int sessionId) {
        Set<String> params = new HashSet<>();
        String cqlSelect = "SELECT parameter FROM s" + sessionId + ";";
        for (Row row : session.execute(cqlSelect)) {
            params.add(row.getString(0));
        }
        return new ArrayList<>(params);
    }

    public List<Parameter> getParameterValuesBySessionId(Integer sessionId, String parameter) {
        boolean loop = true;
        TreeMap<Date, Double> allParam = new TreeMap<>();
        List<Parameter> returnParams = new ArrayList<>();
        PreparedStatement insertStatement = session.prepare("SELECT time, value FROM s" + sessionId + " WHERE parameter = ? ALLOW FILTERING;");
        BoundStatement boundStatement = new BoundStatement(insertStatement);
        for (Row row : session.execute(boundStatement.bind(parameter))) {
            allParam.put(row.getDate(0), row.getDouble(1));
        }
        Date fromDate = new Date(allParam.firstKey().getTime() - 500);
        Date toDate = new Date(allParam.firstKey().getTime() + 500);
        while (loop) {
            double sum = 0;
            int counter = 0;
            SortedMap<Date, Double> submap = allParam.subMap(fromDate, toDate);
            for (Map.Entry<Date, Double> dateDoubleEntry : submap.entrySet()) {
                sum += dateDoubleEntry.getValue();
                counter++;
            }
            double average = sum / counter;
            if (counter != 0) {
                returnParams.add(new Parameter(new Date(fromDate.getTime() + 500), average));
            }
            fromDate.setTime(fromDate.getTime() + 1000);
            toDate.setTime(toDate.getTime() + 1000);
            if (fromDate.after(allParam.lastKey())) {
                loop = false;
            }
        }
        return returnParams;
    }

    public void deleteSession(int sessionId) {
        String cqlDropTable = "DROP TABLE s" + sessionId + ";";
        session.execute(cqlDropTable);
        String cqlDeleteMetadata = "DELETE FROM metadata where sessionid = '" + sessionId + "';";
        session.execute(cqlDeleteMetadata);
    }

    public void insertPacketValues(int sessionId, long timestamp, HashMap<String, Double> map) {
        PreparedStatement insertStatement = session.prepare("INSERT INTO s" + sessionId + " (time, parameter, value) " + "VALUES (?, ?, ?);");
        BoundStatement boundStatement = new BoundStatement(insertStatement);

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            session.execute(boundStatement.bind(new Date(timestamp), entry.getKey(), entry.getValue()));
        }
    }

    public List<GpsValue> getGpsById(Integer sessionId) {
         List<GpsValue> returnGpsValues = new ArrayList<>();
         Map<Date, Double> latitudes = new HashMap<>();
         Map<Date, Double> longitudes = new HashMap<>();
         String cqlSelectLatitudes = "SELECT time, value FROM S" + sessionId + " WHERE parameter = 'latitude' ALLOW FILTERING;";
         String cqlSelectLongitudes = "SELECT time, value FROM S" + sessionId + " WHERE parameter = 'longitude' ALLOW FILTERING;";

         for (Row row : session.execute(cqlSelectLatitudes)) {
             latitudes.put(row.getDate(0), row.getDouble(1));
         }

         for (Row row : session.execute(cqlSelectLongitudes)) {
             longitudes.put(row.getDate(0), row.getDouble(1));
         }

         for (Map.Entry<Date, Double> dateDoubleEntry : latitudes.entrySet()) {
             if (longitudes.containsKey(dateDoubleEntry.getKey())) {
                 GpsValue gpsValue = new GpsValue(dateDoubleEntry.getKey().getTime(), new Coordinate(dateDoubleEntry.getValue(), longitudes.get(dateDoubleEntry.getKey())));
                 returnGpsValues.add(gpsValue);
             }
         }
         return returnGpsValues;
     }
}