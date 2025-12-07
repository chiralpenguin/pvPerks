package com.purityvanilla.pvperks.player;

import com.purityvanilla.pvcore.PVCore;

import java.util.UUID;

public class BadgeMetaHelper {
    public void setPrefix(UUID uuid, int priority, String prefix) {
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
}
