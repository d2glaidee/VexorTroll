package me.d2glaidee.vexortroll;

import me.d2glaidee.vexortroll.command.TrollCommand;
import me.d2glaidee.vexortroll.gui.GUIListener;
import me.d2glaidee.vexortroll.listener.PlayerQuitListener;
import me.d2glaidee.vexortroll.manager.CooldownManager;
import me.d2glaidee.vexortroll.manager.TrollManager;
import me.d2glaidee.vexortroll.registry.TrollRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class VexorTroll extends JavaPlugin {

    private static VexorTroll instance;
    private TrollRegistry registry;
    private TrollManager trollManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        cooldownManager = new CooldownManager(this);
        registry = new TrollRegistry(this);
        trollManager = new TrollManager(this);

        registry.registerDefaults();

        TrollCommand trollCommand = new TrollCommand(this);
        getCommand("troll").setExecutor(trollCommand);
        getCommand("troll").setTabCompleter(trollCommand);

        reg(new GUIListener(this));
        reg(new PlayerQuitListener(this));

        registry.getAll().values().stream()
                .filter(t -> t instanceof Listener)
                .forEach(t -> reg((Listener) t));

        getLogger().info("");
        getLogger().info("  VexorTroll v" + getDescription().getVersion());
        getLogger().info("  Loaded " + registry.getAll().size() + " trolls");
        getLogger().info("  github.com/d2glaidee");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        if (trollManager != null) trollManager.stopAll();
        instance = null;
    }

    private void reg(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public static VexorTroll inst() { return instance; }
    public TrollRegistry getRegistry() { return registry; }
    public TrollManager getTrollManager() { return trollManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
}
