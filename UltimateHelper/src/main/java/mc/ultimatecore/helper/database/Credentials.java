package mc.ultimatecore.helper.database;

import org.apache.commons.lang.*;
import org.bukkit.configuration.file.*;

public record Credentials(String host, String databaseName, String userName, String password, DatabaseType databaseType,
                          int port) {

    public static Credentials fromConfig(FileConfiguration config) {
        String host = config.getString("mysql.host");
        String dbName = config.getString("mysql.database");
        String userName = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        int port = config.getInt("mysql.port");
        DatabaseType databaseType = DatabaseType.valueOf(config.getString("database_type"));

        Validate.notNull(host);
        Validate.notNull(dbName);
        Validate.notNull(userName);
        Validate.notNull(password);
        return new Credentials(host, dbName, userName, password, databaseType, port);
    }
}
