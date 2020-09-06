package de.richardvierhaus.ts3webbridge.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    private static LogLevel currentLogLevel = LogLevel.INFO;

    public static void setLogLevel(LogLevel level){
        currentLogLevel = level;
        log("Changed LogLevel to " + level);
    }

    public static void log(String text, LogLevel lvl){
        if(!currentLogLevel.isActive(lvl)){
            return;
        }

        //Build log message
        String logMessage;
        String time = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(Calendar.getInstance().getTime());
        logMessage = "[%s][%s] " + text;
        logMessage = String.format(logMessage, time, lvl.toString());

        //Sysout log message
        System.out.println(logMessage);

        //Save to log message to DB
        //TODO
    }

    public static void log(String text){
        log(text, LogLevel.INFO);
    }
}
