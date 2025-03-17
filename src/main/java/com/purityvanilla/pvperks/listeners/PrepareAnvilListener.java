package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.FormatCodeParser;
import com.purityvanilla.pvperks.PVPerks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareAnvilListener implements Listener {
    private final PVPerks plugin;

    public PrepareAnvilListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null) {
            return;
        }

        // Handle rename operations
        if (event.getInventory().getRenameText() != null && !event.getInventory().getRenameText().isEmpty() &&
                event.getView().getPlayer() instanceof Player player) {
            ItemStack result = event.getResult();
            String renameString = event.getInventory().getRenameText();

            ItemMeta meta = result.getItemMeta();
            if (meta!= null) {
                meta.displayName(PVCore.getAPI().parsePlayerFormatString(renameString, player, FormatCodeParser.Context.ANVIL));
                result.setItemMeta(meta);
                event.setResult(result);
            }
        }
    }
}
