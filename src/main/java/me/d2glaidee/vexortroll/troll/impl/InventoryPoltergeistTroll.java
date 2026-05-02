package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryPoltergeistTroll extends AbstractTroll {

    public InventoryPoltergeistTroll(VexorTroll plugin) {
        super(plugin, "inventory_poltergeist");
    }

    @Override
    protected void onStart(Player target) {
        int min = cfgInt("min-interval", 30);
        int max = cfgInt("max-interval", 80);

        randomLoop(target, () -> {
            PlayerInventory inv = target.getInventory();
            int a = rng().nextInt(36);
            int b = rng().nextInt(36);
            if (a == b) return;

            ItemStack itemA = inv.getItem(a);
            ItemStack itemB = inv.getItem(b);
            if ((itemA == null || itemA.isEmpty()) && (itemB == null || itemB.isEmpty())) return;

            inv.setItem(a, itemB);
            inv.setItem(b, itemA);
        }, min, max);
    }
}
