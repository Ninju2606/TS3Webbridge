package de.richardvierhaus.ts3webbridge.teamspeak.nicknames;

import de.richardvierhaus.ts3webbridge.database.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class NicknameLogger {

    public static void addNickname(final String uid, final String nickname){
            try {
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "INSERT INTO `nicknamelog` (`client_uid`, `change_time`, `nickname`) VALUES (?, CURRENT_TIMESTAMP, ?)"
                );
                statement.setString(1, uid);
                statement.setString(2, nickname);

                statement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            } finally {
                MySQLManager.disconnect();
            }
    }

    public static Map<Timestamp, String> getHistoryByUid(final String uid){
        Map<Timestamp, String> history = new LinkedHashMap<>();
        try {
            PreparedStatement statement = MySQLManager.connect().prepareStatement(
                    "SELECT `change_time`,`nickname` FROM `nicknamelog` WHERE `client_uid`=? ORDER BY change_time DESC"
            );
            statement.setString(1, uid);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                history.put(rs.getTimestamp("change_time"), rs.getString("nickname"));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
        return history;
    }

    public static String getLastNameByUid(String uid){String name = "";
            try {
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "SELECT `nickname` FROM `nicknamelog` WHERE `client_uid`=? ORDER BY change_time DESC"
                );
                statement.setString(1, uid);

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    name= rs.getString("nickname");
                    break;
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            } finally {
                MySQLManager.disconnect();
            }
        return name;
    }
}
