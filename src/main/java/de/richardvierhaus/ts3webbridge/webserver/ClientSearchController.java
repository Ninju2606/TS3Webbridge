package de.richardvierhaus.ts3webbridge.webserver;

import com.google.gson.Gson;
import de.richardvierhaus.ts3webbridge.database.MySQLManager;
import de.richardvierhaus.ts3webbridge.teamspeak.RedactedClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ClientSearchController {

    @RequestMapping(value = "/search", produces = "application/json")
    public ResponseEntity search(@RequestParam(name = "key", required = true) String key, HttpServletRequest request) {
        if ("".equals(key) || key.length() < 3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid query");
        }
        List<ClientListObject> clientsFound = searchClient(key);
        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(clientsFound));
    }

    private List<ClientListObject> searchClient(final String key) {
        List<ClientListObject> clientsFound = new ArrayList<>();
        try {
            PreparedStatement statement = MySQLManager.connect().prepareStatement("SELECT DISTINCT sessionlist.client_uid FROM sessionlist " +
                    "INNER JOIN nicknamelog ON sessionlist.client_uid = nicknamelog.client_uid " +
                    "WHERE nicknamelog.nickname LIKE ? OR sessionlist.client_ip LIKE ? OR sessionlist.client_uid LIKE ?");
            final String query = "%" + key + "%";
            statement.setString(1, query);
            statement.setString(2, query);
            statement.setString(3, query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                clientsFound.add(new ClientListObject(resultSet.getString("client_uid")));
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            MySQLManager.disconnect();
        }
        return clientsFound;
    }

}
