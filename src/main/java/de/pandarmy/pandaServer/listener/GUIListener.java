package de.pandarmy.pandaServer.listener;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import de.pandarmy.pandaServer.player.PlayerData;
import de.pandarmy.pandaServer.util.Inventories;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.List;

public class GUIListener implements Listener {

    Main main = Main.getInstance();

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) return;

        if ("Shop".equalsIgnoreCase(event.getView().getTitle())){
            event.setCancelled(true);
            if (hasLocalID(currentItem)){
                if (getLocalID(currentItem).equals("back")){
                    player.closeInventory();
                }else if (returnItemPermission(currentItem)){
                    player.openInventory(Inventories.writeItemBuyInventory(currentItem));
                }
            }
        }else if ("Buying Item...".equalsIgnoreCase(event.getView().getTitle())){
            event.setCancelled(true);
            if (!hasLocalID(currentItem)) return;
            if (getLocalID(currentItem).equals("back")){
                player.openInventory(Inventories.writeShopInventory());
            }else if (getLocalID(currentItem).equals("cancel")){
                player.openInventory(Inventories.writeShopInventory());
            }else if (getLocalID(currentItem).equals("buy")){

                String offerID = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "offerid"), PersistentDataType.STRING);// holt die offerID aus customID (key = offerid)
                Main.getInstance().getLogger().warning("offerID: " + offerID); // <- Debug
                Main.getInstance().getLogger().warning("localID: " + getLocalID(currentItem));
                File offerFile = new File(Main.offerFolder, offerID + ".json"); //Holt die Offer Datei, Name = id im Item
                OfferData offerData = Main.getJsonManager().getJSON(offerFile, OfferData.class); // Returnt eine OfferData Klasse/Objekt

                if (!offerFile.exists()){
                    player.openInventory(Inventories.writeShopInventory());
                    player.sendMessage("§eItem already sold!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    return;
                }
                File playerFile = new File(Main.playerFolder, player.getUniqueId() + ".json");
                PlayerData playerData = Main.getJsonManager().getJSON(playerFile, PlayerData.class);

                if (playerData.getCoins() >= offerData.getPrice()){
                    Main.getJsonManager().removeFile(Main.offerFolder, offerData.getID() + ".json");
                    playerData.setCoins(playerData.getCoins() - offerData.getPrice());
                    player.getInventory().addItem(offerData.getItem());
                    player.sendMessage("§aItem gekauft!");
                    player.sendMessage("§eNeuer Kontostand: §b" + playerData.getCoins());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.openInventory(Inventories.writeShopInventory());
                }else{
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    player.sendMessage("§cNot enough Coins: §e" + playerData.getCoins() + "§f/§c" + offerData.getPrice());
                }
            }
        }
    }

    public boolean hasLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "id"));
    }

    public String getLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "id"), PersistentDataType.STRING);
    }

    public boolean returnItemPermission(ItemStack item){
        List<OfferData> offers = Main.getJsonManager().getAllEntries(Main.offerFolder, OfferData.class);

        for (OfferData offer : offers){

            if (String.valueOf(offer.getID()).equals(getLocalID(item))){
                Main.getInstance().getLogger().warning(getLocalID(item) + " " + offer.getID());
                return true;
            }
        }
        return false;
    }


}
