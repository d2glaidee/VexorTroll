package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GroundDecayTroll extends AbstractTroll {

    public GroundDecayTroll(VexorTroll plugin) {
        super(plugin, "ground_decay");
    }

    @Override
    protected void onStart(Player target) {
        int radius = cfgInt("radius", 3);
        int min = cfgInt("min-interval", 20);
        int max = cfgInt("max-interval", 60);

        randomLoop(target, () -> {
            Location feet = target.getLocation();
            int ox = rng().nextInt(-radius, radius + 1);
            int oz = rng().nextInt(-radius, radius + 1);
            Location block = feet.clone().add(ox, -1, oz);

            if (block.getBlock().getType().isAir()) return;

            sendFakeBlock(target, block, Material.AIR);
            target.playSound(block, Sound.BLOCK_STONE_BREAK, 0.2f, 0.5f);

            later(target, () -> restoreFakeBlock(target, block), rng().nextInt(30, 80));
        }, min, max);
    }
}
