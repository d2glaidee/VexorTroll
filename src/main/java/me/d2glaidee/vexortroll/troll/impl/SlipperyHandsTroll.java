package me.d2glaidee.vexortroll.troll.impl;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.AbstractTroll;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class SlipperyHandsTroll extends AbstractTroll implements Listener {

    public SlipperyHandsTroll(VexorTroll plugin) {
        super(plugin, "slippery_hands");
    }

    @Override
    protected void onStart(Player target) {}

    private void tryDrop(Player p) {
        if (!isRunning(p.getUniqueId())) return;
        double chance = cfgDouble("drop-chance", 0.35);
        if (rng().nextDouble() > chance) return;

        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.isEmpty()) return;

        ItemStack drop = hand.clone();
        drop.setAmount(1);
        hand.setAmount(hand.getAmount() - 1);
        p.getInventory().setItemInMainHand(hand);

        p.getWorld().dropItemNaturally(p.getLocation(), drop);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        tryDrop(e.getPlayer());
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {
        tryDrop(e.getPlayer());
    }
}
