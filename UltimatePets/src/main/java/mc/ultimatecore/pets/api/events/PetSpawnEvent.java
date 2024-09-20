package mc.ultimatecore.pets.api.events;

import lombok.*;
import mc.ultimatecore.pets.objects.Pet;
import mc.ultimatecore.pets.objects.PetData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.*;

@Getter
public class PetSpawnEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Pet pet;

    private final PetData petData;

    public PetSpawnEvent(Player player, Pet pet, PetData petData) {
        super(player);
        this.pet = pet;
        this.petData = petData;
    }

    @NotNull
    public final HandlerList getHandlers() {
        return handlers;
    }
}
