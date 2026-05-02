package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class VoidGlimpseTroll extends AbstractTroll implements Listener {

    public VoidGlimpseTroll(VexorTroll plugin) {
        super(plugin, "void_glimpse");
    }

    @Override
    protected void onStart(Player target) {
        Location saved = target.getLocation().clone();
        int fallDur = cfgInt("fall-duration", 60);

        Location up = saved.clone().add(0, 45, 0);
        up.setYaw(saved.getYaw());
        up.setPitch(90);

        applyEffect(target, PotionEffectType.DARKNESS, fallDur + 40, 0);
        applyEffect(target, PotionEffectType.RESISTANCE, fallDur + 40, 255);
        applyEffect(target, PotionEffectType.SLOW_FALLING, fallDur + 40, 0);

        target.teleport(up);
        target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.6f, 0.3f);

        repeat(target, () -> {
            target.spawnParticle(Particle.SCULK_SOUL, target.getLocation(), 3, 1, 2, 1, 0);
            if (rng().nextInt(3) == 0) {
                target.playSound(target.getLocation(), Sound.AMBIENT_CAVE, 0.5f, 0.3f);
            }
        }, 10, 10);

        later(target, () -> {
            target.teleport(saved);
            target.playSound(saved, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.5f);
            target.removePotionEffect(PotionEffectType.DARKNESS);
            target.removePotionEffect(PotionEffectType.RESISTANCE);
            target.removePotionEffect(PotionEffectType.SLOW_FALLING);
            stop(target);
        }, fallDur);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (!isRunning(p.getUniqueId())) return;
        e.setCancelled(true);
    }
}
