package com.purityvanilla.pvperks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.purityvanilla.pvperks.PVPerks;
import com.purityvanilla.pvperks.player.Badge;
import com.purityvanilla.pvperks.player.BadgeData;
import com.purityvanilla.pvperks.util.CustomTagResolvers;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BadgeCommand {
    private static final String PERM_BASE = "pvperks.badge";
    private static final String PERM_LIST = PERM_BASE + ".list";
    private static final String PERM_SET = PERM_BASE + ".set";
    private static final String PERM_SET_ICON = PERM_BASE + ".seticon";
    private static final String PERM_CLEAR = PERM_BASE + ".clear";
    private static final String PERM_CLEAR_ICON = PERM_BASE + ".clearicon";
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
                .then(setBadgeCommand())
                .then(clearBadgeCommand())
                .then(manageBadgesCommand())
                .then(playerBadgesCommand())
                .build();
    }

    /*
    Badge selection commands for players
     */
    private LiteralArgumentBuilder<CommandSourceStack> setBadgeCommand() {
        return Commands.literal("set")
                .requires(source ->
                    source.getExecutor() instanceof Player &&
                    source.getSender().hasPermission(PERM_SET)
                )
                .then(Commands.argument("badge", StringArgumentType.string())
                        .executes(this::executeSetBadge)
                );
    }

    private int executeSetBadge(CommandContext<CommandSourceStack> ctx) {
        String badgeName = ctx.getArgument("badge", String.class).toLowerCase();
        Badge badge = plugin.getBadgeData().getBadge(badgeName);
        Player player = (Player) ctx.getSource().getSender();

        if (badge == null || !plugin.getBadgeData().playerHasBadge(player.getUniqueId(), badge)) {
           player.sendMessage(plugin.config().getMessage("badge-not-found",
                   CustomTagResolvers.badgeResolver(badgeName)
           ));
           return Command.SINGLE_SUCCESS;
        }

        plugin.getBadgeData().getPlayerBadgeData(player.getUniqueId()).updatePlayerBadge(
                badge, plugin.config().getBadgeSuffixWeight());

        player.sendMessage(plugin.config().getMessage("badge-set",
                CustomTagResolvers.badgeResolver(badgeName)
        ));

        return Command.SINGLE_SUCCESS;
    }

    private LiteralArgumentBuilder<CommandSourceStack> clearBadgeCommand() {
        return Commands.literal("clear")
                .requires(source ->
                    source.getExecutor() instanceof Player &&
                    source.getSender().hasPermission(PERM_CLEAR)
                ).executes(this::executeClearBadge);
    }

    private int executeClearBadge(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();
        plugin.getBadgeData().getPlayerBadgeData(player.getUniqueId()).clearPlayerBadge(
                plugin.config().getBadgeSuffixWeight());
        player.sendMessage(plugin.config().getMessage("badge-cleared"));
        return Command.SINGLE_SUCCESS;
    }

    /*
    Badge management commands available to admins
     */
    private LiteralArgumentBuilder<CommandSourceStack> manageBadgesCommand() {
        return Commands.literal("manage")
                .requires(source -> source.getSender().hasPermission(PERM_MANAGE))
                .then(createBadgeCommand()
                );
    }

    private LiteralArgumentBuilder<CommandSourceStack> createBadgeCommand() {
        return Commands.literal("create")
                .then(Commands.argument("badge", StringArgumentType.string())
                    .then(Commands.argument("text", StringArgumentType.string())
                        .executes(this::executeCreateBadge)
                ));
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

    /*
    Player badge management commands available to admins
     */
    private LiteralArgumentBuilder<CommandSourceStack> playerBadgesCommand() {
        return Commands.literal("player")
                .requires(source -> source.getSender().hasPermission(PERM_MANAGE_PLAYER))
                .then(addPlayerBadgeCommand())
                .then(removePlayerBadgeCommand());

    }

    private LiteralArgumentBuilder<CommandSourceStack> addPlayerBadgeCommand() {
        return Commands.literal("add")
                .then(Commands.argument("badge", StringArgumentType.string())
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(this::executeAddPlayerBadge)
                ));
    }

    private int executeAddPlayerBadge(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        OfflinePlayer target = Bukkit.getOfflinePlayer(ctx.getArgument("player", String.class));
        if (!target.hasPlayedBefore()) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return Command.SINGLE_SUCCESS;
        }
        UUID targetID = target.getUniqueId();

        String badgeName = ctx.getArgument("badge", String.class).toLowerCase();
        Badge badge = plugin.getBadgeData().getBadge(badgeName);
        if (badge == null) {
            sender.sendMessage(plugin.config().getMessage(
                    "badge-not-found", CustomTagResolvers.badgeResolver(badgeName)));
            return Command.SINGLE_SUCCESS;
        }

        BadgeData playerBadgeData = plugin.getBadgeData().getPlayerBadgeData(targetID);
        playerBadgeData.addBadge(badge.getName());
        sender.sendMessage(plugin.config().getMessage(
                "badge-added", CustomTagResolvers.playerBadgeResolver(target.getName(), badgeName)));
        return Command.SINGLE_SUCCESS;
    }

    private LiteralArgumentBuilder<CommandSourceStack> removePlayerBadgeCommand() {
        return Commands.literal("remove")
                .then(Commands.argument("badge", StringArgumentType.string())
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(this::executeRemovePlayerBadge)
                        ));
    }

    private int executeRemovePlayerBadge(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        OfflinePlayer target = Bukkit.getOfflinePlayer(ctx.getArgument("player", String.class));
        if (!target.hasPlayedBefore()) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return Command.SINGLE_SUCCESS;
        }
        UUID targetID = target.getUniqueId();

        String badgeName = ctx.getArgument("badge", String.class).toLowerCase();
        Badge badge = plugin.getBadgeData().getBadge(badgeName);
        if (badge == null || !plugin.getBadgeData().playerHasBadge(targetID, badge)) {
            sender.sendMessage(plugin.config().getMessage(
                    "badge-not-found", CustomTagResolvers.badgeResolver(badgeName)));
            return Command.SINGLE_SUCCESS;
        }

        BadgeData playerBadgeData = plugin.getBadgeData().getPlayerBadgeData(targetID);
        playerBadgeData.removeBadge(badgeName);
        if (playerBadgeData.getActiveBadge().equals(badgeName)) playerBadgeData.clearPlayerBadge(
                plugin.config().getBadgeSuffixWeight());

        sender.sendMessage(plugin.config().getMessage(
                "badge-removed", CustomTagResolvers.playerBadgeResolver(target.getName(), badgeName)));
        return Command.SINGLE_SUCCESS;
    }
}
