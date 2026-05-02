package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GravityAnomalyTroll extends AbstractTroll implements Listener {

    public GravityAnomalyTroll(VexorTroll plugin) {
        super(plugin, "gravity_anomaly");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 30);
        int max = cfgInt("max-interval", 80);

        randomLoop(target, () -> {
            int type = rng().nextInt(3);
            switch (type) {
                case 0 -> {
                    applyEffect(target, PotionEffectType.LEVITATION, 30, rng().nextInt(1, 4));
                    applyEffect(target, PotionEffectType.SLOW_FALLING, 100, 0);
                }
                case 1 -> {
                    double power = cfgDouble("launch-power", 1.2);
                    target.setVelocity(new Vector(0, power, 0));
                    applyEffect(target, PotionEffectType.SLOW_FALLING, 80, 0);
                }
                case 2 -> {
                    applyEffect(target, PotionEffectType.SLOW_FALLING, 60, 2);
                    target.setVelocity(target.getVelocity().add(new Vector(
                            rng().nextDouble(-0.5, 0.5), 0.3, rng().nextDouble(-0.5, 0.5))));
                }
            }
        }, min, max);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (!isRunning(p.getUniqueId())) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
}
