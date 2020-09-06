package de.richardvierhaus.ts3webbridge.teamspeak.events;

import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;

public class LeaveListener extends TS3EventAdapter {

    @Override
    public void onClientLeave(ClientLeaveEvent event) {
        Logger.log("Client with clientId " + event.getClientId() + " left", LogLevel.DEBUG);

        Ts3Bot.getSessionController().removeSessionByClientId(event.getClientId());
        Ts3Bot.getNicknameWatcher().removeClient(event.getClientId());
    }
}
