package me.d2glaidee.vexortroll.manager;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.Troll;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final VexorTroll plugin;
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    public CooldownManager(VexorTroll plugin) {
        this.plugin = plugin;
    }

    private String key(UUID uid, String trollId) {
        return uid + ":" + trollId;
    }

    public boolean isOnCooldown(Player target, Troll troll) {
        Long expiry = cooldowns.get(key(target.getUniqueId(), troll.getId()));
        if (expiry == null) return false;
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(key(target.getUniqueId(), troll.getId()));
            return false;
        }
        return true;
    }

    public void setCooldown(Player target, Troll troll) {
        int sec = plugin.getConfig().getInt("cooldown", 10);
        if (sec <= 0) return;
        cooldowns.put(key(target.getUniqueId(), troll.getId()),
                System.currentTimeMillis() + sec * 1000L);
    }

    public long getRemainingSeconds(Player target, Troll troll) {
        Long expiry = cooldowns.get(key(target.getUniqueId(), troll.getId()));
        if (expiry == null) return 0;
        long diff = (expiry - System.currentTimeMillis()) / 1000;
        return Math.max(0, diff);
    }
}
