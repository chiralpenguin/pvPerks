package com.purityvanilla.pvperks.player;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvperks.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerBeheading {
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("pvperks\\.playerbeheading\\.beheadchance\\.(\\d+)");
    private static final String PERMISSION_BASE = "pvperks.playerbeheading.beheadchance.";

    public static double getPlayerBeheadingProbability(Player player) {
        int maxProbability = 0;

        Set<String> playerPermissions = PVCore.getAPI().getPlayerAPI().getPlayerPermissions(player);
        for (String permission : playerPermissions) {
            if (!permission.startsWith(PERMISSION_BASE)) {
                continue;
            }

            Matcher matcher = PERMISSION_PATTERN.matcher(permission);
            if (matcher.matches()) {
                int probability = Integer.parseInt(matcher.group(1));
                maxProbability = Math.max(maxProbability, Math.min(probability, 100));
            }
        }

        return maxProbability / 100d;
    }

    public static ItemStack getPlayerHead(Player player, Config config) {
       ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
       SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

       skullMeta.setPlayerProfile(player.getPlayerProfile());
       skullMeta.displayName(config.getMessage("player-head-name", CustomTagResolvers.playerResolver(player.getName())));
       skullMeta.lore(List.of(config.getMessage("player-head-lore")));

        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }
}
