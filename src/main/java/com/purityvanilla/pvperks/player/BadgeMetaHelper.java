package com.purityvanilla.pvperks.player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;

import java.util.UUID;

public class BadgeMetaHelper {
    public static void updatePrefix(UUID uuid, int priority, String prefix) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
            PrefixNode node = PrefixNode.builder(prefix + " ", priority).build();

            // update will be run regularly so check before update
            if (user.data().contains(node, NodeEqualityPredicate.EXACT).asBoolean()) return;

            user.data().clear(n -> n instanceof PrefixNode pn && pn.getPriority() == priority);
            user.data().add(node);

            luckPerms.getUserManager().saveUser(user);
        });
    }

    public static void removePrefix(UUID uuid, int priority) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
            user.data().clear(node -> node instanceof PrefixNode pn && pn.getPriority() == priority);
            luckPerms.getUserManager().saveUser(user);
        });
    }

    public static void updateSuffix(UUID uuid, int priority, String suffix) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
            SuffixNode node = SuffixNode.builder(" " + suffix, priority).build();
            if (user.data().contains(node, NodeEqualityPredicate.EXACT).asBoolean()) return;

            user.data().clear(n -> n instanceof SuffixNode pn && pn.getPriority() == priority);
            user.data().add(node);

            luckPerms.getUserManager().saveUser(user);
        });
    }

    public static void removeSuffix(UUID uuid, int priority) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
            user.data().clear(node -> node instanceof SuffixNode sn && sn.getPriority() == priority);
            luckPerms.getUserManager().saveUser(user);
        });
    }
}
