package me.d2glaidee.vexortroll.registry;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.Troll;
import me.d2glaidee.vexortroll.troll.impl.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrollRegistry {

    private final VexorTroll plugin;
    private final Map<String, Troll> trolls = new LinkedHashMap<>();

    public TrollRegistry(VexorTroll plugin) {
        this.plugin = plugin;
    }

    public void register(Troll troll) {
        trolls.put(troll.getId(), troll);
    }

    public Troll get(String id) {
        return trolls.get(id);
    }

    public Map<String, Troll> getAll() {
        return Collections.unmodifiableMap(trolls);
    }

    public void registerDefaults() {
        register(new InvertedControlsTroll(plugin));
        register(new SchizophreniaTroll(plugin));
        register(new SlipperyHandsTroll(plugin));
        register(new GravityAnomalyTroll(plugin));
        register(new PhantomBlocksTroll(plugin));
        register(new StalkerTroll(plugin));
        register(new InventoryPoltergeistTroll(plugin));
        register(new SonicBoomTroll(plugin));
        register(new VertigoTroll(plugin));
        register(new MatrixGlitchTroll(plugin));
        register(new TimeLoopTroll(plugin));
        register(new ParanoiaTroll(plugin));
        register(new PhantomTouchTroll(plugin));
        register(new VoidGlimpseTroll(plugin));
        register(new HerobrineTroll(plugin));
        register(new GroundDecayTroll(plugin));
        register(new SoulDragTroll(plugin));
        register(new NightTerrorTroll(plugin));
        register(new CreeperWhisperTroll(plugin));
        register(new DoppelgangerTroll(plugin));
        register(new EarthquakeTroll(plugin));
        register(new WorldCorruptTroll(plugin));
        register(new NeckSnapTroll(plugin));
        register(new DesyncTroll(plugin));
        register(new CursedHotbarTroll(plugin));
    }
}
