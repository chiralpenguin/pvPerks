package com.purityvanilla.pvperks.player;

import java.util.Set;

public class BadgeData {
    private final Set<String> availableBadges;
    private String activeBadge;
    private String activeIcon;

    public BadgeData(Set<String> availableBadges, String activeBadge, String activeIcon) {
        this.availableBadges = availableBadges;
        this.activeBadge = activeBadge;
        this.activeIcon = activeBadge;
    }

    public BadgeData(Set<String> availableBadges) {
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
}