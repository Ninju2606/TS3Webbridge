package de.richardvierhaus.ts3webbridge.webserver;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.teamspeak.nicknames.NicknameLogger;
import de.richardvierhaus.ts3webbridge.teamspeak.servergroups.GroupController;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.Session;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.SessionController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ClientResponse {

    private final List<Session> sessionList;
    private final List<ClientInfo> connectedClients;
    private final Map<Timestamp, String> nicknameLog;
    private final List<String> serverGroups;

    public ClientResponse(final String uid) {
        this.sessionList = SessionController.getSessionsByUid(uid);
        this.connectedClients = Ts3Bot.getConnectedClientsWithUid(uid);
        this.serverGroups = GroupController.getServerGroupsByUid(uid);
        this.nicknameLog = NicknameLogger.getHistoryByUid(uid);
    }

}
