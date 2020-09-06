package de.richardvierhaus.ts3webbridge.teamspeak;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.richardvierhaus.ts3webbridge.teamspeak.servergroups.PermissionLevel;

import java.util.Map;


public class RedactedClient {

    private static String[] blacklist = {"client_security_hash", "client_myteamspeak_id", "client_signed_badges", "connection_filetransfer_bandwidth_received", "connection_bytes_received_total", "client_login_name", "client_integrations", "client_meta_data", "connection_packets_received_total", "client_month_bytes_uploaded", "connection_client_ip", "connection_bytes_sent_total", "client_default_token", "client_base64HashClientUID", "client_version_sign", "client_total_bytes_downloaded", "connection_packets_sent_total", "connection_bandwidth_sent_last_minute_total", "connection_filetransfer_bandwidth_sent", "client_total_bytes_uploaded", "client_lastconnected", "connection_bandwidth_received_last_minute_total", "connection_bandwidth_sent_last_second_total", "connection_bandwidth_received_last_second_total", "client_month_bytes_downloaded"};

    private final int clientId;
    private final Map<String, String> map;

    public RedactedClient(ClientInfo info, PermissionLevel level) {
        this.clientId = info.getId();
        this.map = info.getMap();

        if (level == PermissionLevel.ADMIN) {
            return;
        }
        for (String s : blacklist) {
            map.remove(s);
        }
    }
}
