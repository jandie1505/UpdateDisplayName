package net.jandie1505.updatedisplayname.api;

import net.chaossquad.mclib.storage.DataStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface UDNApi {

    // ----- PLAYER UPDATE -----

    /**
     * Updates all player names.
     */
    void updatePlayers();

    /**
     * Updates the player name of the specified player.
     * @param player player
     */
    void updatePlayer(@NotNull Player player);

    // ----- EXCLUDE -----

    /**
     * Check if a player is excluded from changing displayname.
     * @param player player
     * @return true = excluded
     */
    boolean isPlayerExcluded(@NotNull Player player);

    /**
     * Returns the excluded players.<br/>
     * Names of players in this list will not be updated.<br/>
     * @return exclude/include list
     */
    @NotNull Set<UUID> getExcludedPlayers();

    // ----- ENABLE/DISABLE -----

    /**
     * Returns if name updates are enabled.
     * @return updates enabled
     */
    boolean isUpdatesEnabled();

    /**
     * Set updates enabled.<br/>
     * When disabled, the plugin will not update player names.
     * @param updatesEnabled updates enabled
     */
    void setUpdatesEnabled(boolean updatesEnabled);

    // ----- CONFIG -----

    /**
     * Returns the plugin config.
     * @return config
     */
    @NotNull DataStorage getPluginConfig();

}
