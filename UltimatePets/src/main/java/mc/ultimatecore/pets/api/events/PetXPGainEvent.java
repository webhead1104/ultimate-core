package mc.ultimatecore.pets.api.events;

import lombok.*;
import mc.ultimatecore.pets.objects.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.*;

@Getter
public class PetXPGainEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Pet pet;

    private final double xpAmount;

    public PetXPGainEvent(Player player, Pet pet, double xpAmount) {
        super(player);
        this.pet = pet;
        this.xpAmount = xpAmount;
    }

    @NotNull
    public final HandlerList getHandlers() {
        return handlers;
    }

}