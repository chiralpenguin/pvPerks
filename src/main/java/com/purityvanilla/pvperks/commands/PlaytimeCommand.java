package com.purityvanilla.pvperks.commands;

import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.TimeFormatting;
import com.purityvanilla.pvperks.PVPerks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeCommand implements CommandExecutor {
    private final PVPerks plugin;

    public PlaytimeCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player target;

        // Assume player is checking own playtime if no argument given
        if (args.length < 1) {
            if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("playtime-usage"))) return true;
            target = (Player) sender;
            long playtimeSeconds = getPlayerPlaytimeSeconds(target);
            String playtimeString = TimeFormatting.PrettyDurationString(playtimeSeconds);

            TagResolver resolver = TagResolver.resolver(Placeholder.component("playtime", Component.text(playtimeString)));
            sender.sendMessage(plugin.config().getMessage("playtime-self", resolver));
            return true;
        }

        target = plugin.getServer().getPlayer(args[0].toLowerCase());
        if (target == null) {
            sender.sendMessage("player-not-found");
            return true;
        }

        long playTimeSeconds = getPlayerPlaytimeSeconds(target);
        String playTimeString = TimeFormatting.PrettyDurationString(playTimeSeconds);

        sender.sendMessage(plugin.config().getMessage("playtime-others",
                CustomTagResolvers.playerDateResolver(target.getName(), playTimeString)));
        return true;
    }

    private long getPlayerPlaytimeSeconds(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
    }
}
