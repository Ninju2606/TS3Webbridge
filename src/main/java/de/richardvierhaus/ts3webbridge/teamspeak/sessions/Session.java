package de.richardvierhaus.ts3webbridge.teamspeak.sessions;

import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Session {

    private final Timestamp joinTime;
    private Timestamp leaveTime;
    private final String clientUid, ipAdress;
    private Long duration;

    public Session(String clientUid, String ipAdress) {
        this(clientUid, ipAdress, new Timestamp(System.currentTimeMillis()), null);
    }

    public Session(String clientUid, String ipAdress, Timestamp joinTime, Timestamp leaveTime) {
        this.clientUid = clientUid;
        this.ipAdress = ipAdress;
        this.joinTime = joinTime;
        this.leaveTime = leaveTime;
        this.duration = getDuration();
    }

    public Session end() {
        leaveTime = new Timestamp(System.currentTimeMillis());
        this.duration = getDuration();
        return this;
    }

    public Runnable saveToDatabase() {
        return () -> {
            try {
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "INSERT INTO `sessionlist` (`client_uid`, `join_time`, `leave_time`, `client_ip`) VALUES (?, ?, ?, ?)"
                );
                statement.setString(1, clientUid);
                statement.setTimestamp(2, joinTime);
                statement.setTimestamp(3, leaveTime);
                statement.setString(4, ipAdress);

                statement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            } finally {
                MySQLManager.disconnect();
            }

        };
    }

    public long getDuration() {
        if (leaveTime == null) {
            return System.currentTimeMillis() - joinTime.getTime();
        } else {
            return leaveTime.getTime() - joinTime.getTime();
        }
    }

}
