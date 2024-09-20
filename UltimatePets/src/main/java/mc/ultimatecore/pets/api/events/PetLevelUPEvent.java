package mc.ultimatecore.pets.api.events;

import lombok.*;
import mc.ultimatecore.pets.objects.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.*;

@Getter
public class PetLevelUPEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Pet pet;

    private final int petLevel;

    public PetLevelUPEvent(Player player, Pet pet, int petLevel) {
        super(player);
        this.pet = pet;
        this.petLevel = petLevel;
    }

    @NotNull
    public final HandlerList getHandlers() {
        return handlers;
    }
}
