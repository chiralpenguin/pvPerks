package com.purityvanilla.pvperks;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.database.DataService;
import com.purityvanilla.pvperks.commands.*;
import com.purityvanilla.pvperks.database.BadgeDataService;
import com.purityvanilla.pvperks.listeners.PlayerDeathListener;
import com.purityvanilla.pvperks.listeners.PrepareAnvilListener;
import com.purityvanilla.pvperks.listeners.SignChangeListener;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class PVPerks extends JavaPlugin {
    private Config config;

    private HashMap<String, DataService> dataServices;

    @Override
    public void onEnable() {
        config = new Config();

        dataServices = new HashMap<>();
        dataServices.put("badge", new BadgeDataService(this, PVCore.getAPI().getDatabase()));

        registerCommands();
        registerBrigadierCommands();
        registerListeners();
    }

    public Config config() {
        return config;
    }

    public void reload() {
        config = new Config();
    }

    public BadgeDataService getBadgeData() {
        return (BadgeDataService) dataServices.get("badge");
    }

    private void registerCommands() {
        getCommand("hat").setExecutor(new HatCommand(this));
        getCommand("joindate").setExecutor(new JoinDateCommand(this));
        getCommand("playtime").setExecutor(new PlaytimeCommand(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
    }

    private void registerBrigadierCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();

            commands.register(new BadgeCommand(this).buildCommand(), "Manage badges", List.of());
        });
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
    }
}
