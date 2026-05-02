package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ParanoiaTroll extends AbstractTroll {

    private static final Sound[] SOUNDS = {
            Sound.BLOCK_WOODEN_DOOR_OPEN,
            Sound.BLOCK_WOODEN_DOOR_CLOSE,
            Sound.BLOCK_CHEST_OPEN,
            Sound.BLOCK_CHEST_CLOSE,
            Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
            Sound.BLOCK_GRAVEL_STEP,
            Sound.BLOCK_STONE_STEP,
            Sound.BLOCK_WOOD_STEP,
            Sound.BLOCK_GLASS_BREAK,
            Sound.ENTITY_ITEM_PICKUP,
            Sound.BLOCK_STONE_BREAK,
            Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
            Sound.AMBIENT_CAVE
    };

    public ParanoiaTroll(VexorTroll plugin) {
        super(plugin, "paranoia");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 25);
        int max = cfgInt("max-interval", 70);

        randomLoop(target, () -> {
            int type = rng().nextInt(3);
            switch (type) {
                case 0 -> {
                    Sound s = SOUNDS[rng().nextInt(SOUNDS.length)];
                    Location loc = behind(target, rng().nextDouble(2, 6));
                    target.playSound(loc, s, 0.5f, rng().nextFloat(0.8f, 1.2f));
                }
                case 1 -> {
                    Location loc = randomNear3D(target.getLocation(), 6);
                    target.playSound(loc, Sound.BLOCK_GRAVEL_STEP, 0.7f, 0.7f);
                    later(target, () -> {
                        Location loc2 = behind(target, rng().nextDouble(1, 3));
                        target.playSound(loc2, Sound.BLOCK_GRAVEL_STEP, 0.8f, 0.7f);
                    }, rng().nextInt(5, 15));
                }
                case 2 -> {
                    target.playSound(behind(target, 1.5), Sound.ENTITY_PLAYER_BREATH, 0.4f, 0.6f);
                }
            }
        }, min, max);
    }
}
