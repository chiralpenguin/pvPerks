package com.purityvanilla.pvperks.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.TimeFormatting;
import com.purityvanilla.pvperks.PVPerks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public class JoinDateCommand implements CommandExecutor {
    private final PVPerks plugin;

    public JoinDateCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        OfflinePlayer target;

        // Assume player is checking their own joindate if no arguments given
        if (args.length < 1) {
            if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("joindate-usage"))) return true;
            target = (Player) sender;
            Timestamp firstJoined = new Timestamp(target.getFirstPlayed());
            Component dateComponent = Component.text(TimeFormatting.basicDateString(firstJoined));

            TagResolver resolver = TagResolver.resolver(Placeholder.component("date", dateComponent));
            sender.sendMessage(plugin.config().getMessage("joindate-self", resolver));
            return true;
        }

        UUID targetUUID = PVCore.getAPI().getPlayerAPI().getUUIDFromName(args[0].toLowerCase());
        if (targetUUID == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        target = plugin.getServer().getOfflinePlayer(targetUUID);
        String targetName = PVCore.getAPI().getPlayerAPI().getPlayerUsername(targetUUID);
        Timestamp firstJoined = new Timestamp(target.getFirstPlayed());
        String dateString = TimeFormatting.basicDateString(firstJoined);
        sender.sendMessage(plugin.config().getMessage("joindate-others", CustomTagResolvers.playerDateResolver(targetName, dateString)));
        return true;
    }
}
