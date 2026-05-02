package me.d2glaidee.vexortroll.listener;

import me.d2glaidee.vexortroll.VexorTroll;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final VexorTroll plugin;

    public PlayerQuitListener(VexorTroll plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.getTrollManager().stopAllFor(e.getPlayer());
    }
}
