package mc.ultimatecore.pets.objects.stats;

import com.archyx.aureliumskills.stats.*;
import mc.ultimatecore.pets.*;
import org.bukkit.entity.*;

import java.util.*;

public record AurelliumStats(Map<Stat, Double> petAbilities) implements PetStats {
    @Override
    public void addStats(Player player) {
        petAbilities.forEach((ability, amount) -> HyperPets.getInstance().getAddonsManager().getAurelliumSkills()
                .addStats(player, ability, amount));
    }

    @Override
    public void removeStats(Player player) {
        petAbilities.forEach((ability, amount) -> HyperPets.getInstance().getAddonsManager().getAurelliumSkills()
                .removeStats(player, ability));
    }
}