package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StalkerTroll extends AbstractTroll {

    public StalkerTroll(VexorTroll plugin) {
        super(plugin, "stalker");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 80);
        int max = cfgInt("max-interval", 200);
        double dist = cfgDouble("distance", 12);

        randomLoop(target, () -> {
            Location loc = behind(target, dist);
            loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);

            if (!loc.getChunk().isLoaded()) return;

            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class, as -> {
                as.setVisible(false);
                as.setGravity(false);
                as.setInvulnerable(true);
                as.setBasePlate(false);
                as.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
                as.setCustomNameVisible(false);
                as.setSilent(true);

                Location lookAt = target.getLocation();
                double dx = lookAt.getX() - loc.getX();
                double dz = lookAt.getZ() - loc.getZ();
                float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                as.setRotation(yaw, 0);
            });

            trackEntity(target.getUniqueId(), stand);
            soundNear(target, Sound.AMBIENT_CAVE, 3, 0.3f, 0.5f);

            later(target, stand::remove, rng().nextInt(30, 60));
        }, min, max);
    }
}
