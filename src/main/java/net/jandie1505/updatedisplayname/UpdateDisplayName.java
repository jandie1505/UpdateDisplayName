package net.jandie1505.updatedisplayname;

import com.google.common.collect.ImmutableList;
import net.chaossquad.mclib.AdventureTagResolvers;
import net.chaossquad.mclib.IntegrationsCheck;
import net.chaossquad.mclib.storage.DSSerializer;
import net.chaossquad.mclib.storage.DataStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class UpdateDisplayName extends JavaPlugin implements Listener {
    public static final String CONFIG_EXCLUDE_MODE = "exclude_mode";
    public static final String CONFIG_ENABLE_DISPLAYNAME = "displayname.enable";
    public static final String CONFIG_FORMAT_DISPLAYNAME = "displayname.format";
    public static final String CONFIG_ENABLE_TABLIST_NAME = "tablist.enable";
    public static final String CONFIG_TABLIST_FORMAT = "tablist.format";
    public static final String CONFIG_UPDATE_INTERVAL = "update_interval";

    @NotNull private final DataStorage config;
    @NotNull private final Set<UUID> players;

    private int updateTime;

    public UpdateDisplayName() {
        this.config = new DataStorage();
        this.players = new HashSet<>();
    }

    // ----- ENABLE/DISABLE -----

    @Override
    public void onEnable() {

        // CONFIG

        this.config.set(CONFIG_EXCLUDE_MODE, true);
        this.config.set(CONFIG_ENABLE_DISPLAYNAME, true);
        this.config.set(CONFIG_FORMAT_DISPLAYNAME, "<luckperms:prefix><player><luckperms:suffix>");
        this.config.set(CONFIG_ENABLE_TABLIST_NAME, true);
        this.config.set(CONFIG_TABLIST_FORMAT, "<luckperms:prefix><player><luckperms:suffix>");
        this.config.set(CONFIG_UPDATE_INTERVAL, 60);

        this.reloadConfig();

        // TIME

        this.updateTime = 0;

        // COMMAND

        PluginCommand command = this.getCommand("updatedisplayname");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

        // TASK

        new BukkitRunnable() {
            @Override
            public void run() {
                UpdateDisplayName.this.run();
            }
        }.runTaskTimer(this, 1, 20);

        // LISTENER

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.clear();
        this.players.clear();
    }

    // ----- TASKS -----

    private void run() {
        int maxTime = this.config.optInt(CONFIG_UPDATE_INTERVAL, 60);
        if (maxTime < 0) return;

        if (this.updateTime > maxTime) {
            this.updateTime = 0;
            this.updatePlayers();
        } else {
            this.updateTime++;
        }

    }

    public void updatePlayers() {

        for (Player player : ImmutableList.copyOf(this.getServer().getOnlinePlayers())) {
            if (this.isPlayerExcluded(player)) continue;
            this.updatePlayer(player);
        }

    }

    // ----- EVENTS -----

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.isPlayerExcluded(event.getPlayer())) return;
        this.updatePlayer(event.getPlayer());
    }

    // ----- UPDATE NAME -----

    public void updatePlayer(@NotNull Player player) {

        if (this.config.optBoolean(CONFIG_ENABLE_DISPLAYNAME, false)) {
            player.displayName(this.generatePlayerDisplayName(player, this.config.optString(CONFIG_FORMAT_DISPLAYNAME, null)));
        }

        if (this.config.optBoolean(CONFIG_TABLIST_FORMAT, false)) {
            player.playerListName(this.generatePlayerDisplayName(player, this.config.optString(CONFIG_TABLIST_FORMAT, null)));
        }

    }

    private @Nullable Component generatePlayerDisplayName(@NotNull Player player, @Nullable String format) {
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

    // ----- COMMAND -----

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender != this.getServer().getConsoleSender() && !sender.hasPermission("updatedisplayname.command")) return true;

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /updatedisplayname (reload|update|playerlist)", NamedTextColor.RED));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                this.reloadConfig();
                sender.sendMessage(Component.text("Reloaded config", NamedTextColor.GREEN));
                return true;
            }
            case "update" -> {
                this.updatePlayers();
                sender.sendMessage(Component.text("Updated player displaynames", NamedTextColor.GREEN));
                return true;
            }
            case "playerlist" -> {
                sender.sendMessage(Component.text("Exclude Mode: " + this.config.optBoolean(CONFIG_EXCLUDE_MODE, false), NamedTextColor.GRAY));

                for (UUID playerId : this.players) {
                    OfflinePlayer player = this.getServer().getOfflinePlayer(playerId);
                    sender.sendMessage(Component.text("- " + player.getName() + " (uuid=" + playerId + ")", NamedTextColor.GRAY));
                }

                return true;
            }
            default -> {
                sender.sendMessage(Component.text("Run command without args to get usage.", NamedTextColor.RED));
                return true;
            }
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {

        if (args.length == 1) {
            return List.of("reload", "update", "playerlist");
        }

        return List.of();
    }

    // ----- CONFIG -----

    public void reloadConfig() {

        try {

            DataStorage loadedStorage = DSSerializer.loadConfig(new File(this.getDataFolder(), "config.yml"));
            if (loadedStorage != null) {
                this.config.merge(loadedStorage);
                this.getLogger().info("Config loaded successfully");
            } else {
                YamlConfiguration ymlConfig = DSSerializer.serialize(this.config);
                ymlConfig.set(CONFIG_EXCLUDE_MODE, null);

                ymlConfig.setComments(CONFIG_ENABLE_DISPLAYNAME, List.of("Display name = The name which other players will see everywhere except the tablist"));
                ymlConfig.setComments(CONFIG_FORMAT_DISPLAYNAME, List.of(
                        "Here you can set the display name format.",
                        "The display name is the name players will see ingame everywhere, except for the tablist.",
                        "Please note that this format DOES NOT support legacy color codes. You have to use MiniMessage. But don't worry, it's easy:",
                        "- &0 = <black>, &1 = <dark_blue>, &2 = <dark_green>, etc...",
                        "- &l = <bold>, &m = <strikethrough>, &n = <underline>, &r = <reset>, etc...,",
                        "More information: https://docs.advntr.dev/minimessage/format.html",
                        "Placeholders support both MiniMessage and legacy color codes.",
                        "Available placeholders:",
                        "- LuckPerms: <luckperms:prefix> <luckperms:suffix> <luckperms:group>",
                        "- Vault: <vault:prefix> <vault:suffix> <vault:group>",
                        "- PlaceholderAPI: <papi:PLACEHOLDER>"
                ));
                ymlConfig.setComments(CONFIG_ENABLE_TABLIST_NAME, List.of("Tablist name = The player name in the tablist"));
                ymlConfig.setComments(CONFIG_TABLIST_FORMAT, List.of("Here you can set the tablist name format.", "You can use it like the display name format."));
                ymlConfig.setComments(CONFIG_UPDATE_INTERVAL, List.of(
                        "The interval in seconds the names are updated (default: 60).",
                        "Set to < 0 to disable auto-update.",
                        "When disabled, the name of a player will only updated on join."
                ));

                ymlConfig.save(new File(this.getDataFolder(), "config.yml"));
                this.getLogger().info("Config created successfully");
            }

        } catch (Exception e) {
            this.getLogger().log(Level.WARNING, "Failed to load config", e);
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
