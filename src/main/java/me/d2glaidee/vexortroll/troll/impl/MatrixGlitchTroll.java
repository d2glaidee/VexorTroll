package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MatrixGlitchTroll extends AbstractTroll {

    public MatrixGlitchTroll(VexorTroll plugin) {
        super(plugin, "matrix_glitch");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 10);
        int max = cfgInt("max-interval", 40);

        randomLoop(target, () -> {
            Location loc = target.getLocation();
            double dx = rng().nextDouble(-2, 2);
            double dz = rng().nextDouble(-2, 2);
            Location glitch = loc.clone().add(dx, 0, dz);
            glitch.setYaw(loc.getYaw());
            glitch.setPitch(loc.getPitch());

            if (!glitch.getBlock().isPassable()) return;

            target.spawnParticle(Particle.END_ROD, loc, 5, 0.3, 0.5, 0.3, 0);
            target.teleport(glitch);
            target.playSound(glitch, Sound.ENTITY_ENDERMAN_TELEPORT, 0.15f, 1.8f);
        }, min, max);
    }
}
