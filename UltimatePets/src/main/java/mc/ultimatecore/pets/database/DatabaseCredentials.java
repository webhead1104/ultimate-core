package mc.ultimatecore.pets.database;

import org.apache.commons.lang.*;
import org.bukkit.configuration.file.*;

public record DatabaseCredentials(String host, String databaseName, String userName, String password, String type,
                                  int port) {

    public static DatabaseCredentials fromConfig(FileConfiguration config) {

        String host = config.getString("mysql.host");
        String dbName = config.getString("mysql.database");
        String userName = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        String type = config.getString("database_type");
        int port = config.getInt("mysql.port");

        Validate.notNull(host);
        Validate.notNull(dbName);
        Validate.notNull(userName);
        Validate.notNull(password);
        Validate.notNull(type);

        return new DatabaseCredentials(host, dbName, userName, password, type, port);
    }

}
