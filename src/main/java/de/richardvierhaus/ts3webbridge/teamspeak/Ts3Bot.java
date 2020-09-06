package de.richardvierhaus.ts3webbridge.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;
import de.richardvierhaus.ts3webbridge.teamspeak.events.JoinListener;
import de.richardvierhaus.ts3webbridge.teamspeak.events.LeaveListener;
import de.richardvierhaus.ts3webbridge.teamspeak.nicknames.NicknameWatcher;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.SessionController;

import java.util.List;
import java.util.stream.Collectors;

public class Ts3Bot implements Runnable {

    private static TS3Api api;
    private static TS3Query query;


    private static SessionController sessionController;
    private static NicknameWatcher nicknameWatcher;

    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String HOST = "";
    private static final int PORT = 9987;

    @Override
    public void run() {
        Logger.log("TS3 Bot starting...");

        TS3Config config = new TS3Config();
        config.setHost(HOST);
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED);

        query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        api.login(USERNAME, PASSWORD);
        api.selectVirtualServerByPort(PORT);

        sessionController = new SessionController();

        registerEvents();

        nicknameWatcher = new NicknameWatcher();
        new Thread(nicknameWatcher.run()).start();

        Logger.log("TS3 Bot started");
    }

    private static void registerEvents() {
        Logger.log("Registering Events", LogLevel.DEBUG);

        api.registerEvent(TS3EventType.SERVER);

        api.addTS3Listeners(new JoinListener());
        api.addTS3Listeners(new LeaveListener());
    }

    public static SessionController getSessionController() {
        return sessionController;
    }

    public static NicknameWatcher getNicknameWatcher(){
        return nicknameWatcher;
    }

    public static ClientInfo getClientInfoByClientId(final int clientId) {
        return api.getClientInfo(clientId);
    }

    public static List<ClientInfo> getConnectedClientsWithUid(final String uid) {
        return api.getClients().stream().filter((c) -> c.getUniqueIdentifier().equals(uid)).map((c) -> api.getClientInfo(c.getId())).collect(Collectors.toList());
    }

    public static List<Client> getAllClients(){
        return api.getClients();
    }

    public static List<Channel> getAllChannels(){
        return api.getChannels();
    }

    public static List<ClientInfo> getClientsByChannel(final int channelId){
        return api.getClients().stream().filter((c) -> c.getChannelId() == channelId).map((c) -> api.getClientInfo(c.getId())).collect(Collectors.toList());
    }

    public static ClientInfo getClientById(final int clientId){
        return api.getClientInfo(clientId);
    }

    public static List<ServerGroup> getClientServerGroups(final int dbId){ return api.getServerGroupsByClientId(dbId);}
}
