package mc.ultimatecore.skills.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GUI extends InventoryHolder{
    void onInventoryClick(InventoryClickEvent event);

}
