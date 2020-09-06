package de.richardvierhaus.ts3webbridge.webserver;

import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.teamspeak.nicknames.NicknameLogger;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientListObject {

    private final String uid;
    private final String lastName;
    private final String lastOnline;
    private final int connections;
    private int totalTimeOnline;

    public ClientListObject(final String uid) {
        this.uid = uid;
        this.lastName = NicknameLogger.getLastNameByUid(uid);

        Object[] data = getUserData(uid);
        Optional<Session> currentSession = Ts3Bot.getSessionController().getSessionByUid(uid);

        this.lastOnline = currentSession.isPresent() ? "ONLINE" : data[0].toString();
        this.connections = (int) data[1];
        this.totalTimeOnline = (int) data[2];
        if (currentSession.isPresent())
            this.totalTimeOnline += ((int) currentSession.get().getDuration() / 1000);
    }

    private Object[] getUserData(final String uid) {
        Object[] data = new Object[3];
        try {
            PreparedStatement statement = MySQLManager.connect().prepareStatement(
                    "SELECT clients.connections, MAX(sessionlist.leave_time) as last_online, SUM(sessionlist.leave_time)-SUM(sessionlist.join_time) as time_online " +
                            "FROM sessionlist INNER JOIN clients ON sessionlist.client_uid = clients.client_uid " +
                            "WHERE sessionlist.client_uid = ?"
            );
            statement.setString(1, uid);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data[0] = resultSet.getTimestamp("last_online");
                data[1] = resultSet.getInt("connections");
                data[2] = resultSet.getInt("time_online");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
        return data;
    }
}
