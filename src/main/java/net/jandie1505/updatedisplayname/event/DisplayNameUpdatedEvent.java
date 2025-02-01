package net.jandie1505.updatedisplayname.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event fired before the plugin updates the display name of a player.
 */
public class DisplayNameUpdatedEvent extends Event implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();
    @NotNull private final Player player;
    @Nullable private final Component updatedDisplayName;
    private boolean cancelled;

    public DisplayNameUpdatedEvent(@NotNull Player player, @Nullable Component updatedDisplayName) {
        this.player = player;
        this.updatedDisplayName = updatedDisplayName;
        this.cancelled = false;
    }

    /**
     * Returns the player the display name is updated from.
     * @return player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Returns the new display name of the player.
     * @return new display name
     */
    public @Nullable Component getUpdatedDisplayName() {
        return updatedDisplayName;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
