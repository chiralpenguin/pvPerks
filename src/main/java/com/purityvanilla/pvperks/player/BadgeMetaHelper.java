package com.purityvanilla.pvperks.player;

import com.purityvanilla.pvcore.PVCore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;

import java.util.UUID;

public class BadgeMetaHelper {
    public static void setPrefix(UUID uuid, int priority, String prefix) {
        LuckPerms luckPerms = PVCore.getAPI().getLuckPerms();
        PVCore.getAPI().getLuckPerms().getUserManager().loadUser(uuid).thenAccept(user -> {
            user.data().clear(node ->
                    node instanceof PrefixNode pn && pn.getPriority() == priority
            );

            PrefixNode node = PrefixNode.builder(prefix, priority).build();
            user.data().add(node);

            luckPerms.getUserManager().saveUser(user);
        });
    }

    public static void setSuffix(UUID uuid, int priority, String suffix) {
        LuckPerms luckPerms = PVCore.getAPI().getLuckPerms();
        PVCore.getAPI().getLuckPerms().getUserManager().loadUser(uuid).thenAccept(user -> {
            user.data().clear(node ->
                    node instanceof SuffixNode sn && sn.getPriority() == priority
            );

            SuffixNode node = SuffixNode.builder(suffix, priority).build();
            user.data().add(node);

            luckPerms.getUserManager().saveUser(user);
        });
    }
}
