package com.purityvanilla.pvperks.listeners;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import com.purityvanilla.pvperks.PVPerks;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private final PVPerks plugin;

    public SignChangeListener(PVPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Component[] lines = event.lines().toArray(new Component[0]);

        for (int i = 0; i < lines.length; i++) {
            event.line(i, PVCore.getAPI().parsePlayerComponent(lines[i], player, FormatCodeParser.Context.SIGN));
        }
    }
}
