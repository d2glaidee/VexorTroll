package me.d2glaidee.vexortroll.gui;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.Troll;
import me.d2glaidee.vexortroll.util.Permissions;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class GUIListener implements Listener {

    private final VexorTroll plugin;

    public GUIListener(VexorTroll plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player viewer)) return;

        String title = PlainTextComponentSerializer.plainText().serialize(e.getView().title());

        if (title.startsWith("Выбор жертвы")) {
            e.setCancelled(true);
            onPlayerSelect(viewer, e);
        } else if (title.startsWith(TrollSelectGUI.TITLE_PREFIX)) {
            e.setCancelled(true);
            onTrollSelect(viewer, e);
        }
    }

    private void onPlayerSelect(Player viewer, InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (!viewer.hasPermission(Permissions.MENU)) return;

        if (item.getType() == Material.ARROW && item.hasItemMeta()) {
            Integer page = item.getItemMeta().getPersistentDataContainer()
                    .get(PlayerSelectGUI.pageKey(plugin), PersistentDataType.INTEGER);
            if (page != null) {
                PlayerSelectGUI.open(plugin, viewer, page);
                return;
            }
        }

        if (item.getType() != Material.PLAYER_HEAD) return;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta.getOwningPlayer() == null) return;

        Player target = meta.getOwningPlayer().getPlayer();
        if (target == null || !target.isOnline()) return;

        TrollSelectGUI.open(plugin, viewer, target);
    }

    private void onTrollSelect(Player viewer, InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (!viewer.hasPermission(Permissions.MENU)) return;

        if (item.getType() == Material.ARROW) {
            PlayerSelectGUI.open(plugin, viewer);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String trollId = meta.getPersistentDataContainer()
                .get(TrollSelectGUI.trollKey(plugin), PersistentDataType.STRING);
        String targetUid = meta.getPersistentDataContainer()
                .get(TrollSelectGUI.targetKey(plugin), PersistentDataType.STRING);

        if (trollId == null || targetUid == null) return;

        Troll troll = plugin.getRegistry().get(trollId);
        Player target = Bukkit.getPlayer(UUID.fromString(targetUid));
        if (troll == null || target == null || !target.isOnline()) return;

        if (troll.isRunning(target.getUniqueId())) {
            plugin.getTrollManager().deactivate(target, troll);
        } else {
            plugin.getTrollManager().activate(target, troll);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (viewer.isOnline() && target.isOnline()) {
                TrollSelectGUI.open(plugin, viewer, target);
            }
        }, 1L);
    }
}
