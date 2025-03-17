package com.purityvanilla.pvperks;

import com.purityvanilla.pvlib.config.ConfigFile;
import com.purityvanilla.pvlib.config.Messages;

public class Config extends ConfigFile {
    private final boolean verbose;

    public Config() {
        super("plugins/pvPerks/config.yml");
        messages = new Messages(this, "plugins/pvPerks/messages.json");

        verbose = configRoot.node("verbose").getBoolean();
    }

    public boolean verbose() {
        return this.verbose;
    }
}
