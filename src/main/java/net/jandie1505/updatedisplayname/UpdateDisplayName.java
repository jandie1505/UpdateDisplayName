package net.jandie1505.updatedisplayname;

import net.chaossquad.mclib.AdventureTagResolvers;
import net.chaossquad.mclib.IntegrationsCheck;
import net.chaossquad.mclib.storage.DataStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UpdateDisplayName extends JavaPlugin implements Listener {
    public static final String CONFIG_EXCLUDE_MODE = "exclude_mode";
    public static final String CONFIG_FORMAT = "format";
    public static final String CONFIG_ALLOW_LEGACY_FORMAT = "allow_legacy_format";

    @NotNull private final DataStorage config;
    @NotNull private final Set<UUID> players;

    public UpdateDisplayName() {
        this.config = new DataStorage();
        this.players = new HashSet<>();
    }

    // ----- ENABLE/DISABLE -----

    @Override
    public void onEnable() {

        this.config.set(CONFIG_EXCLUDE_MODE, true);
        this.config.set(CONFIG_FORMAT, "<luckperms:prefix><player>");

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.clear();
        this.players.clear();
    }

    // ----- EVENTS -----

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.isPlayerExcluded(event.getPlayer())) return;
        event.getPlayer().displayName(this.generatePlayerDisplayName(event.getPlayer()));
    }

    // ----- UPDATE NAME -----

    public @Nullable Component generatePlayerDisplayName(@NotNull Player player) {
        String format = this.config.optString(CONFIG_FORMAT, null);
        if (format == null || format.isEmpty()) return null;

        return MiniMessage.miniMessage().deserialize(format, tagResolvers(player).toArray(new TagResolver[0]));
    }

    // ----- MANAGEMENT -----

    /**
     * Check if a player is excluded from changing displayname.
     * @param player player
     * @return true = excluded
     */
    public boolean isPlayerExcluded(@NotNull Player player) {

        if (this.config.optBoolean(CONFIG_EXCLUDE_MODE, false)) {
            return this.players.contains(player.getUniqueId());
        } else {
            return !this.players.contains(player.getUniqueId());
        }

    }

    // ----- TOOLS -----

    private List<TagResolver> tagResolvers(final @NotNull Player player) {
        List<TagResolver> resolvers = new ArrayList<>();

        resolvers.add(TagResolver.resolver("player", Tag.inserting(player.name())));
        if (IntegrationsCheck.luckPerms()) resolvers.add(AdventureTagResolvers.luckPermsPlayer("luckperms", player, true));
        if (IntegrationsCheck.placeholderAPI()) resolvers.add(AdventureTagResolvers.placeholderAPI("papi", player, true));
        if (IntegrationsCheck.vault()) resolvers.add(AdventureTagResolvers.vault("vault", player, true));

        return resolvers;
    }

}
