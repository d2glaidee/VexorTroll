package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public class DoppelgangerTroll extends AbstractTroll {

    public DoppelgangerTroll(VexorTroll plugin) {
        super(plugin, "doppelganger");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 80);
        int max = cfgInt("max-interval", 180);

        randomLoop(target, () -> {
            double angle = rng().nextDouble() * Math.PI * 2;
            double dist = rng().nextDouble(5, 10);
            Location loc = target.getLocation().clone().add(
                    Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
            loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);

            if (!loc.getChunk().isLoaded()) return;

            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class, as -> {
                as.setVisible(false);
                as.setGravity(false);
                as.setInvulnerable(true);
                as.setBasePlate(false);
                as.setSilent(true);

                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwningPlayer(target);
                skull.setItemMeta(meta);

                PlayerInventory inv = target.getInventory();
                as.getEquipment().setHelmet(skull);
                as.getEquipment().setChestplate(inv.getChestplate());
                as.getEquipment().setLeggings(inv.getLeggings());
                as.getEquipment().setBoots(inv.getBoots());

                Location look = target.getLocation();
                double dx = look.getX() - loc.getX();
                double dz = look.getZ() - loc.getZ();
                as.setRotation((float) Math.toDegrees(Math.atan2(-dx, dz)), 0);
            });

            trackEntity(target.getUniqueId(), stand);
            target.playSound(loc, Sound.PARTICLE_SOUL_ESCAPE, 0.3f, 0.5f);

            later(target, stand::remove, rng().nextInt(40, 100));
        }, min, max);
    }
}
