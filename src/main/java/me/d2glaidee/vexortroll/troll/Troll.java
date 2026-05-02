package me.d2glaidee.vexortroll.troll;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Troll {
    String getId();
    String getDisplayName();
    Material getIcon();
    List<String> getLore();
    int getDuration();
    boolean isEnabled();
    void start(Player target);
    void stop(Player target);
    boolean isRunning(UUID playerId);
    void stopAll();
}
