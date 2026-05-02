package me.d2glaidee.vexortroll.gui;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.util.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class PlayerSelectGUI {

    private static final int SLOTS_PER_PAGE = 45;
    private static final int INV_SIZE = 54;

    public static NamespacedKey pageKey(VexorTroll plugin) {
        return new NamespacedKey(plugin, "page");
    }

    public static void open(VexorTroll plugin, Player viewer) {
        open(plugin, viewer, 0);
    }

    public static void open(VexorTroll plugin, Player viewer, int page) {
        List<Player> targets = Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.getUniqueId().equals(viewer.getUniqueId()))
                .filter(p -> !p.hasPermission(Permissions.BYPASS))
                .map(p -> (Player) p)
                .toList();

        int totalPages = Math.max(1, (int) Math.ceil((double) targets.size() / SLOTS_PER_PAGE));
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;

        String pageInfo = targets.size() > SLOTS_PER_PAGE ? " [" + (page + 1) + "/" + totalPages + "]" : "";
        Component title = Component.text("Выбор жертвы" + pageInfo, NamedTextColor.DARK_GRAY);

        if (targets.size() <= 9) {
            Inventory inv = Bukkit.createInventory(null, 18, title);
            for (int i = 0; i < targets.size(); i++) {
                inv.setItem(i, createHead(plugin, targets.get(i)));
            }
            inv.setItem(13, createBrand());
            viewer.openInventory(inv);
            return;
        }

        Inventory inv = Bukkit.createInventory(null, INV_SIZE, title);

        int start = page * SLOTS_PER_PAGE;
        int end = Math.min(start + SLOTS_PER_PAGE, targets.size());

        for (int i = start; i < end; i++) {
            inv.setItem(i - start, createHead(plugin, targets.get(i)));
        }

        if (page > 0) {
            inv.setItem(45, createNavItem(plugin, "&7← Назад", page - 1));
        }

        inv.setItem(49, createBrand());

        if (page < totalPages - 1) {
            inv.setItem(53, createNavItem(plugin, "&7Вперёд →", page + 1));
        }

        viewer.openInventory(inv);
    }

    private static ItemStack createHead(VexorTroll plugin, Player target) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(target);
        meta.displayName(Component.text(target.getName(), NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false));

        long active = plugin.getRegistry().getAll().values().stream()
                .filter(t -> t.isRunning(target.getUniqueId())).count();
        if (active > 0) {
            meta.lore(List.of(Component.text("Активных: " + active, NamedTextColor.RED)
                    .decoration(TextDecoration.ITALIC, false)));
        }

        head.setItemMeta(meta);
        return head;
    }

    private static ItemStack createNavItem(VexorTroll plugin, String name, int targetPage) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                .legacyAmpersand().deserialize(name).decoration(TextDecoration.ITALIC, false));
        meta.getPersistentDataContainer().set(pageKey(plugin), PersistentDataType.INTEGER, targetPage);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createBrand() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("VexorTroll", NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.empty(),
                Component.text("by d2glaidee", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("github.com/d2glaidee", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
