package de.richardvierhaus.ts3webbridge.webserver;

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import de.richardvierhaus.ts3webbridge.teamspeak.RedactedClient;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.teamspeak.servergroups.PermissionLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelContent {

    private final Channel channel;
    private final List<RedactedClient> clients;
    private final boolean visible;


    public ChannelContent(Channel channel, boolean visible, PermissionLevel level) {
        this.visible = visible;
        this.channel = channel;
        this.clients = visible ? Ts3Bot.getClientsByChannel(channel.getId()).stream()
                .map(client -> new RedactedClient(client, level))
                .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
    }
}
