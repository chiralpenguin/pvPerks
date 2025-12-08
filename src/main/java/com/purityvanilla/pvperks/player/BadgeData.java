package com.purityvanilla.pvperks.player;

import java.util.Set;
import java.util.UUID;

public class BadgeData {
    private final UUID playerID;
    private final Set<String> availableBadges;
    private String activeBadge;
    private String activeIcon;

    public BadgeData(UUID playerID, Set<String> availableBadges, String activeBadge, String activeIcon) {
        this.playerID = playerID;
        this.availableBadges = availableBadges;
        this.activeBadge = activeBadge;
        this.activeIcon = activeBadge;
    }

    public BadgeData(UUID playerID, Set<String> availableBadges) {
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
}