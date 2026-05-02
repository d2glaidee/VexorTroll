package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NightTerrorTroll extends AbstractTroll {

    private final Map<UUID, AtomicInteger> phases = new ConcurrentHashMap<>();

    public NightTerrorTroll(VexorTroll plugin) {
        super(plugin, "night_terror");
    }

    @Override
    protected void onStart(Player target) {
        int interval = cfgInt("interval", 40);
        UUID uid = target.getUniqueId();
        phases.put(uid, new AtomicInteger(0));

        repeat(target, () -> {
            int phase = phases.get(uid).getAndIncrement();

            applyEffect(target, PotionEffectType.DARKNESS, interval + 10, 0, true);

            if (phase < 5) {
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 0.4f, 0.8f);
            } else if (phase < 10) {
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 0.6f, 1.0f);
                if (rng().nextBoolean()) {
                    soundNear(target, Sound.BLOCK_SCULK_SENSOR_CLICKING, 5, 0.4f, 0.7f);
                }
            } else if (phase < 15) {
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 0.8f, 1.2f);
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_AMBIENT, 0.5f, 0.5f);
                target.spawnParticle(Particle.SCULK_SOUL, target.getLocation(), 5, 2, 1, 2, 0);
            } else {
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_ROAR, 0.7f, 0.7f);
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1.0f, 1.4f);
                soundNear(target, Sound.ENTITY_WARDEN_NEARBY_CLOSER, 3, 0.6f, 0.8f);
                target.spawnParticle(Particle.SCULK_SOUL, target.getLocation(), 15, 3, 2, 3, 0.01);
                phases.get(uid).set(10);
            }
        }, interval);
    }

    @Override
    protected void onStop(Player target) {
        phases.remove(target.getUniqueId());
        target.removePotionEffect(PotionEffectType.DARKNESS);
    }
}
