package com.purityvanilla.pvperks.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.TimeFormatting;
import com.purityvanilla.pvperks.PVPerks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public class SeenCommand implements CommandExecutor {
    private final PVPerks plugin;

    public SeenCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("player-seen-usage"))) return true;

        String username = args[0].toLowerCase();
        UUID uuid = PVCore.getAPI().getPlayerAPI().getUUIDFromName(username);
        if (uuid == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        Timestamp lastSeen = PVCore.getAPI().getPlayerAPI().getPlayerLastSeen(uuid);
        String dateString = TimeFormatting.basicDateString(lastSeen);
        sender.sendMessage(plugin.config().getMessage("player-seen", CustomTagResolvers.playerDateResolver(username, dateString)));
        return true;
    }
}