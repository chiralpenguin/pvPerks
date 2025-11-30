package com.purityvanilla.pvperks.database;

import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.database.DatabaseOperator;
import com.purityvanilla.pvlib.database.ResultSetProcessor;
import com.purityvanilla.pvperks.player.Badge;
import com.purityvanilla.pvperks.player.BadgeData;

import java.util.*;

public class BadgeOperator extends DatabaseOperator {

    public BadgeOperator(DatabaseConnector database) {
        super(database);
    }

    @Override
    protected void createTables() {
        String query = """
            CREATE TABLE IF NOTE EXISTS badges (
                name VARCHAR(255) PRIMARY KEY,
                text VARCHAR(255) NOT NULL,
            )
            """;
        database.executeUpdate(query);

        query = """
            CREATE TABLE IF NOT EXISTS player_badges (
                player_uuid VARCHAR(36) NOT NULL,
                badge_name VARCHAR(255) NOT NULL,
                active_badge TINYINT(1) NOT NULL DEFAULT 0,
                active_icon TINYINT(1) NOT NULL DEFAULT 0,
                PRIMARY KEY (player_uuid, badge_name),
                CONSTRAINT fk_player_uuid FOREIGN KEY (player_uuid) REFERENCES players (uuid) ON DELETE CASCADE,
                CONSTRAINT fk_badge_name FOREIGN KEY (badge_name) REFERENCES badges (name) ON DELETE CASCADE
           """;
        database.executeUpdate(query);
    }

    public Set<Badge> getBadges() {
        String query = "SELECT name, text FROM badges";
        ResultSetProcessor<Set<Badge>> badgeProcessor = rs -> {
           Set<Badge> badges = new HashSet<>();
           while (rs.next()) {
               badges.add(new Badge(rs.getString("name"), rs.getString("text")));
           }
           return badges;
        };

        return database.executeQuery(query, badgeProcessor);
    }

    public int saveBadge(String name, String text) {
        String query = """
            INSERT INTO badges (name, text) VALUES (?, ?)
            ON DUPLICATE KEY UPDATE text = VALUES(text)
            """;
        List<Object> params = new ArrayList<>();
        params.add(name);
        params.add(text);
        return database.executeUpdate(query, params);
    }

    public int saveBadge(Badge badge) {
        return saveBadge(badge.getName(), badge.getText());
    }

    public int deleteBadge(String name) {
        String query = "DELETE FROM badges WHERE name = ?";
        List<Object> params = new ArrayList<>();
        params.add(name);
        return database.executeUpdate(query, params);
    }

    public BadgeData getPlayerBadgeData(UUID playerID) {
        String query = "SELECT badge_name, active_badge, active_icon FROM player_badges WHERE player_UUID = ?";
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        ResultSetProcessor<BadgeData> badgeDataProcessor = rs -> {
            Set<String> availableBadges = new HashSet<>();
            String activeBadge = "";
            String activeIcon = "";

            while (rs.next()) {
                String name = rs.getString("badge_name");
                availableBadges.add(name);

                if (rs.getBoolean("active_badge")) activeBadge = name;
                if (rs.getBoolean("active_icon")) activeIcon = name;
            }

            return new BadgeData(availableBadges, activeBadge, activeIcon);
        };

        return database.executeQuery(query, params, badgeDataProcessor);
    }

    public void writePlayerBadgeData(UUID playerID, BadgeData data) {

    }
}
