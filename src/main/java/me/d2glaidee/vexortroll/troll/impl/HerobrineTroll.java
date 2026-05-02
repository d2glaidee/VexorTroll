package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class HerobrineTroll extends AbstractTroll {

    public HerobrineTroll(VexorTroll plugin) {
        super(plugin, "herobrine");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 100);
        int max = cfgInt("max-interval", 250);
        double dist = cfgDouble("distance", 15);

        randomLoop(target, () -> {
            int action = rng().nextInt(3);
            switch (action) {
                case 0 -> spawnFigure(target, dist);
                case 1 -> placeClientTorch(target);
                case 2 -> {
                    spawnFigure(target, dist * 0.6);
                    target.playSound(target.getLocation(), Sound.AMBIENT_CAVE, 0.4f, 0.3f);
                }
            }
        }, min, max);
    }

    private void spawnFigure(Player target, double distance) {
        double angle = rng().nextDouble() * Math.PI * 2;
        Location loc = target.getLocation().clone().add(
                Math.cos(angle) * distance, 0, Math.sin(angle) * distance);
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
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2")));
            skull.setItemMeta(meta);
            as.getEquipment().setHelmet(skull);

            Location look = target.getLocation();
            double dx = look.getX() - loc.getX();
            double dz = look.getZ() - loc.getZ();
            as.setRotation((float) Math.toDegrees(Math.atan2(-dx, dz)), 0);
        });

        trackEntity(target.getUniqueId(), stand);
        later(target, stand::remove, rng().nextInt(40, 80));
    }

    private void placeClientTorch(Player target) {
        Location loc = randomNear(target.getLocation(), 8);
        loc.setY(target.getLocation().getBlockY());

        if (!loc.getBlock().getType().isAir()) return;

        sendFakeBlock(target, loc, Material.REDSTONE_TORCH);
        later(target, () -> restoreFakeBlock(target, loc), rng().nextInt(100, 200));
    }
}
