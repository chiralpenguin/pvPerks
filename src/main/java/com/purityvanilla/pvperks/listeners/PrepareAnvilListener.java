package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import com.purityvanilla.pvperks.PVPerks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
        if (event.getResult() == null || event.getInventory().getRenameText() == null || event.getInventory().getRenameText().isEmpty()) {
            return;
        }

        // Handle rename operations
        if (event.getView().getPlayer() instanceof Player player) {
            String renameString = event.getInventory().getRenameText();
            if (!renameString.contains("&") && !renameString.contains("#")) {
                return;
            }

            ItemStack result = event.getResult();
            ItemMeta meta = result.getItemMeta();
            if (meta == null) {
                return;
            }

            Component itemNameComponent = PVCore.getAPI().parsePlayerFormatString(renameString, player, FormatCodeParser.Context.ANVIL);
            meta.displayName(itemNameComponent.decoration(TextDecoration.ITALIC, false));
            result.setItemMeta(meta);
            event.setResult(result);
        }
    }
}
