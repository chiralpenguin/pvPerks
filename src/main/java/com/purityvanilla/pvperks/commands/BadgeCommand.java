package com.purityvanilla.pvperks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.player.Badge;
import com.purityvanilla.pvperks.util.CustomTagResolvers;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

public class BadgeCommand {
    private static final String PERM_BASE = "pvperks.badge";
    private static final String PERM_LIST = PERM_BASE + ".list";
    private static final String PERM_SET = PERM_BASE + ".set";
    private static final String PERM_SET_ICON = PERM_BASE + ".seticon";
    private static final String PERM_MANAGE = PERM_BASE + ".manage";
    private static final String PERM_MANAGE_PLAYER = PERM_BASE + ".manageplayer";
    private static final String INVALID_NAME_REGEX = "[^\\w-]";

    private final PVPerks plugin;

    public BadgeCommand(PVPerks plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("badge")
            .requires(source -> source.getSender().hasPermission(PERM_BASE))
                .then(manageBadgesCommand())
                .build();
    }

    /*
    Badge management commands available to admins
     */
    private LiteralArgumentBuilder<CommandSourceStack> manageBadgesCommand() {
        return Commands.literal("manage")
                .requires(source -> source.getSender().hasPermission(PERM_MANAGE))
                .then(createBadgeCommand());
    }

    private LiteralArgumentBuilder<CommandSourceStack> createBadgeCommand() {
        return Commands.literal("create")
                .then(Commands.argument("badge", StringArgumentType.string()))
                    .then(Commands.argument("text", StringArgumentType.string()))
                        .executes(this::executeCreateBadge);
    }

    private int executeCreateBadge(CommandContext<CommandSourceStack> ctx) {
        String badgeName = ctx.getArgument("badge", String.class)
                .toLowerCase().
                replaceAll(INVALID_NAME_REGEX, "");

        Badge badge = new Badge(badgeName, ctx.getArgument("text", String.class));

        CommandSender sender = ctx.getSource().getSender();
        if (plugin.getBadgeData().getBadge(badgeName) == null) {
            sender.sendMessage(plugin.config().getMessage("badge-created",
                    CustomTagResolvers.badgeResolver(badgeName)));
        } else {
            sender.sendMessage(plugin.config().getMessage("badge-updated",
                    CustomTagResolvers.badgeResolver(badgeName)));
        }

        plugin.getBadgeData().saveBadge(badge);
        return Command.SINGLE_SUCCESS;
    }
}
