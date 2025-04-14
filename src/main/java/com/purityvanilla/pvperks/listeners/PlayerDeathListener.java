package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.player.PlayerBeheading;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {
    private final PVPerks plugin;
    private final String PERMISSION_CAN_BEHEAD = "pvperks.playerbeheading.canbehead";
    private final String PERMISSION_CAN_LOSE_HEAD = "pvperks.playerbeheading.canlosehead";
    private final String PERMISSION_ALWAYS_BEHEAD = "pvperks.playerbeheading.alwaysbehead";

    public PlayerDeathListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = player.getKiller();

        // Check killer for permissions
        if (killer == null || !killer.hasPermission(PERMISSION_CAN_BEHEAD)) {
            return;
        }

        // Check if player can be beheaded and if killer has sufficient permission to bypass
        if (!player.hasPermission(PERMISSION_CAN_LOSE_HEAD) && !killer.hasPermission(PERMISSION_ALWAYS_BEHEAD)) {
            return;
        }

        // Use maximum beheading probability assigned as permission, default if no permissions are assigned
        double beheadingProbability = PlayerBeheading.getPlayerBeheadingProbability(killer);
        if (beheadingProbability == 0) {
            beheadingProbability = plugin.config().getDefaultBeheadingChance();
        }

        // Override probability if killer has bypass permission
        if (killer.hasPermission(PERMISSION_ALWAYS_BEHEAD)) {
            beheadingProbability = 1d;
        }

        // Roll for beheading, returning if not met
        if (Math.random() > beheadingProbability) {
            return;
        }

        ItemStack playerHead = PlayerBeheading.getPlayerHead(player, plugin.config());
        Location deathLocation = player.getLocation();
        deathLocation.getWorld().dropItemNaturally(deathLocation, playerHead);
    }
}