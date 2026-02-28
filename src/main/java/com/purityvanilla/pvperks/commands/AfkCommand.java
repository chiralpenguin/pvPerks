package com.purityvanilla.pvperks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvperks.Config;
import com.purityvanilla.pvperks.PVPerks;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;

public class AfkCommand {
    private final static String PERM = "pvperks.afk";

    private final PVPerks plugin;

    public AfkCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("afk")
                .requires(source ->
                        source.getExecutor() instanceof Player &&
                        source.getSender().hasPermission(PERM))
                .executes(this::executeAfk)
                .build();
    }

    public int executeAfk(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();

        if (plugin.playerIsAfk(player)) {
            removePlayerAfk(plugin, player);
            return Command.SINGLE_SUCCESS;
        }

        PVCore.getAPI().getLuckPerms().getUserManager().modifyUser(player.getUniqueId(), user -> {
            user.data().add(Node.builder("group." + plugin.config().getAfkGroupName()).build());
        });

        plugin.setPlayerAfk(player);
        player.sendMessage(plugin.config().getMessage("afk"));
        return Command.SINGLE_SUCCESS;
    }

    public static void removePlayerAfk(PVPerks plugin, Player player) {
        PVCore.getAPI().getLuckPerms().getUserManager().modifyUser(player.getUniqueId(), user -> {
            user.data().remove(Node.builder("group." + plugin.config().getAfkGroupName()).build());
        });

        plugin.unsetPlayerAfk(player);
        player.sendMessage(plugin.config().getMessage("afk-removed"));
    }
}
