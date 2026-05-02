package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NeckSnapTroll extends AbstractTroll {

    public NeckSnapTroll(VexorTroll plugin) {
        super(plugin, "neck_snap");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 40);
        int max = cfgInt("max-interval", 120);

        randomLoop(target, () -> {
            Location loc = target.getLocation();
            int type = rng().nextInt(4);

            switch (type) {
                case 0 -> {
                    loc.setYaw(loc.getYaw() + 180);
                    target.teleport(loc);
                    soundNear(target, Sound.ENTITY_PHANTOM_AMBIENT, 2, 0.2f, 0.4f);
                }
                case 1 -> {
                    loc.setYaw(loc.getYaw() + rng().nextFloat(-90, 90));
                    loc.setPitch(rng().nextFloat(-30, 60));
                    target.teleport(loc);
                }
                case 2 -> {
                    loc.setYaw(loc.getYaw() + 180);
                    loc.setPitch(90);
                    target.teleport(loc);
                    later(target, () -> {
                        Location back = target.getLocation();
                        back.setPitch(0);
                        target.teleport(back);
                    }, 5);
                }
                case 3 -> {
                    float originalYaw = loc.getYaw();
                    for (int i = 0; i < 6; i++) {
                        final int tick = i;
                        later(target, () -> {
                            Location l = target.getLocation();
                            l.setYaw(originalYaw + (tick % 2 == 0 ? 30 : -30));
                            target.teleport(l);
                        }, i * 2L);
                    }
                }
            }
        }, min, max);
    }
}
