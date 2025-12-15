package com.purityvanilla.pvperks.player;

import com.purityvanilla.pvperks.Config;
import com.purityvanilla.pvperks.database.BadgeDataService;
import com.purityvanilla.pvperks.util.CustomTagResolvers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Set;
import java.util.UUID;

public class PlayerBadgeData {
    private final UUID playerID;
    private final Set<String> availableBadges;
    private String activeBadge;
    private String activeIcon;

    public PlayerBadgeData(UUID playerID, Set<String> availableBadges, String activeBadge, String activeIcon) {
        this.playerID = playerID;
        this.availableBadges = availableBadges;
        this.activeBadge = activeBadge;
        this.activeIcon = activeBadge;
    }

    public PlayerBadgeData(UUID playerID, Set<String> availableBadges) {
        this.playerID = playerID;
        this.availableBadges = availableBadges;
        this.activeBadge = "";
        this.activeIcon = "";
    }

    public Set<String> getBadges() {
        return availableBadges;
    }

    public void addBadge(String name) {
        this.availableBadges.add(name.toLowerCase());
    }

    public void removeBadge(String name) {
        this.availableBadges.remove(name.toLowerCase());
    }

    public String getActiveBadge() {
        return activeBadge;
    }

    public void setActiveBadge(String activeBadge) {
        this.activeBadge = activeBadge;
    }

    public String getActiveIcon() {
        return activeIcon;
    }

    public void setActiveIcon(String activeIcon) {
        this.activeIcon = activeIcon;
    }

    public void updatePlayerBadge(Badge badge, int priority) {
        setActiveBadge(badge.getName());
        BadgeMetaHelper.updateSuffix(playerID, priority, badge.getText());
    }

    public void clearPlayerBadge(int priority) {
        setActiveBadge("");
        BadgeMetaHelper.removeSuffix(playerID, priority);
    }

    public void updatePlayerIcon(Badge badge, int priority) {
        setActiveIcon(badge.getName());
        BadgeMetaHelper.updatePrefix(playerID, priority, badge.getText());
    }

    public void clearPlayerIcon(int priority) {
        setActiveIcon("");
        BadgeMetaHelper.removePrefix(playerID, priority);
    }

    public Component getBadgeListMessage(BadgeDataService badgeData, Config config) {
        TextComponent.Builder message = Component.text();
        for (String badgeName : availableBadges) {
            Badge badge = badgeData.getBadge(badgeName);
            if (badge == null) continue;
            // TODO Generalise to accept both MiniMessage and Legacy format codes as LuckPerms supports both
            Component badgeComponent = LegacyComponentSerializer.legacyAmpersand()
                    .deserialize(badge.getText())
                    .hoverEvent(HoverEvent.showText(config.getMessage("badge-list-hover",
                            CustomTagResolvers.badgeResolver(badgeName))))
                    .clickEvent(ClickEvent.runCommand("/badge set " + badgeName));

            message.append(badgeComponent);
            message.append(Component.text("  "));
        }

        return message.build();
    }
}