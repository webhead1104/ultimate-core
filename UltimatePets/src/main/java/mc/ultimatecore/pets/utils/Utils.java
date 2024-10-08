package mc.ultimatecore.pets.utils;

import com.cryptomorin.xseries.*;
import de.tr7zw.changeme.nbtapi.*;
import mc.ultimatecore.pets.Item;
import mc.ultimatecore.pets.*;
import mc.ultimatecore.pets.objects.*;
import mc.ultimatecore.pets.objects.commands.*;
import mc.ultimatecore.pets.objects.potions.*;
import mc.ultimatecore.pets.objects.stats.*;
import mc.ultimatecore.pets.playerdata.*;
import net.advancedplugins.ae.utils.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.*;

public class Utils {
    private static final boolean SUPPORT;

    static {
        boolean support = true;
        try {
            NBT.itemStackToNBT(Objects.requireNonNull(XMaterial.DIRT.parseItem())).setUUID("Id", UUID.randomUUID());
        } catch (Exception e) {
            support = false;
        }
        SUPPORT = support;
    }

    public static ItemStack makeItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null)
            return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        if (m != null) {
            m.setLore(color(lore));
            m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(Item item) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                itemstack = SkullCreator.withBase64(Objects.requireNonNull(itemstack), item.headData);
                return itemstack;
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItemHidden(Item item) {
        try {
            ItemStack itemstack = makeItemHidden(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                if (SUPPORT)
                    skull.setUUID("Id", UUID.randomUUID());
                else
                    skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItemHidden(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItemHidden(Item item, List<Placeholder> placeholders) {
        try {
            ItemStack itemstack = makeItemHidden(item.material, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(item.lore, placeholders)));
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                if (SUPPORT)
                    skull.setUUID("Id", UUID.randomUUID());
                else
                    skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            }
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItemHidden(XMaterial.STONE, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(item.lore, placeholders)));
        }
    }


    public static ItemStack makeItemHidden(Item item, List<Placeholder> placeholders, Pet pet) {
        try {
            List<String> lore = new ArrayList<>();
            for (String line : item.lore) {
                if (line.contains("%pet_description%")) {
                    lore.addAll(pet.description());
                    continue;
                }
                lore.add(line);
            }
            ItemStack itemstack = makeItemHidden(item.material, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(lore, placeholders)));
            if (item.material == XMaterial.PLAYER_HEAD && pet.texture() != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                if (SUPPORT)
                    skull.setUUID("Id", UUID.randomUUID());
                else
                    skull.setString("Id", UUID.randomUUID().toString());
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", pet.texture());
                return nbtItem.getItem();
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItemHidden(XMaterial.STONE, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(item.lore, placeholders)));
        }
    }

    public static ItemStack makeItemHidden(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null)
            return null;
        item.setAmount(amount);
        ItemMeta m = Objects.requireNonNull(item.getItemMeta());
        if (lore != null)
            m.setLore(color(lore));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        m.setDisplayName(color(name));
        item.setItemMeta(m);
        return item;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Utils::color).collect(Collectors.toList());
    }

    public static void removePet(Player player, int petUUID) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) continue;
            NBTItem nbtItem = new NBTItem(itemStack.clone());
            if (!nbtItem.hasTag("petUUID") || nbtItem.getInteger("petUUID") != petUUID) continue;
            player.getInventory().removeItem(itemStack);
        }
    }


    public static List<Placeholder> getPetEquippedPlaceholders(User user) {
        PlayerPet petManager = user.getPlayerPet();
        Pet pet = HyperPets.getInstance().getPets().getPetByID(petManager.getPetData().getPetName());
        int level = petManager.getPetData().getLevel();
        double xp = petManager.getPetData().getXp();
        Tier tier = petManager.getPetData().getTier();
        double maxXP = pet == null || tier == null ? 0 : pet.getLevelRequirement(tier.name(), level);
        String equipMessage = user.spawnedID == petManager.getPetData().getPetUUID() ? HyperPets.getInstance().getMessages().getMessage("petUnequipMessage") : HyperPets.getInstance().getMessages().getMessage("petEquipMessage");
        int currentPercentage = Math.min(getCurrentPercentage((int) xp, (int) maxXP), 100);
        return new ArrayList<Placeholder>() {{
            add(new Placeholder("pet_name", pet != null ? pet.displayName() : ""));
            add(new Placeholder("current_percentage", String.valueOf(round(currentPercentage))));
            add(new Placeholder("pet_level", String.valueOf(level)));
            add(new Placeholder("next_level", String.valueOf(level + 1)));
            add(new Placeholder("progress_bar", getProgressBar(currentPercentage)));
            add(new Placeholder("equip_message", equipMessage));
            add(new Placeholder("tier", tier != null ? tier.displayName() : ""));
            add(new Placeholder("player", user.getName()));
            add(new Placeholder("max_xp", String.valueOf(maxXP)));
            add(new Placeholder("current_xp", String.valueOf(xp)));
            addAll(getSkillsPlaceholders(pet, tier, level));
        }};
    }

    public static Set<PetPotion> getPotions(Set<String> str) {
        Set<PetPotion> potions = new HashSet<>();
        for (String string : str) {
            try {
                String[] split = string.split(":");
                potions.add(new PetPotion(split[0], Integer.parseInt(split[1])));
            } catch (Exception ignored) {
            }
        }
        return potions;
    }

    public static Set<PetCommand> getCommands(YamlConfiguration cf, String path) {
        Set<PetCommand> commands = new HashSet<>();
        ConfigurationSection section = cf.getConfigurationSection(path);
        if (section == null) return commands;
        for (String key : section.getKeys(false)) {
            try {
                String command = cf.getString(path + "." + key + ".command");
                PetCommandType petCommandType = PetCommandType.valueOf(cf.getString(path + "." + key + ".type"));
                int seconds = cf.getInt(path + "." + key + ".seconds");
                commands.add(new PetCommand(command, petCommandType, seconds));
            } catch (Exception ignored) {
            }
        }
        return commands;
    }

    public static List<Placeholder> getPetUnequippedPlaceholders(User user, PetData petData) {
        Pet pet = HyperPets.getInstance().getPets().getPetByID(petData.getPetName());
        if (pet == null) {
            return new ArrayList<>();
        }
        int level = petData.getLevel();
        double xp = petData.getXp();
        Tier tier = petData.getTier();
        double maxXP = pet.getLevelRequirement(tier.name(), level);
        String equipMessage = user.spawnedID == petData.getPetUUID() ? HyperPets.getInstance().getMessages().getMessage("petUnequipMessage") : HyperPets.getInstance().getMessages().getMessage("petEquipMessage");
        int currentPercentage = Math.min(getCurrentPercentage((int) xp, (int) maxXP), 100);
        return new ArrayList<Placeholder>() {{
            add(new Placeholder("pet_name", pet.displayName()));
            add(new Placeholder("current_percentage", String.valueOf(round(currentPercentage))));
            add(new Placeholder("pet_level", String.valueOf(level)));
            add(new Placeholder("next_level", String.valueOf(level + 1)));
            add(new Placeholder("progress_bar", getProgressBar(currentPercentage)));
            add(new Placeholder("equip_message", equipMessage));
            add(new Placeholder("player", user.getName()));
            add(new Placeholder("tier", tier.displayName()));
            add(new Placeholder("max_xp", String.valueOf(maxXP)));
            add(new Placeholder("current_xp", String.valueOf(xp)));
            addAll(getSkillsPlaceholders(pet, tier, level));
        }};
    }


    public static List<Placeholder> getPetItemPlaceholders(PetData petData) {
        Pet pet = HyperPets.getInstance().getPets().getPetByID(petData.getPetName());
        int level = petData.getLevel();
        double xp = petData.getXp();
        Tier tier = petData.getTier();
        double maxXP = pet.getLevelRequirement(tier.name(), level);
        int currentPercentage = Math.min(getCurrentPercentage((int) xp, (int) maxXP), 100);
        return new ArrayList<Placeholder>() {{
            add(new Placeholder("pet_name", pet.displayName()));
            add(new Placeholder("current_percentage", String.valueOf(round(currentPercentage))));
            add(new Placeholder("pet_level", String.valueOf(level)));
            add(new Placeholder("next_level", String.valueOf(level + 1)));
            add(new Placeholder("tier", tier.displayName()));

            add(new Placeholder("progress_bar", getProgressBar(currentPercentage)));
            add(new Placeholder("max_xp", String.valueOf(maxXP)));
            add(new Placeholder("current_xp", String.valueOf(xp)));
            addAll(getSkillsPlaceholders(pet, tier, level));
        }};
    }

    private static List<Placeholder> getSkillsPlaceholders(Pet pet, Tier tier, int level) {
        List<Placeholder> list = new ArrayList<>();
        if (pet == null || tier == null) return list;
        PetStats petStats = pet.getPetStats(tier.name(), level);
        if (petStats != null) {
            if (petStats instanceof UltimateStats ultimateStats) {
                ultimateStats.petAbilities().forEach((ability, amount) -> list.add(new Placeholder("pet_" + getFormattedName(ability.toString()), String.valueOf(round(amount)))));
                ultimateStats.petPerks().forEach((perk, amount) -> list.add(new Placeholder("pet_" + getFormattedName(perk.toString()), String.valueOf(round(amount)))));
            } else {
                AurelliumStats ultimateStats = (AurelliumStats) petStats;
                ultimateStats.petAbilities().forEach((ability, amount) -> list.add(new Placeholder("pet_" + getFormattedName(ability.toString()), String.valueOf(round(amount)))));
            }
        }
        return list;
    }

    private static String getFormattedName(String str) {
        return str.replaceAll(" ", "_").toLowerCase();
    }


    public static Double round(double value) {
        return Math.round(value * 10) / 10D;
    }

    public static List<String> processMultiplePlaceholders(List<String> lines, List<Placeholder> placeholders) {
        List<String> newlist = new ArrayList<>();
        for (String string : lines)
            newlist.add(processMultiplePlaceholders(string, placeholders));
        return newlist;
    }

    public static String processMultiplePlaceholders(String line, List<Placeholder> placeholders) {
        for (Placeholder placeholder : placeholders)
            line = placeholder.process(line);
        return color(line);
    }

    public static int getCurrentPercentage(int xp, int maxXP) {
        if (xp > 0 && maxXP > 0)
            return (xp * 100) / maxXP;
        return 0;
    }

    public static int getPercentageQuantity(int percentage) {
        if (percentage > 0)
            return (percentage * 30) / 100;
        return 0;
    }

    public static Location getDirection(Location l, double sus) {
        Location to = l.clone();
        l.clone();
        double x = Math.cos(Math.toRadians((l.getYaw() - 180.0F))) * sus;
        double z = Math.sin(Math.toRadians((l.getYaw() - 180.0F))) * sus;
        to.add(x, 1.0D, z);
        to.setDirection(l.getDirection());
        return to;
    }

    public static Item getItemFromConfig(YamlConfiguration yamlConfig, String path) {
        Item item = new Item();
        if (yamlConfig.contains(path + ".material"))
            item.material = XMaterial.valueOf(yamlConfig.getString(path + ".material"));
        if (yamlConfig.contains(path + ".slot")) item.slot = yamlConfig.getInt(path + ".slot");
        item.title = yamlConfig.contains(path + ".title") ? yamlConfig.getString(path + ".title") : "";
        item.lore = yamlConfig.contains(path + ".lore") ? yamlConfig.getStringList(path + ".lore") : new ArrayList<>();
        item.amount = yamlConfig.contains(path + ".amount") ? yamlConfig.getInt(path + ".amount") : 1;
        if (yamlConfig.contains(path + ".command")) item.command = yamlConfig.getString(path + ".command");
        return item;
    }

    public static String getProgressBar(int percentage) {
        StringBuilder bar = new StringBuilder();
        int current = getPercentageQuantity(percentage);
        bar.append("&a|".repeat(Math.max(0, current)));
        bar.append("&c|".repeat(Math.max(0, 30 - current)));
        return bar.toString();
    }
}
