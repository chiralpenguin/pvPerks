package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvperks.PVPerks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PreLoginListener implements Listener {
    private final PVPerks plugin;

    public PreLoginListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        UUID uuid = event.getUniqueId();
        plugin.getBadgeData().getPlayerBadgeData(uuid);
    }
}
