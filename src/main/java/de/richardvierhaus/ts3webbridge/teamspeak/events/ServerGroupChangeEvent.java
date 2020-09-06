package de.richardvierhaus.ts3webbridge.teamspeak.events;

import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import lombok.Getter;

public class ServerGroupChangeEvent {

    @Getter
    private String clientId;
    private ServerGroup group;
    private long time;


    public ServerGroupChangeEvent(String clientId, ServerGroup group) {
        this.clientId = clientId;
        this.group = group;
        time = System.currentTimeMillis();
    }


}
