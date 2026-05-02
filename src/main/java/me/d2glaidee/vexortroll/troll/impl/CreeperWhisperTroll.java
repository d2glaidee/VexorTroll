package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CreeperWhisperTroll extends AbstractTroll {

    public CreeperWhisperTroll(VexorTroll plugin) {
        super(plugin, "creeper_whisper");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 40);
        int max = cfgInt("max-interval", 100);

        randomLoop(target, () -> {
            int type = rng().nextInt(4);
            Location src = behind(target, rng().nextDouble(2, 5));

            switch (type) {
                case 0 -> target.playSound(src, Sound.ENTITY_CREEPER_PRIMED, 0.8f, 1.0f);
                case 1 -> {
                    target.playSound(src, Sound.ENTITY_CREEPER_PRIMED, 0.6f, 1.0f);
                    later(target, () -> {
                        Location near = randomNear(target.getLocation(), 3);
                        target.playSound(near, Sound.ENTITY_GENERIC_EXPLODE, 0.4f, 1.0f);
                    }, rng().nextInt(20, 35));
                }
                case 2 -> {
                    for (int i = 0; i < 3; i++) {
                        later(target, () -> {
                            Location s = randomNear3D(target.getLocation(), 5);
                            target.playSound(s, Sound.ENTITY_CREEPER_PRIMED, 0.3f, rng().nextFloat(0.8f, 1.3f));
                        }, i * 15L);
                    }
                }
                case 3 -> target.playSound(src, Sound.ENTITY_TNT_PRIMED, 0.5f, 0.7f);
            }
        }, min, max);
    }
}
