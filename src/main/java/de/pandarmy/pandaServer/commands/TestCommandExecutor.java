package de.pandarmy.pandaServer.commands;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class TestCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player){

            if (strings.length != 1) return false;
            String arg1 = strings[0];
            try{
                int price = Integer.parseInt(arg1);

                ItemStack item = player.getInventory().getItemInMainHand();

                if (item == null){
                    player.sendMessage("§cNo item in main hand");
                    return false;
                }


                OfferData data = new OfferData(Main.getInstance().getConfig().getInt("JsonOfferCounter") ,player.getUniqueId(), player.getName(), price, item);

                player.getInventory().removeItem(item);
                int jsonOfferCounter = Main.getInstance().getConfig().getInt("JsonOfferCounter");
                Main.getJsonManager().createJSON(data, jsonOfferCounter + ".json", Main.offerFolder);
            }catch (Exception e){
                e.printStackTrace();
                player.sendMessage("§cFehler: " + e.getMessage());
            }
        }
        return false;
    }
}
