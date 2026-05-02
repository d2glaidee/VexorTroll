package me.d2glaidee.vexortroll.gui;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.troll.Troll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrollSelectGUI {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    public static final String TITLE_PREFIX = "Троллинг: ";

    public static NamespacedKey trollKey(VexorTroll plugin) {
        return new NamespacedKey(plugin, "troll_id");
    }

    public static NamespacedKey targetKey(VexorTroll plugin) {
        return new NamespacedKey(plugin, "target_uuid");
    }

    public static void open(VexorTroll plugin, Player viewer, Player target) {
        List<Troll> enabled = plugin.getRegistry().getAll().values().stream()
                .filter(Troll::isEnabled).toList();

        int size = Math.max(36, Math.min(54, ((enabled.size() + 9) / 9) * 9));

        Component title = Component.text(TITLE_PREFIX + target.getName(), NamedTextColor.DARK_GRAY);
        Inventory inv = Bukkit.createInventory(null, size, title);

        UUID tid = target.getUniqueId();

        for (int i = 0; i < enabled.size() && i < size - 9; i++) {
            Troll troll = enabled.get(i);
            boolean running = troll.isRunning(tid);

            ItemStack item = new ItemStack(troll.getIcon());
            ItemMeta meta = item.getItemMeta();

            Component name = LEGACY.deserialize(troll.getDisplayName())
                    .decoration(TextDecoration.ITALIC, false);
            if (running) {
                name = Component.text("● ", NamedTextColor.GREEN)
                        .append(name).decoration(TextDecoration.ITALIC, false);
            }
            meta.displayName(name);

            List<Component> lore = new ArrayList<>();
            troll.getLore().forEach(l -> lore.add(LEGACY.deserialize(l)
                    .decoration(TextDecoration.ITALIC, false)));

            lore.add(Component.empty());
            if (running) {
                lore.add(Component.text("Нажми чтобы отключить", NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                long cd = plugin.getCooldownManager().getRemainingSeconds(target, troll);
                if (cd > 0) {
                    lore.add(Component.text("Кулдаун: " + cd + "с", NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false));
                } else {
                    lore.add(Component.text("Нажми чтобы включить", NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false));
                }
            }

            int dur = troll.getDuration();
            if (dur > 0) {
                lore.add(Component.text("Длительность: " + dur + "с", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false));
            }

            meta.lore(lore);

            if (running) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.getPersistentDataContainer().set(trollKey(plugin), PersistentDataType.STRING, troll.getId());
            meta.getPersistentDataContainer().set(targetKey(plugin), PersistentDataType.STRING, tid.toString());

            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        int lastRow = size - 9;

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("← Назад", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));
        back.setItemMeta(backMeta);
        inv.setItem(lastRow, back);

        ItemStack brand = new ItemStack(Material.NETHER_STAR);
        ItemMeta brandMeta = brand.getItemMeta();
        brandMeta.displayName(Component.text("VexorTroll", NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false));
        brandMeta.lore(List.of(
                Component.empty(),
                Component.text("by d2glaidee", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("github.com/d2glaidee", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));
        brand.setItemMeta(brandMeta);
        inv.setItem(lastRow + 4, brand);

        viewer.openInventory(inv);
    }
}
