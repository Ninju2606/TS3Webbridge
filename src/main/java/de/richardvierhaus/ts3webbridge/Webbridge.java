package de.richardvierhaus.ts3webbridge;

import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;
import de.richardvierhaus.ts3webbridge.teamspeak.Ts3Bot;
import de.richardvierhaus.ts3webbridge.webserver.WebServer;
import lombok.Getter;

public class Webbridge {

	private static boolean running;

    private static Ts3Bot TS3_BOT;

	public static void main(String[] args) {

		Logger.setLogLevel(LogLevel.DEBUG);
		
		running = true;

		TS3_BOT = new Ts3Bot();
		new Thread(TS3_BOT).start();

		new WebServer().start();
	}

	public static boolean isRunning(){
		return running;
	}

}
