package com.purityvanilla.pvperks.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.TimeFormatting;
import com.purityvanilla.pvperks.PVPerks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {
    private final PVPerks plugin;

    public PlaytimeCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        OfflinePlayer target;

        // Assume player is checking own playtime if no argument given
        if (args.length < 1) {
            if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("playtime-usage"))) return true;
            target = (OfflinePlayer) sender;
            long playtimeSeconds = getPlayerPlaytimeSeconds(target);
            String playtimeString = TimeFormatting.PrettyDurationString(playtimeSeconds);

            TagResolver resolver = TagResolver.resolver(Placeholder.component("playtime", Component.text(playtimeString)));
            sender.sendMessage(plugin.config().getMessage("playtime-self", resolver));
            return true;
        }

        UUID targetUUID = PVCore.getAPI().getPlayerAPI().getUUIDFromName(args[0].toLowerCase());
        if (targetUUID == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        target = plugin.getServer().getOfflinePlayer(targetUUID);
        String targetName = PVCore.getAPI().getPlayerAPI().getPlayerUsername(targetUUID);
        long playTimeSeconds = getPlayerPlaytimeSeconds(target);
        String playTimeString = TimeFormatting.PrettyDurationString(playTimeSeconds);

        TagResolver resolver = TagResolver.resolver(
                Placeholder.component("player", Component.text(targetName)),
                Placeholder.component("playtime", Component.text(playTimeString))
        );
        sender.sendMessage(plugin.config().getMessage("playtime-others", resolver));
        return true;
    }

    private long getPlayerPlaytimeSeconds(OfflinePlayer player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
    }
}
