package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class InvertedControlsTroll extends AbstractTroll implements Listener {

    public InvertedControlsTroll(VexorTroll plugin) {
        super(plugin, "inverted_controls");
    }

    @Override
    protected void onStart(Player target) {}

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!isRunning(p.getUniqueId())) return;

        Location from = e.getFrom();
        Location to = e.getTo();

        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        if (dx == 0 && dz == 0) return;

        Location inv = from.clone();
        inv.setX(from.getX() - dx);
        inv.setZ(from.getZ() - dz);
        inv.setY(to.getY());
        inv.setYaw(to.getYaw());
        inv.setPitch(to.getPitch());
        e.setTo(inv);
    }
}
