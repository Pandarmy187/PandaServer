package de.pandarmy.pandaServer.commands;

import de.pandarmy.pandaServer.util.Inventories;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player){
            player.openInventory(Inventories.writeShopInventory(0));
        }else{
            commandSender.sendMessage("§cYou must be a player!");
        }
        return false;
    }
}
