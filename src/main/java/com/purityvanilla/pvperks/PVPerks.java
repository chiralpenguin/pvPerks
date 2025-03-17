package com.purityvanilla.pvperks;

import com.purityvanilla.pvperks.commands.ReloadCommand;
import com.purityvanilla.pvperks.listeners.SignChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPerks extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        config = new Config();

        registerCommands();
        registerListeners();
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

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
    }
}
