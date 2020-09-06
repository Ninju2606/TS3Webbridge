package de.richardvierhaus.ts3webbridge.database;

import de.richardvierhaus.ts3webbridge.logging.LogLevel;
import de.richardvierhaus.ts3webbridge.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQLManager {

    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String DATABASE_URL = "";

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    // init connection object
    private static Connection connection;
    // init properties object
    private static Properties properties;

    // create properties
    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("useSSL", "true");
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", "250");
        }
        return properties;
    }

    // connect database
    public synchronized static Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                Logger.log("MySQL connected", LogLevel.DEBUG);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public synchronized static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                Logger.log("MySQL disconnected", LogLevel.DEBUG);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
