package de.richardvierhaus.ts3webbridge.teamspeak.servergroups;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupController {

    public static List<String> getServerGroupsByIp(final String ip){
        List<String> groups = new ArrayList<>();
        try{
            PreparedStatement statement = MySQLManager.connect().prepareStatement(
                    "SELECT DISTINCT client_servergroups.servergroup " +
                            "FROM sessionlist " +
                            "INNER JOIN client_servergroups " +
                            "ON sessionlist.client_uid = client_servergroups.client_uid " +
                            "WHERE sessionlist.client_ip = ?"
            );
            statement.setString(1, ip);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                groups.add(resultSet.getString("servergroup"));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
        return groups;
    }

    public static List<String> getServerGroupsByUid(final String uid) {
        List<String> serverGroups = new ArrayList<>();
        try{
            PreparedStatement statement = MySQLManager.connect().prepareStatement(
                    "SELECT `servergroup` FROM `client_servergroups` WHERE `client_uid` = ?"
            );
            statement.setString(1, uid);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                serverGroups.add(resultSet.getString("servergroup"));
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }

        return serverGroups;
    }

    public static void updateServerGroups(final ClientInfo info) {
        ArrayList<String> serverGroups = Ts3Bot.getClientServerGroups(info.getDatabaseId()).stream().map(ServerGroup::getName).collect(Collectors.toCollection(ArrayList::new));

        try{
            PreparedStatement statement = MySQLManager.connect().prepareStatement(
                    "SELECT `servergroup` FROM `client_servergroups` WHERE `client_uid` = ?"
            );
            statement.setString(1, info.getUniqueIdentifier());

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String group = resultSet.getString("servergroup");
                if(serverGroups.contains(group)){
                    serverGroups.remove(group);
                    continue;
                }

                statement = MySQLManager.connect().prepareStatement(
                        "DELETE FROM `client_servergroups` WHERE `client_uid` = ? AND `servergroup` = ?"
                );
                statement.setString(1, info.getUniqueIdentifier());
                statement.setString(2, group);
                statement.execute();
            }

            for (String group : serverGroups) {
                statement = MySQLManager.connect().prepareStatement(
                        "INSERT INTO `client_servergroups` (`client_uid`, `servergroup`) VALUES (?, ?)"
                );
                statement.setString(1, info.getUniqueIdentifier());
                statement.setString(2, group);

                statement.execute();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
    }
}
