package de.richardvierhaus.ts3webbridge.webserver;

import com.google.gson.Gson;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.Session;
import de.richardvierhaus.ts3webbridge.teamspeak.sessions.SessionController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ClientInfoController {

    @RequestMapping(value = "/user", produces = "application/json")
    public String user(@RequestParam(name = "uid", required = true) String uid, HttpServletRequest request) {
        ClientResponse clientResponse = new ClientResponse(uid);
        return new Gson().toJson(clientResponse);
    }

}
