package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class VertigoTroll extends AbstractTroll implements Listener {

    public VertigoTroll(VexorTroll plugin) {
        super(plugin, "vertigo");
    }

    @Override
    protected void onStart(Player target) {
        applyEffect(target, PotionEffectType.NAUSEA, getDuration() * 20, 0, true);

        repeat(target, () -> {
            Location loc = target.getLocation();
            double ox = rng().nextDouble(-0.3, 0.3);
            double oz = rng().nextDouble(-0.3, 0.3);
            loc.add(ox, 0, oz);
            loc.setYaw(loc.getYaw() + (float) rng().nextDouble(-3, 3));
            target.teleport(loc);
        }, 8, 8);
    }

    @Override
    protected void onStop(Player target) {
        target.removePotionEffect(PotionEffectType.NAUSEA);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (!isRunning(p.getUniqueId())) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }
}
