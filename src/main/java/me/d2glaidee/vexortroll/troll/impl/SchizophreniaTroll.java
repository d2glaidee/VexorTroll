package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SchizophreniaTroll extends AbstractTroll {

    private final Sound[] SOUNDS = {
            Sound.AMBIENT_CAVE,
            Sound.ENTITY_ENDERMAN_STARE,
            Sound.ENTITY_GHAST_AMBIENT,
            Sound.ENTITY_VEX_AMBIENT,
            Sound.ENTITY_PHANTOM_AMBIENT,
            Sound.ENTITY_WARDEN_HEARTBEAT,
            Sound.BLOCK_SCULK_SENSOR_CLICKING,
            Sound.ENTITY_ELDER_GUARDIAN_CURSE,
            Sound.ENTITY_WITHER_AMBIENT,
            Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM,
            Sound.PARTICLE_SOUL_ESCAPE
    };

    private final Particle[] PARTICLES = {
            Particle.SOUL,
            Particle.SCULK_SOUL,
            Particle.SMOKE,
            Particle.ASH,
            Particle.CRIMSON_SPORE
    };

    public SchizophreniaTroll(VexorTroll plugin) {
        super(plugin, "schizophrenia");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 20);
        int max = cfgInt("max-interval", 80);

        randomLoop(target, () -> {
            int effect = rng().nextInt(5);
            switch (effect) {
                case 0, 1 -> {
                    Sound s = SOUNDS[rng().nextInt(SOUNDS.length)];
                    soundNear(target, s, 8, 0.6f, rng().nextFloat(0.5f, 1.5f));
                }
                case 2 -> {
                    Particle part = PARTICLES[rng().nextInt(PARTICLES.length)];
                    Location loc = randomNear3D(target.getLocation().add(0, 1, 0), 4);
                    target.spawnParticle(part, loc, rng().nextInt(5, 20), 0.5, 0.5, 0.5, 0);
                }
                case 3 -> applyEffect(target, PotionEffectType.DARKNESS, 30, 0, true);
                case 4 -> {
                    target.playSound(target.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.4f, 0.8f);
                    applyEffect(target, PotionEffectType.DARKNESS, 20, 0, true);
                }
            }
        }, min, max);
    }
}
