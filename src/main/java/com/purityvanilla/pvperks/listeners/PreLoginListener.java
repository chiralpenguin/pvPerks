package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.player.Badge;
import com.purityvanilla.pvperks.player.PlayerBadgeData;
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
        PlayerBadgeData playerBadgeData = plugin.getBadgeData().getPlayerBadgeData(uuid);
        String activeBadge = playerBadgeData.getActiveBadge();
        if (!activeBadge.isEmpty()) {
            if (playerBadgeData.getBadges().contains(activeBadge)) {
                Badge badge = plugin.getBadgeData().getBadge(activeBadge);
                playerBadgeData.updatePlayerBadge(badge, plugin.config().getBadgeSuffixWeight());
            } else {
                playerBadgeData.clearPlayerBadge(plugin.config().getBadgeSuffixWeight());
            }
        }

        String activeIcon = playerBadgeData.getActiveIcon();
        if (!activeIcon.isEmpty()) {
            if (playerBadgeData.getBadges().contains(activeIcon)) {
                Badge icon = plugin.getBadgeData().getBadge(activeIcon);
                playerBadgeData.updatePlayerIcon(icon, plugin.config().getIconPrefixWeight());
            } else {
                playerBadgeData.clearPlayerIcon(plugin.config().getIconPrefixWeight());
            }
        }
    }
}
