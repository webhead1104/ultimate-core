package mc.ultimatecore.pets.database.implementations;

import mc.ultimatecore.helper.database.*;
import mc.ultimatecore.pets.*;
import mc.ultimatecore.pets.database.*;
import mc.ultimatecore.pets.objects.*;
import org.bukkit.*;

public class SQLiteDatabase extends SQLDatabase {

    public SQLiteDatabase(HyperPets plugin, Credentials credentials) {
        super(plugin, "Players");
        this.plugin.getLogger().info("Using SQLite (local) database.");
        this.connect(credentials);
    }

    @Override
    public void addIntoPetsDatabase(PetData petData) {
        this.execute("INSERT OR IGNORE INTO " + PETS_TABLE_NAME + " VALUES(?,?,?,?,?)", petData.getPetUUID().toString(), petData.getPetName(), petData.getLevel(), petData.getXp(), petData.getTier().name());
    }

    @Override
    public void addIntoPlayerDatabase(OfflinePlayer offlinePlayer) {
        this.execute("INSERT OR IGNORE INTO " + playersTable + " VALUES(?,?,?)", offlinePlayer.getUniqueId().toString(), -1, "");
    }
}
