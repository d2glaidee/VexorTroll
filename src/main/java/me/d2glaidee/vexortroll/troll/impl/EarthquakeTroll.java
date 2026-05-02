package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EarthquakeTroll extends AbstractTroll {

    public EarthquakeTroll(VexorTroll plugin) {
        super(plugin, "earthquake");
    }

    @Override
    protected void onStart(Player target) {
        double intensity = cfgDouble("intensity", 0.08);
        int interval = cfgInt("interval", 2);

        repeat(target, () -> {
            Location loc = target.getLocation();
            double ox = (rng().nextDouble() - 0.5) * intensity * 2;
            double oz = (rng().nextDouble() - 0.5) * intensity * 2;

            Location shake = loc.clone().add(ox, 0, oz);
            shake.setYaw(loc.getYaw() + (float) ((rng().nextDouble() - 0.5) * 2));
            shake.setPitch(loc.getPitch() + (float) ((rng().nextDouble() - 0.5) * 1.5));
            target.teleport(shake);

            if (rng().nextInt(10) == 0) {
                soundNear(target, Sound.BLOCK_STONE_BREAK, 6, 0.5f, 0.6f);
            }
            if (rng().nextInt(20) == 0) {
                target.playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.3f, 0.5f);
            }
        }, interval);
    }
}
