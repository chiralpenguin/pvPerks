package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvperks.PVPerks;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final PVPerks plugin;

    public PlayerJoinListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String removeGroupPerm = "group." + plugin.config().getAfkGroupName();

        PVCore.getAPI().getLuckPerms().getUserManager().modifyUser(player.getUniqueId(), user -> {
            boolean hasGroup = user.data().toCollection().stream()
                    .anyMatch(node -> node.getKey().equals(removeGroupPerm));

            if (hasGroup) {
                user.data().remove(Node.builder(removeGroupPerm).build());
            }
        });
    }
}
