package com.purityvanilla.pvperks;

import com.purityvanilla.pvlib.config.ConfigFile;
import com.purityvanilla.pvlib.config.Messages;

public class Config extends ConfigFile {
    private final float defaultBeheadingChance;
    private final int badgeSuffixWeight;
    private final int iconPrefixWeight;
    private final boolean verbose;

    public Config() {
        super("plugins/pvPerks/config.yml");
        messages = new Messages(this, "plugins/pvPerks/messages.json");

        defaultBeheadingChance = configRoot.node("default_beheading_chance").getFloat();
        badgeSuffixWeight = configRoot.node("badge_suffix_weight").getInt();
        iconPrefixWeight = configRoot.node("icon_prefix_weight").getInt();
        verbose = configRoot.node("verbose").getBoolean();
    }

    public float getDefaultBeheadingChance() {
        return defaultBeheadingChance;
    }

    public int getIconPrefixWeight() {
        return iconPrefixWeight;
    }

    public int getBadgeSuffixWeight() {
        return badgeSuffixWeight;
    }

    public boolean verbose() {
        return verbose;
    }
}
