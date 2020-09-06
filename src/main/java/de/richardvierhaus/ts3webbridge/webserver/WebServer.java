package de.richardvierhaus.ts3webbridge.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebServer {

    public void start(){
        SpringApplication.run(WebServer.class);
    }


}
