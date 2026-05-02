package me.d2glaidee.vexortroll.troll;

import me.d2glaidee.vexortroll.VexorTroll;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractTroll implements Troll {

    protected final VexorTroll plugin;
    protected final String id;

    private final Set<UUID> active = ConcurrentHashMap.newKeySet();
    private final Map<UUID, List<BukkitTask>> tasks = new ConcurrentHashMap<>();
    private final Map<UUID, List<Entity>> entities = new ConcurrentHashMap<>();
    private final Map<UUID, Set<Location>> fakeBlocks = new ConcurrentHashMap<>();
    private final Map<UUID, Set<PotionEffectType>> effects = new ConcurrentHashMap<>();

    public AbstractTroll(VexorTroll plugin, String id) {
        this.plugin = plugin;
        this.id = id;
    }

    // --- config helpers ---

    protected String path(String key) { return "trolls." + id + "." + key; }
    protected int cfgInt(String key, int def) { return plugin.getConfig().getInt(path(key), def); }
    protected double cfgDouble(String key, double def) { return plugin.getConfig().getDouble(path(key), def); }
    protected String cfgString(String key, String def) { return plugin.getConfig().getString(path(key), def); }

    // --- interface ---

    @Override public String getId() { return id; }

    @Override
    public String getDisplayName() { return cfgString("display-name", id); }

    @Override
    public Material getIcon() {
        try { return Material.valueOf(cfgString("icon", "STONE")); }
        catch (Exception e) { return Material.STONE; }
    }

    @Override
    public List<String> getLore() { return plugin.getConfig().getStringList(path("lore")); }

    @Override
    public int getDuration() { return cfgInt("duration", plugin.getConfig().getInt("default-duration", 30)); }

    @Override
    public boolean isEnabled() { return plugin.getConfig().getBoolean(path("enabled"), true); }

    @Override
    public boolean isRunning(UUID uid) { return active.contains(uid); }

    // --- lifecycle ---

    @Override
    public void start(Player target) {
        UUID uid = target.getUniqueId();
        if (active.contains(uid)) stop(target);

        active.add(uid);
        onStart(target);

        int dur = getDuration();
        if (dur > 0) {
            addTask(uid, Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (active.contains(uid)) stop(target);
            }, dur * 20L));
        }
    }

    @Override
    public void stop(Player target) {
        UUID uid = target.getUniqueId();
        if (!active.remove(uid)) return;

        onStop(target);
        cleanup(uid, target);
    }

    @Override
    public void stopAll() {
        for (UUID uid : Set.copyOf(active)) {
            Player p = Bukkit.getPlayer(uid);
            if (p != null) stop(p);
            else {
                active.remove(uid);
                cleanup(uid, null);
            }
        }
    }

    private void cleanup(UUID uid, Player target) {
        List<BukkitTask> t = tasks.remove(uid);
        if (t != null) t.forEach(BukkitTask::cancel);

        List<Entity> ents = entities.remove(uid);
        if (ents != null) ents.forEach(Entity::remove);

        if (target != null) {
            Set<Location> blocks = fakeBlocks.remove(uid);
            if (blocks != null) {
                blocks.forEach(loc -> target.sendBlockChange(loc, loc.getBlock().getBlockData()));
            }

            Set<PotionEffectType> eff = effects.remove(uid);
            if (eff != null) eff.forEach(target::removePotionEffect);
        }
    }

    protected abstract void onStart(Player target);
    protected void onStop(Player target) {}

    // --- task helpers ---

    protected void addTask(UUID uid, BukkitTask task) {
        tasks.computeIfAbsent(uid, k -> Collections.synchronizedList(new ArrayList<>())).add(task);
    }

    protected void repeat(Player target, Runnable action, long period) {
        repeat(target, action, period, period);
    }

    protected void repeat(Player target, Runnable action, long delay, long period) {
        UUID uid = target.getUniqueId();
        addTask(uid, Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!active.contains(uid) || !target.isOnline()) return;
            action.run();
        }, delay, period));
    }

    protected void later(Player target, Runnable action, long delay) {
        UUID uid = target.getUniqueId();
        addTask(uid, Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!active.contains(uid) || !target.isOnline()) return;
            action.run();
        }, delay));
    }

    protected void randomLoop(Player target, Runnable action, int minTicks, int maxTicks) {
        UUID uid = target.getUniqueId();
        if (!active.contains(uid) || !target.isOnline()) return;

        int delay = ThreadLocalRandom.current().nextInt(minTicks, maxTicks + 1);
        addTask(uid, Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!active.contains(uid) || !target.isOnline()) return;
            action.run();
            randomLoop(target, action, minTicks, maxTicks);
        }, delay));
    }

    // --- entity tracking ---

    protected void trackEntity(UUID uid, Entity entity) {
        entities.computeIfAbsent(uid, k -> Collections.synchronizedList(new ArrayList<>())).add(entity);
    }

    // --- fake blocks ---

    protected void sendFakeBlock(Player target, Location loc, Material material) {
        target.sendBlockChange(loc, material.createBlockData());
        fakeBlocks.computeIfAbsent(target.getUniqueId(), k -> ConcurrentHashMap.newKeySet()).add(loc);
    }

    protected void restoreFakeBlock(Player target, Location loc) {
        target.sendBlockChange(loc, loc.getBlock().getBlockData());
        Set<Location> set = fakeBlocks.get(target.getUniqueId());
        if (set != null) set.remove(loc);
    }

    // --- effects ---

    protected void applyEffect(Player target, PotionEffectType type, int ticks, int amplifier) {
        applyEffect(target, type, ticks, amplifier, false);
    }

    protected void applyEffect(Player target, PotionEffectType type, int ticks, int amplifier, boolean ambient) {
        target.addPotionEffect(new PotionEffect(type, ticks, amplifier, ambient, false, false));
        effects.computeIfAbsent(target.getUniqueId(), k -> ConcurrentHashMap.newKeySet()).add(type);
    }

    // --- utility ---

    protected ThreadLocalRandom rng() {
        return ThreadLocalRandom.current();
    }

    protected Location randomNear(Location center, double radius) {
        double angle = rng().nextDouble() * Math.PI * 2;
        double dist = rng().nextDouble() * radius;
        return center.clone().add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
    }

    protected Location randomNear3D(Location center, double radius) {
        double angle = rng().nextDouble() * Math.PI * 2;
        double dist = rng().nextDouble() * radius;
        double dy = rng().nextDouble(-radius / 2, radius / 2);
        return center.clone().add(Math.cos(angle) * dist, dy, Math.sin(angle) * dist);
    }

    protected void soundNear(Player p, Sound sound, double maxOffset, float volume, float pitch) {
        Location loc = randomNear3D(p.getLocation(), maxOffset);
        p.playSound(loc, sound, volume, pitch);
    }

    protected Location behind(Player p, double distance) {
        Location loc = p.getLocation();
        double yawRad = Math.toRadians(loc.getYaw());
        return loc.clone().add(Math.sin(yawRad) * distance, 0, -Math.cos(yawRad) * distance);
    }
}
