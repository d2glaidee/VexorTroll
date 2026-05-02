package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WorldCorruptTroll extends AbstractTroll {

    private static final Material[] CORRUPTION = {
            Material.BEDROCK, Material.CRYING_OBSIDIAN, Material.SCULK,
            Material.DEEPSLATE, Material.TINTED_GLASS, Material.OBSIDIAN,
            Material.END_STONE, Material.NETHERRACK, Material.SOUL_SOIL,
            Material.MAGMA_BLOCK, Material.BLACKSTONE, Material.AIR
    };

    public WorldCorruptTroll(VexorTroll plugin) {
        super(plugin, "world_corrupt");
    }

    @Override
    protected void onStart(Player target) {
        int radius = cfgInt("radius", 8);
        Location center = target.getLocation().clone();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -3; y <= 4; y++) {
                    if (rng().nextDouble() > 0.3) continue;

                    Location loc = center.clone().add(x, y, z);
                    if (loc.getBlock().getType().isAir() && rng().nextDouble() > 0.15) continue;

                    Material mat = CORRUPTION[rng().nextInt(CORRUPTION.length)];
                    sendFakeBlock(target, loc, mat);
                }
            }
        }
    }
}
