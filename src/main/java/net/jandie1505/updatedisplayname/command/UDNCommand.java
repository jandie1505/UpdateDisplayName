package net.jandie1505.updatedisplayname.command;

import net.chaossquad.mclib.command.TabCompletingCommandExecutor;
import net.jandie1505.updatedisplayname.UpdateDisplayName;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class UDNCommand implements TabCompletingCommandExecutor {
    @NotNull private final UpdateDisplayName plugin;

    public UDNCommand(@NotNull UpdateDisplayName plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender != this.plugin.getServer().getConsoleSender() && !sender.hasPermission("updatedisplayname.command")) return true;

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /updatedisplayname (reload|update|playerlist)", NamedTextColor.RED));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                this.plugin.reloadConfig();
                sender.sendMessage(Component.text("Reloaded config", NamedTextColor.GREEN));
                return true;
            }
            case "update" -> {
                this.plugin.updatePlayers();
                sender.sendMessage(Component.text("Updated player displaynames", NamedTextColor.GREEN));
                return true;
            }
            case "playerlist" -> {
                sender.sendMessage(Component.text("Exclude Mode: " + this.plugin.getPluginConfig().optBoolean(UpdateDisplayName.CONFIG_EXCLUDE_MODE, false), NamedTextColor.GRAY));

                for (UUID playerId : this.plugin.getPlayers()) {
                    OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playerId);
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

    public @NotNull UpdateDisplayName getPlugin() {
        return plugin;
    }

}
