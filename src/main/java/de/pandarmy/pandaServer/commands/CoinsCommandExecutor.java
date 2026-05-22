package de.pandarmy.pandaServer.commands;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.player.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CoinsCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player) {
            File file = new File(Main.playerFolder, player.getUniqueId() + ".json");
            PlayerData playerData = Main.getJsonManager().getJSON(file, PlayerData.class);

            if (playerData == null) {
                player.sendMessage("§cPlayerData not found!");
                return false;
            }

            if (strings.length == 1) {
                try {
                    int number = Integer.parseInt(strings[0]);
                    playerData.setCoins(number);
                    player.sendMessage("§aCoins set to §e" + number);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number: " + strings[0]);
                }
            } else if (strings.length == 0) {
                player.sendMessage("§eYour Coins: §a" + playerData.getCoins());
            } else {
                return false;
            }

        } else {
            commandSender.sendMessage("§cYou must be a player");
        }
        return true;
    }
}