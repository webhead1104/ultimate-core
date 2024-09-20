package mc.ultimatecore.pets.objects.stats;

import mc.ultimatecore.skills.*;
import mc.ultimatecore.skills.objects.abilities.*;
import mc.ultimatecore.skills.objects.perks.*;
import org.bukkit.entity.*;

import java.util.*;

public record UltimateStats(Map<Ability, Double> petAbilities, Map<Perk, Double> petPerks) implements PetStats {
    public void addStats(Player player) {
        petAbilities.forEach((ability, amount) -> HyperSkills.getInstance().getApi()
                .addArmorAbility(player.getUniqueId(), ability, amount));
        petPerks.forEach((perk, amount) -> HyperSkills.getInstance().getApi()
                .addArmorPerk(player.getUniqueId(), perk, amount));
    }

    public void removeStats(Player player) {
        petAbilities.forEach((ability, amount) -> HyperSkills.getInstance().getApi()
                .removeArmorAbility(player.getUniqueId(), ability, petAbilities.get(ability)));
        petPerks.forEach((ability, amount) -> HyperSkills.getInstance().getApi()
                .removeArmorPerk(player.getUniqueId(), ability, amount));
    }
}
