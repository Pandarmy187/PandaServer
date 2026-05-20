package de.pandarmy.pandaServer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player){
            if (strings.length == 0){
                player.sendMessage("§7[§2Ping§7] §eYour ping is §a" + player.getPing());
            }else if (strings.length == 1){
                String arg1 = strings[0];
                Player argPlayer = Bukkit.getPlayer(arg1);
                if (argPlayer != null){
                    player.sendMessage("§7[§2Ping§7] §b" + argPlayer.getName() + "'s §e ping is §a" + argPlayer.getPing());
                }else{
                    player.sendMessage("§cInvalid player: '" + arg1 + "'");
                }
            }

        }else{
            commandSender.sendMessage("§cYou must be a player");
            return true;
        }
        return false;
    }
}
