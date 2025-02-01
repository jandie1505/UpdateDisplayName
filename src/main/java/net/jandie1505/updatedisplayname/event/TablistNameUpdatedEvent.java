package net.jandie1505.updatedisplayname.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event fired before the plugin updates the tablist name of a player.
 */
public class TablistNameUpdatedEvent extends Event implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();
    @NotNull private final Player player;
    @Nullable private final Component updatedTablistName;
    private boolean cancelled;

    public TablistNameUpdatedEvent(@NotNull Player player, @Nullable Component updatedTablistName) {
        this.player = player;
        this.updatedTablistName = updatedTablistName;
        this.cancelled = false;
    }

    /**
     * Returns the player the tablist name is updated from.
     * @return player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Returns the new tablist name of the player.
     * @return new tablist name
     */
    public @Nullable Component getUpdatedTablistName() {
        return updatedTablistName;
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
