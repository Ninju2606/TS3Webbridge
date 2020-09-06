package de.richardvierhaus.ts3webbridge.teamspeak.nicknames;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.richardvierhaus.ts3webbridge.Webbridge;
import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NicknameWatcher {

    private Map<Integer, String> nicknames;
    private List<Integer> changedNames;

    public NicknameWatcher() {
        this.nicknames = new HashMap<>();
        this.changedNames = new ArrayList<>();
        Ts3Bot.getAllClients().forEach(c -> nicknames.put(c.getId(), c.getNickname()));
    }

    private void checkNickname(int cId) {
        ClientInfo c = Ts3Bot.getClientById(cId);

        if (c.getNickname().equals(nicknames.get(cId))) return;

        Logger.log("Client " + c.getUniqueIdentifier() + " changed name from " + nicknames.get(cId) + " to " + c.getNickname(), LogLevel.DEBUG);
        NicknameLogger.addNickname(c.getUniqueIdentifier(), c.getNickname());

        changedNames.add(cId);

    }

    public Runnable run() {
        return () -> {
            while (Webbridge.isRunning()) {
                nicknames.keySet().forEach(cId -> checkNickname(cId));

                changedNames.forEach(id -> {
                    nicknames.remove(id);
                    nicknames.put(id, Ts3Bot.getClientById(id).getNickname());
                });
                changedNames.clear();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void addClient(ClientInfo c) {
        nicknames.put(c.getId(), c.getNickname());
        String lastName = NicknameLogger.getLastNameByUid(c.getUniqueIdentifier());
        if ("".equals(lastName) || !lastName.equals(c.getNickname()))
            NicknameLogger.addNickname(c.getUniqueIdentifier(), c.getNickname());
    }

    public void removeClient(int cId) {
        nicknames.remove(cId);
    }
}
