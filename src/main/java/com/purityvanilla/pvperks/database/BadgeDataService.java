package com.purityvanilla.pvperks.database;

import com.purityvanilla.pvlib.database.DataService;
import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.util.CacheHelper;
import com.purityvanilla.pvperks.player.Badge;
import com.purityvanilla.pvperks.player.BadgeData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BadgeDataService extends DataService {
    private final BadgeOperator operator;
    private final ConcurrentHashMap<String, Badge> badgeCache;
    private final ConcurrentHashMap<UUID, BadgeData> playerBadgeCache;

    public BadgeDataService(JavaPlugin plugin, DatabaseConnector database) {
        super(plugin);
        operator = new BadgeOperator(database);

        badgeCache = new ConcurrentHashMap<>();
        for (Badge badge : operator.getBadges()) {
            badgeCache.put(badge.getName(), badge);
        }

        playerBadgeCache = new ConcurrentHashMap<>();
    }

    @Override
    public void saveAll() {
        for (Badge badge : badgeCache.values()) {
            operator.saveBadge(badge);
        }

        for (Map.Entry<UUID, BadgeData> entry : playerBadgeCache.entrySet()) {
            operator.savePlayerBadgeData(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void cleanCache() {
        for (UUID absentPlayer : CacheHelper.getAbsentUUIDs(playerBadgeCache.keySet(), this.plugin)) {
            unloadPlayerBadges(absentPlayer);
        }
    }

    /**
     * Get a collection of all defined badges from the cache
     * Badges are loaded from the database at server initialisation only
     * @return {@code Set<Badge>}
     */
    public Set<Badge> getAllBadges() {
        return new HashSet<>(badgeCache.values());
    }

    /**
     * Get a Badge object from its key (name)
     * @param badgeName Unique string identifier
     * @return {@code Badge} if it exists, {@code null} otherwise
     */
    public Badge getBadge(String badgeName) {
        return badgeCache.get(badgeName);
    }

    /**
     * Save a badge by adding/overwriting its entry in the cache and saving it to the database
     * @param badge {@code Badge} object
     */
    public void saveBadge(Badge badge) {
        badgeCache.put(badge.getName(), badge);
    }

    /**
     * Load player BadgeData into the cache from the database
     * @param playerID uuid of player to load {@code BadgeData} for
     */
    public void loadPlayerBadges(UUID playerID) {
        playerBadgeCache.put(playerID, operator.getPlayerBadgeData(playerID));
    }

    /**
     * Save player BadgeData to the database and remove it from the cache
     * @param playerID uuid of player to save {@code BadgeData} for
     */
    public void unloadPlayerBadges(UUID playerID) {
        operator.savePlayerBadgeData(playerID, playerBadgeCache.get(playerID));
        playerBadgeCache.remove(playerID);
    }

    /**
     * Get player BadgeData from the cache, loading it from the database first if necessary
     * @param playerID uuid of player to retrieve {@code BadgeData} for
     * @return {@code BadgeData} of player
     */
    public BadgeData getPlayerBadgeData(UUID playerID) {
        if (!playerBadgeCache.containsKey(playerID)) {
            loadPlayerBadges(playerID);
        }

        return playerBadgeCache.get(playerID);
    }
}
