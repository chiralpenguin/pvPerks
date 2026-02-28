package com.purityvanilla.pvperks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvperks.PVPerks;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatItemCommand {
    private static final String PERM = "pvperks.chatitem";

    private final PVPerks plugin;

    public ChatItemCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("chatitem")
                .requires(source ->
                        source.getExecutor() instanceof Player &&
                        source.getSender().hasPermission(PERM))
                .executes(this::executeChatItem)
                .build();
    }

    public int executeChatItem(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isEmpty()) {
            player.sendMessage(plugin.config().getMessage("chatitem-empty"));
            return Command.SINGLE_SUCCESS;
        }

        Component itemName = item.displayName()
                .hoverEvent(item.asHoverEvent())
                .color(NamedTextColor.LIGHT_PURPLE);

        Component message = plugin.config().getMessage("chatitem-message", TagResolver.resolver(
                Placeholder.component("player", player.displayName()),
                Placeholder.component("item", itemName)
        ));

        for (Player target : plugin.getServer().getOnlinePlayers()) {
            if (!PVCore.getAPI().getPlayerAPI().isPlayerIgnored(target, player)) target.sendMessage(message);
        }
        return Command.SINGLE_SUCCESS;
    }
}
