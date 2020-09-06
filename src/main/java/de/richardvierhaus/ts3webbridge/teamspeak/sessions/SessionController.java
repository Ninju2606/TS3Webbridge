package de.richardvierhaus.ts3webbridge.teamspeak.sessions;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionController {

    private Map<Integer, Session> sessions;
    private ExecutorService executorService;

    public SessionController() {
        sessions = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
    }

    public Optional<Session> getSessionByClientId(final int clientId) {
        final Session s = sessions.get(clientId);
        return s == null ? Optional.empty() : Optional.of(s);
    }

    public Optional<Session> getSessionByUid(final String uid){
        ClientInfo info = Ts3Bot.getConnectedClientsWithUid(uid).get(0);
        if(info == null)
            return Optional.empty();
        return getSessionByClientId(info.getId());
    }

    public void insertSession(final int clientId, final Session session) {
        sessions.put(clientId, session);
    }

    public void removeSessionByClientId(final int clientId) {
        Session session = sessions.remove(clientId);
        if (session == null) {
            return;
        }

        executorService.submit(session.end().saveToDatabase());
    }


    public static List<Session> getSessionsByUid(final String uid) {
        List<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement statement = MySQLManager.connect().prepareStatement("SELECT * FROM `sessionlist` WHERE client_uid=? ORDER BY join_time DESC LIMIT 20");
            statement.setString(1, uid);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                sessions.add(new Session(rs.getString("client_uid"), rs.getString("client_ip"), rs.getTimestamp("join_time"), rs.getTimestamp("leave_time")));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
        return sessions;
    }
}
