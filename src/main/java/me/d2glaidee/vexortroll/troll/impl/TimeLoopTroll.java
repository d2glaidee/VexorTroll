package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimeLoopTroll extends AbstractTroll {

    private final Map<UUID, Queue<Location>> history = new ConcurrentHashMap<>();

    public TimeLoopTroll(VexorTroll plugin) {
        super(plugin, "time_loop");
    }

    @Override
    protected void onStart(Player target) {
        UUID uid = target.getUniqueId();
        int loopSec = cfgInt("loop-seconds", 5);
        int interval = cfgInt("interval", 100);
        int historySize = loopSec * 20;

        Queue<Location> queue = new ConcurrentLinkedQueue<>();
        history.put(uid, queue);

        repeat(target, () -> {
            queue.add(target.getLocation().clone());
            while (queue.size() > historySize) queue.poll();
        }, 1);

        repeat(target, () -> {
            Location past = queue.peek();
            if (past == null) return;
            target.spawnParticle(Particle.REVERSE_PORTAL, target.getLocation(), 30, 0.3, 0.5, 0.3, 0.05);
            target.teleport(past);
            target.playSound(past, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.3f, 1.5f);
            queue.clear();
        }, interval, interval);
    }

    @Override
    protected void onStop(Player target) {
        history.remove(target.getUniqueId());
    }
}
