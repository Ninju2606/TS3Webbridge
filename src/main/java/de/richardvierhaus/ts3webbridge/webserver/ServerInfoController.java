package de.richardvierhaus.ts3webbridge.webserver;

import com.google.gson.Gson;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.teamspeak.servergroups.PermissionLevel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ServerInfoController {

    @RequestMapping(value = "/serverlist", produces = "application/json")
    public String info(HttpServletRequest request) {
        PermissionLevel permissionLevel = PermissionLevel.getPermissionLevel(request.getRemoteAddr());
        List<ChannelContent> channels = new ArrayList<>();
        Ts3Bot.getAllChannels().forEach((c) -> channels.add(new ChannelContent(c, permissionLevel.canSubscribe(c), permissionLevel)));
        return new Gson().toJson(channels);
    }


}
