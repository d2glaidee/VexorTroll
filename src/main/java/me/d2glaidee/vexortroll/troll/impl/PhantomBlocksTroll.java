package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PhantomBlocksTroll extends AbstractTroll {

    private static final Material[] CREEPY_BLOCKS = {
            Material.COBWEB, Material.SOUL_SAND, Material.CRYING_OBSIDIAN,
            Material.SCULK, Material.NETHER_PORTAL, Material.REDSTONE_TORCH,
            Material.SOUL_FIRE, Material.END_PORTAL
    };

    public PhantomBlocksTroll(VexorTroll plugin) {
        super(plugin, "phantom_blocks");
    }

    @Override
    protected void onStart(Player target) {
        int radius = cfgInt("radius", 5);
        int interval = cfgInt("interval", 25);

        repeat(target, () -> {
            Location base = target.getLocation();
            int x = base.getBlockX() + rng().nextInt(-radius, radius + 1);
            int z = base.getBlockZ() + rng().nextInt(-radius, radius + 1);
            int y = base.getBlockY() + rng().nextInt(-1, 3);
            Location loc = new Location(base.getWorld(), x, y, z);

            if (!loc.getBlock().getType().isAir()) return;

            Material mat = CREEPY_BLOCKS[rng().nextInt(CREEPY_BLOCKS.length)];
            sendFakeBlock(target, loc, mat);

            later(target, () -> restoreFakeBlock(target, loc), rng().nextInt(20, 60));
        }, interval);
    }
}
