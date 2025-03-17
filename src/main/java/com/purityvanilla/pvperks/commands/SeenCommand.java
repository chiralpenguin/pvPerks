package com.purityvanilla.pvperks.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.TimeFormatting;
import com.purityvanilla.pvperks.PVPerks;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class SeenCommand implements CommandExecutor {
    private final PVPerks plugin;

    public SeenCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("player-seen-usage"))) return true;

        // Make sure to use this method when getting player by username as blocking web request will be made with getOfflinePlayer()
        OfflinePlayer player = plugin.getServer().getOfflinePlayerIfCached(args[0].toLowerCase());
        if (player == null || player.getName() == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        Timestamp lastSeen = PVCore.getAPI().getPlayerAPI().getPlayerLastSeen(player.getUniqueId());
        String dateString = TimeFormatting.basicDateString(lastSeen);
        sender.sendMessage(plugin.config().getMessage("player-seen", CustomTagResolvers.playerDateResolver(player.getName(), dateString)));
        return true;
    }
}