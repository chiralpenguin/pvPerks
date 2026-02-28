package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.commands.AfkCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final PVPerks plugin;

    public PlayerMoveListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.playerIsAfk(player)) return;

        AfkCommand.removePlayerAfk(plugin, player);
    }
}
