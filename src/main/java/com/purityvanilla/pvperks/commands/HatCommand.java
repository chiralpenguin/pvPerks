package com.purityvanilla.pvperks.commands;

import java.util.HashSet;
import java.util.Set;

import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvperks.PVPerks;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

public class HatCommand implements CommandExecutor {
    private final PVPerks plugin;

    private final Set<Material> restrictedMaterials = new HashSet<>(Set.of(
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.BUNDLE
    ));

    public HatCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;

        Player player = (Player) sender;

        // Schedule the task in the player's region scheduler
        player.getScheduler().run(plugin, (ScheduledTask task) -> {
            executeHat(player);
            }, () -> {}
        );

        return true;
    }

    private void executeHat(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack handItem = inventory.getItemInMainHand();

        // Check if player is holding anything
        if (handItem.getType() == Material.AIR) {
            player.sendMessage(plugin.config().getMessage("hat-usage"));
            return;
        }

        // Check if the item is restricted
        if (restrictedMaterials.contains(handItem.getType())) {
            player.sendMessage(plugin.config().getMessage("hat-item-restricted"));
            return;
        }

        ItemStack currentHelmet = inventory.getHelmet();
        ItemStack itemToWear = handItem.clone();
        itemToWear.setAmount(1); // Only move one item to the head
        inventory.setHelmet(itemToWear);

        // Reduce the number of items in hand by 1
        if (handItem.getAmount() > 1) {
            handItem.setAmount(handItem.getAmount() - 1);
            inventory.setItemInMainHand(handItem);
        } else {
            inventory.setItemInMainHand(null);
        }

        // If there was a helmet, put it in the hand
        if (currentHelmet != null && currentHelmet.getType() != Material.AIR) {
            // If the hand is now empty, put the helmet there. Otherwise, put it in inventory or drop it
            inventory.getItemInMainHand();
            if (inventory.getItemInMainHand().getType() == Material.AIR) {
                inventory.setItemInMainHand(currentHelmet);
            } else {
                if (inventory.firstEmpty() == -1) {
                    // Inventory is full, drop the item
                    player.getWorld().dropItem(player.getLocation(), currentHelmet);
                } else {
                    inventory.addItem(currentHelmet);
                }
            }
        }
    }
}
