package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PhantomTouchTroll extends AbstractTroll {

    public PhantomTouchTroll(VexorTroll plugin) {
        super(plugin, "phantom_touch");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 30);
        int max = cfgInt("max-interval", 90);

        randomLoop(target, () -> {
            double angle = rng().nextDouble() * Math.PI * 2;
            double force = rng().nextDouble(0.3, 0.7);
            double vy = rng().nextDouble(-0.1, 0.2);

            target.setVelocity(new Vector(
                    Math.cos(angle) * force, vy, Math.sin(angle) * force));

            target.playSound(target.getLocation(), Sound.ENTITY_BREEZE_INHALE, 0.3f, 0.5f);
        }, min, max);
    }
}
