package mc.ultimatecore.farm;

import com.cryptomorin.xseries.*;
import lombok.*;
import mc.ultimatecore.farm.commands.*;
import mc.ultimatecore.farm.configs.*;
import mc.ultimatecore.farm.guardians.Guardian;
import mc.ultimatecore.farm.listeners.*;
import mc.ultimatecore.farm.managers.*;
import mc.ultimatecore.farm.nms.*;
import mc.ultimatecore.farm.utils.*;
import mc.ultimatecore.helper.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import java.util.*;

@Getter
public class HyperRegions extends UltimatePlugin {

    @Getter
    private static HyperRegions instance;
    private Config configuration;
    private Messages messages;
    private Inventories inventories;
    private AddonsManager addonsManager;
    private RegionsManager farmManager;
    private CommandManager commandManager;
    private Guardians guardians;
    private NMS nms;

    @Override
    public void onEnable() {
        instance = this;

        loadConfigs();

        registerListeners(new InventoryClickListener(), new CropBreakListener(this));

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(getInstance(), this::saveConfigs, 1000L, 1900L);

        addonsManager = new AddonsManager(this);

        commandManager = new CommandManager("ultimatefarm");

        commandManager.registerCommands();

        loadNMS();

        if (nms == null) {
            setEnabled(false);
            return;
        }

        Bukkit.getConsoleSender().sendMessage(Utils.color("&e" + getDescription().getName() + " Has been enabled!"));
    }

    private void loadNMS() {
        nms = new VersionMatcher().match();
    }

    @Override
    public void onDisable() {
        Optional.ofNullable(guardians).ifPresent(guardians1 -> guardians.getGuardians().forEach(Guardian::remove));

        Optional.ofNullable(farmManager).ifPresent(manager -> manager.blockRegenCache.forEach(blockRegen -> blockRegen.disableRegen(true)));

        saveConfigs();

        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);

        getLogger().info(getDescription().getName() + " Disabled!");
    }

    public void sendErrorMessage(Exception e) {
        e.printStackTrace();
    }

    public void registerListeners(Listener... listener) {
        for (Listener l : listener)
            Bukkit.getPluginManager().registerEvents(l, this);
    }

    public void loadConfigs() {
        configuration = new Config(this, "config", true);
        guardians = new Guardians(this, "guardians", false);
        farmManager = XMaterial.supports(13) ? new RegionsManager(this, "regionsManager", false) : new RegionsManager(this, "regionManager", false);
        inventories = new Inventories();
        messages = new Messages();
    }

    public void reloadConfigs() {
        Optional.ofNullable(guardians).ifPresent(Guardians::reload);
        Optional.ofNullable(farmManager).ifPresent(RegionsManager::reload);
        Optional.ofNullable(configuration).ifPresent(Config::reload);
    }

    public void saveConfigs() {
        Optional.ofNullable(guardians).ifPresent(Guardians::reload);
    }
}