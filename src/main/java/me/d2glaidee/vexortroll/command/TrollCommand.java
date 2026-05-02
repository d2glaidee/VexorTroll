package me.d2glaidee.vexortroll.command;

import me.d2glaidee.vexortroll.VexorTroll;
import me.d2glaidee.vexortroll.gui.PlayerSelectGUI;
import me.d2glaidee.vexortroll.util.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrollCommand implements CommandExecutor, TabCompleter {

    private final VexorTroll plugin;

    public TrollCommand(VexorTroll plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String label, String @NotNull [] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(Permissions.RELOAD)) {
                sender.sendMessage(Component.text("Недостаточно прав.", NamedTextColor.RED));
                return true;
            }

            plugin.getTrollManager().stopAll();
            plugin.reloadConfig();

            sender.sendMessage(Component.text("VexorTroll ", NamedTextColor.LIGHT_PURPLE)
                    .append(Component.text("конфиг перезагружен.", NamedTextColor.GRAY)));
            return true;
        }

        if (!(sender instanceof Player p)) {
            sender.sendMessage(Component.text("Только для игроков.", NamedTextColor.RED));
            return true;
        }

        if (!p.hasPermission(Permissions.USE)) return true;

        PlayerSelectGUI.open(plugin, p);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                                @NotNull String label, String @NotNull [] args) {
        if (args.length == 1 && sender.hasPermission(Permissions.RELOAD)) {
            return List.of("reload");
        }
        return List.of();
    }
}
