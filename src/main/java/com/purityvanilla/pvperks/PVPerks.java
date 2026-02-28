package com.purityvanilla.pvperks;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.database.DataService;
import com.purityvanilla.pvlib.tasks.CacheCleanTask;
import com.purityvanilla.pvlib.tasks.SaveDataTask;
import com.purityvanilla.pvperks.commands.*;
import com.purityvanilla.pvperks.database.BadgeDataService;
import com.purityvanilla.pvperks.listeners.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PVPerks extends JavaPlugin {
    private Config config;

    private HashMap<String, DataService> dataServices;
    private Set<UUID> afkPlayers;

    @Override
    public void onEnable() {
        config = new Config();

        dataServices = new HashMap<>();
        dataServices.put("badge", new BadgeDataService(this, PVCore.getAPI().getDatabase()));

        afkPlayers = new HashSet<>();

        registerCommands();
        registerBrigadierCommands();
        registerListeners();
        scheduleTasks();
    }

    @Override
    public void onDisable() {
        for (DataService service : dataServices.values()) {
            service.saveAll();
        }

        getLogger().info("Plugin disabled");
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

    public boolean playerIsAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public void setPlayerAfk(Player player) {
        afkPlayers.add(player.getUniqueId());
    }

    public void unsetPlayerAfk(Player player) {
        afkPlayers.remove(player.getUniqueId());
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

            commands.register(new AfkCommand(this).buildCommand(), "Set AFK status", List.of());
            commands.register(new BadgeCommand(this).buildCommand(), "Manage badges", List.of());
            commands.register(new ChatItemCommand(this).buildCommand(), "Display held item in chat", List.of());
        });
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new PreLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
    }

    private void scheduleTasks() {
        getServer().getGlobalRegionScheduler().cancelTasks(this);

        // Run saveData every 5 minutes after 1 minute
        SaveDataTask saveDataTask = new SaveDataTask(dataServices);
        getServer().getGlobalRegionScheduler().runAtFixedRate(
                this, task -> saveDataTask.run(),1200L, 6000L);

        // Run cacheClean every 10 minutes after 2 minute
        CacheCleanTask cacheCleanTask = new CacheCleanTask(dataServices);
        getServer().getGlobalRegionScheduler().runAtFixedRate(
                this, task -> cacheCleanTask.run(),2400L, 12000L);
    }
}
