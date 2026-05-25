package de.pandarmy.pandaServer.commands;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import de.pandarmy.pandaServer.util.Inventories;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SellCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length != 1) return false;
            try {
                int price = Integer.parseInt(strings[0]);
                ItemStack item = player.getInventory().getItemInMainHand();

                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage("§cNo item in main hand");
                    return false;
                }

                player.openInventory(Inventories.writeSellInventory(item, price));

            } catch (Exception e) {
                player.sendMessage("§cFehler: " + e.getMessage());
            }
        }
        return false;
    }
}
