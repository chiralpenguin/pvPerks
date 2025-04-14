package com.purityvanilla.pvperks;

import com.purityvanilla.pvlib.config.ConfigFile;
import com.purityvanilla.pvlib.config.Messages;

public class Config extends ConfigFile {
    private final float defaultBeheadingChance;
    private final boolean verbose;

    public Config() {
        super("plugins/pvPerks/config.yml");
        messages = new Messages(this, "plugins/pvPerks/messages.json");

        defaultBeheadingChance = configRoot.node("default_beheading_chance").getFloat();
        verbose = configRoot.node("verbose").getBoolean();
    }

    public float getDefaultBeheadingChance() {
        return this.defaultBeheadingChance;
    }

    public boolean verbose() {
        return this.verbose;
    }
}
