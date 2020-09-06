package de.richardvierhaus.ts3webbridge.teamspeak.servergroups;

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.List;

public enum PermissionLevel {
    ADMIN(100, true),
    MODERATOR(15, false),
    DEFAULT(10, false);

    private final int subscribePower;
    private final boolean seeIp;

    PermissionLevel(final int subscribePower, boolean seeIp) {
        this.subscribePower = subscribePower;
        this.seeIp = seeIp;
    }

    public boolean canSubscribe(final Channel channel){
        return channel.getNeededSubscribePower() <= this.subscribePower;
    }

    public boolean canSeeIp() {
        return seeIp;
    }

    public static PermissionLevel getPermissionLevel(final String ip){
        List<String> serverGroups = GroupController.getServerGroupsByIp(ip);
        if(serverGroups.contains("Admin"))
            return ADMIN;
        else if (serverGroups.contains("Moderator"))
            return MODERATOR;
        return DEFAULT;
    }
}
