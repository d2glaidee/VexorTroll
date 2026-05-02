package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.entity.Player;

public class CursedHotbarTroll extends AbstractTroll {

    public CursedHotbarTroll(VexorTroll plugin) {
        super(plugin, "cursed_hotbar");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 15);
        int max = cfgInt("max-interval", 50);

        randomLoop(target, () -> {
            int current = target.getInventory().getHeldItemSlot();
            int next;
            do {
                next = rng().nextInt(9);
            } while (next == current);
            target.getInventory().setHeldItemSlot(next);
        }, min, max);
    }
}
