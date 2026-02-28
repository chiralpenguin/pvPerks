package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.commands.AfkCommand;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {
    private final PVPerks plugin;

    public AsyncChatListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.playerIsAfk(player)) return;

        AfkCommand.removePlayerAfk(plugin, player);
    }
}
