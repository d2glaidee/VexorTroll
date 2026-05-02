package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DesyncTroll extends AbstractTroll {

    private final Map<UUID, Queue<Location>> positions = new ConcurrentHashMap<>();

    public DesyncTroll(VexorTroll plugin) {
        super(plugin, "desync");
    }

    @Override
    protected void onStart(Player target) {
        UUID uid = target.getUniqueId();
        int min = cfgInt("min-interval", 20);
        int max = cfgInt("max-interval", 60);

        Queue<Location> queue = new ConcurrentLinkedDeque<>();
        positions.put(uid, queue);

        repeat(target, () -> {
            queue.add(target.getLocation().clone());
            while (queue.size() > 40) queue.poll();
        }, 1);

        randomLoop(target, () -> {
            if (queue.size() < 20) return;

            Object[] arr = queue.toArray();
            int idx = rng().nextInt(Math.max(1, arr.length - 30), arr.length);
            Location rollback = (Location) arr[Math.max(0, idx - rng().nextInt(10, 25))];

            target.teleport(rollback);
            target.playSound(target.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 0.15f, 1.5f);
        }, min, max);
    }

    @Override
    protected void onStop(Player target) {
        positions.remove(target.getUniqueId());
    }
}
