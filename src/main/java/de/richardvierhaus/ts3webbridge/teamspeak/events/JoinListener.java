package de.richardvierhaus.ts3webbridge.teamspeak.events;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.teamspeak.servergroups.GroupController;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinListener extends TS3EventAdapter {

    @Override
    public void onClientJoin(ClientJoinEvent event) {
        Logger.log("Client " + event.getUniqueClientIdentifier() + " joined", LogLevel.DEBUG);

        ClientInfo info = Ts3Bot.getClientInfoByClientId(event.getClientId());

        if (firstConnection(info)) {
            registerClient(info);
        } else {
            updateClient(info);
        }

        GroupController.updateServerGroups(info);

        Session session = new Session(info.getUniqueIdentifier(), info.getIp());
        Ts3Bot.getSessionController().insertSession(event.getClientId(), session);

        Ts3Bot.getNicknameWatcher().addClient(info);
    }

    private void updateClient(final ClientInfo info) {
            try {
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "UPDATE `clients` SET `country` = ?, `version` = ?, `connections` = ? WHERE `clients`.`client_uid` = ?"
                );
                statement.setString(1, info.getCountry());
                statement.setString(2, info.getVersion());
                statement.setInt(3, info.getTotalConnections());
                statement.setString(4, info.getUniqueIdentifier());

                statement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
    }

    private void registerClient(final ClientInfo info) {
            try{
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "INSERT INTO `clients` (`client_uid`, `country`, `version`, `connections`) VALUES (?, ?, ?, ?)"
                );
                statement.setString(1, info.getUniqueIdentifier());
                statement.setString(2, info.getCountry());
                statement.setString(3, info.getVersion());
                statement.setInt(4, info.getTotalConnections());

                statement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

    }

    private boolean firstConnection(final ClientInfo info) {
        boolean var = true;
            try{
                PreparedStatement statement = MySQLManager.connect().prepareStatement(
                        "SELECT * FROM `clients` WHERE `client_uid` = ?"
                );

                statement.setString(1, info.getUniqueIdentifier());

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    var = false;
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        return var;
    }

}
