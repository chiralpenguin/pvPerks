package com.purityvanilla.pvperks.player;

import java.util.UUID;

public class Badge {
    private final String name;
    private String text;

    public Badge(String name, String text) {
        this.name = name.toLowerCase();
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setActiveBadge(UUID playerID, int suffixPriority, BadgeData playerBadgeData) {
        BadgeMetaHelper.setSuffix(playerID, suffixPriority, getText());
        playerBadgeData.setActiveBadge(getName());
    }
}
