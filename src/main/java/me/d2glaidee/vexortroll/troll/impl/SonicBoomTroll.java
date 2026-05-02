package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SonicBoomTroll extends AbstractTroll {

    public SonicBoomTroll(VexorTroll plugin) {
        super(plugin, "sonic_boom");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 60);
        int max = cfgInt("max-interval", 140);

        randomLoop(target, () -> {
            target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.7f, rng().nextFloat(0.7f, 1.3f));
            target.spawnParticle(Particle.SONIC_BOOM, target.getLocation().add(0, 1, 0), 1);

            double angle = rng().nextDouble() * Math.PI * 2;
            double force = 0.6 + rng().nextDouble() * 0.5;
            target.setVelocity(new Vector(Math.cos(angle) * force, 0.2, Math.sin(angle) * force));
        }, min, max);
    }
}
