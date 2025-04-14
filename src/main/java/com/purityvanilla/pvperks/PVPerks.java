package com.purityvanilla.pvperks;

import com.purityvanilla.pvperks.commands.JoinDateCommand;
import com.purityvanilla.pvperks.commands.PlaytimeCommand;
import com.purityvanilla.pvperks.commands.ReloadCommand;
import com.purityvanilla.pvperks.commands.SeenCommand;
import com.purityvanilla.pvperks.listeners.PlayerDeathListener;
import com.purityvanilla.pvperks.listeners.PrepareAnvilListener;
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
        getCommand("joindate").setExecutor(new JoinDateCommand(this));
        getCommand("playtime").setExecutor(new PlaytimeCommand(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
    }
}
