package me.d2glaidee.vexortroll.manager;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.Troll;
import me.d2glaidee.vexortroll.util.Permissions;
import org.bukkit.entity.Player;

public class TrollManager {

    private final VexorTroll plugin;

    public TrollManager(VexorTroll plugin) {
        this.plugin = plugin;
    }

    public boolean activate(Player target, Troll troll) {
        if (target.hasPermission(Permissions.BYPASS)) return false;
        if (!troll.isEnabled()) return false;
        if (plugin.getCooldownManager().isOnCooldown(target, troll)) return false;

        troll.start(target);
        plugin.getCooldownManager().setCooldown(target, troll);
        return true;
    }

    public void deactivate(Player target, Troll troll) {
        troll.stop(target);
    }

    public void stopAllFor(Player target) {
        plugin.getRegistry().getAll().values().forEach(t -> {
            if (t.isRunning(target.getUniqueId())) t.stop(target);
        });
    }

    public void stopAll() {
        plugin.getRegistry().getAll().values().forEach(Troll::stopAll);
    }
}
