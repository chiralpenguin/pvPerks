package com.purityvanilla.pvperks;

import com.purityvanilla.pvperks.commands.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPerks extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        config = new Config();

        registerCommands();
    }

    public Config  config() {
        return config;
    }

    public void reload() {
        config = new Config();
    }

    private void registerCommands() {
        getCommand("reload").setExecutor(new ReloadCommand(this));
    }
}
