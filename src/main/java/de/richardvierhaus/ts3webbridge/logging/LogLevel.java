package de.richardvierhaus.ts3webbridge.logging;

public enum LogLevel {
    DEBUG(4),
    INFO(3),
    WARN(2),
    ERROR(1);

    private final int level;

    LogLevel(int level){
        this.level = level;
    }

    protected boolean isActive(LogLevel lvl){
        return this.level >= lvl.level;
    }
}
