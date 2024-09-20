package mc.ultimatecore.pets.objects;

import mc.ultimatecore.pets.*;
import mc.ultimatecore.pets.objects.commands.*;
import mc.ultimatecore.pets.objects.potions.*;
import mc.ultimatecore.pets.objects.rewards.*;
import mc.ultimatecore.pets.objects.stats.*;
import org.bukkit.entity.*;

import java.util.*;

public record Pet(String displayName, String entityName, List<String> description, String texture,
                  Map<String, PetLevel> petLevels, PetCommands petCommands) {

    public Double getLevelRequirement(String tier, int level) {
        if (petLevels.containsKey(tier))
            return petLevels.get(tier).getRequirement(level);
        return 0.0D;
    }

    public int getMaxLevel(String tier) {
        if (petLevels.containsKey(tier))
            return petLevels.get(tier).getRequirements().size();
        return 0;
    }

    public boolean tierHasNextLevel(String tier, int level) {
        return petLevels.get(tier).getRequirements().containsKey(level);
    }

    public void applyNewStats(Player player, String tier, int level) {
        PetLevel petLevel = petLevels.getOrDefault(tier, null);
        if (petLevel == null) return;
        //Abilities
        if (HyperPets.getInstance().getAddonsManager().isHyperSkills()) {
            PetStats petStats = petLevel.getPetStats(level);
            if (petStats != null) petStats.addStats(player);
        }
        //Potions
        PetPotions petPotions = petLevel.getPetPotions(level);
        if (petPotions != null) petPotions.apply(player);
    }

    public void removeStats(Player player, String tier, int level) {
        PetLevel petLevel = petLevels.getOrDefault(tier, null);
        if (petLevel == null) return;
        //Abilities
        if (HyperPets.getInstance().getAddonsManager().isHyperSkills()) {
            PetStats petStats = petLevel.getPetStats(level);
            if (petStats != null) petStats.removeStats(player);
        }
        //Potions
        PetPotions petPotions = petLevel.getPetPotions(level);
        if (petPotions != null) petPotions.remove(player);
    }

    public PetReward getPetReward(String tier, int level) {
        PetLevel petLevel = petLevels.getOrDefault(tier, null);
        if (petLevel != null)
            return petLevel.getPetRewards(level);
        return null;
    }

    public PetStats getPetStats(String tier, int level) {
        PetLevel petLevel = petLevels.getOrDefault(tier, null);
        if (petLevel != null)
            return petLevel.getPetStats(level);
        return null;
    }
}
