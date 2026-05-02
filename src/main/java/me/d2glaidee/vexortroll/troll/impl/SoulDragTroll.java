package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SoulDragTroll extends AbstractTroll {

    private final Map<UUID, Location> anchors = new ConcurrentHashMap<>();

    public SoulDragTroll(VexorTroll plugin) {
        super(plugin, "soul_drag");
    }

    @Override
    protected void onStart(Player target) {
        double strength = cfgDouble("strength", 0.15);
        int interval = cfgInt("interval", 5);
        UUID uid = target.getUniqueId();

        Location anchor = randomNear(target.getLocation(), 20);
        anchors.put(uid, anchor);

        randomLoop(target, () -> {
            if (rng().nextInt(100) < 5) {
                anchors.put(uid, randomNear(target.getLocation(), 20));
            }
        }, 100, 300);

        repeat(target, () -> {
            Location anch = anchors.get(uid);
            if (anch == null) return;

            Vector dir = anch.toVector().subtract(target.getLocation().toVector()).normalize();
            dir.multiply(strength);
            dir.setY(Math.min(dir.getY(), 0.05));

            target.setVelocity(target.getVelocity().add(dir));
            if (rng().nextInt(4) == 0) {
                target.spawnParticle(Particle.SOUL, target.getLocation(), 2, 0.3, 0.1, 0.3, 0);
            }
        }, interval);
    }

    @Override
    protected void onStop(Player target) {
        anchors.remove(target.getUniqueId());
    }
}
