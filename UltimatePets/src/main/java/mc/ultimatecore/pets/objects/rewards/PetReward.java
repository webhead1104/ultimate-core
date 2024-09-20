package mc.ultimatecore.pets.objects.rewards;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public record PetReward(List<String> commands) {
    public void execute(Player player) {
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
    }
}
