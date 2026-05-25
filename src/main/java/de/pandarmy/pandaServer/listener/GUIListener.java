package de.pandarmy.pandaServer.listener;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import de.pandarmy.pandaServer.player.PlayerData;
import de.pandarmy.pandaServer.util.Inventories;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.UUID;

public class GUIListener implements Listener {

    Main main = Main.getInstance();

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) return;

        if ("Shop".equalsIgnoreCase(event.getView().getTitle())) {
            event.setCancelled(true);
            if (!hasLocalID(currentItem)) return;

            String id = getLocalID(currentItem);

            if (id.equals("back")) {
                player.closeInventory();

            } else if (id.equals("prevpage") || id.equals("nextpage")) {
                // Seite aus CustomId lesen
                String pageStr = currentItem.getItemMeta()
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(main, "page"), PersistentDataType.STRING);
                if (pageStr == null) return;
                int targetPage = Integer.parseInt(pageStr);
                player.openInventory(Inventories.writeShopInventory(targetPage));

            } else if (returnItemPermission(currentItem)) {
                player.openInventory(Inventories.writeItemBuyInventory(currentItem));
            }
        }else if ("Buying Item...".equalsIgnoreCase(event.getView().getTitle())){
            event.setCancelled(true);
            if (!hasLocalID(currentItem)) return;
            if (getLocalID(currentItem).equals("back")){
                player.openInventory(Inventories.writeShopInventory(0));
            }else if (getLocalID(currentItem).equals("cancel")){
                player.openInventory(Inventories.writeShopInventory(0));
            }else if (getLocalID(currentItem).equals("buy")){

                String offerID = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "offerid"), PersistentDataType.STRING);// holt die offerID aus customID (key = offerid)
                Main.getInstance().getLogger().warning("offerID: " + offerID); // <- Debug
                Main.getInstance().getLogger().warning("localID: " + getLocalID(currentItem));
                File offerFile = new File(Main.offerFolder, offerID + ".json"); //Holt die Offer Datei, Name = id im Item
                OfferData offerData = Main.getJsonManager().getJSON(offerFile, OfferData.class); // Returnt eine OfferData Klasse/Objekt

                if (!offerFile.exists()){
                    player.openInventory(Inventories.writeShopInventory(0));
                    player.sendMessage("§7[§eShop§7] §cItem already sold!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    return;
                }
                File playerFile = new File(Main.playerFolder, player.getUniqueId() + ".json");
                PlayerData playerData = Main.getJsonManager().getJSON(playerFile, PlayerData.class);

                if (playerData.getCoins() >= offerData.getPrice()){

                    ItemStack offerItem = offerData.getItem();
                    String itemName = PlainTextComponentSerializer.plainText().serialize(offerItem.effectiveName());

                    Main.getJsonManager().removeFile(Main.offerFolder, offerData.getID() + ".json");
                    playerData.setCoins(playerData.getCoins() - offerData.getPrice());
                    player.getInventory().addItem(offerData.getItem());
                    player.sendMessage("§7[§eShop§7] §7Item bought: §6" + itemName);
                    player.sendMessage("§7[§eShop§7] §7New Account balance: §e" + playerData.getCoins());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.openInventory(Inventories.writeShopInventory(0));

                    UUID uuid = offerData.getPlayerUUID();

                    File sellerFile = new File(Main.playerFolder, uuid + ".json");
                    PlayerData sellerData = Main.getJsonManager().getJSON(sellerFile, PlayerData.class);

                    sellerData.setCoins(sellerData.getCoins() + offerData.getPrice());

                    Player sellerPlayer = Bukkit.getPlayer(uuid);
                    if (sellerPlayer != null) {
                        sellerPlayer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        sellerPlayer.sendMessage("§7[§eShop§7] §7You sold an Item: §6" + itemName + " §e+" + offerData.getPrice());



                        sellerPlayer.sendMessage("§7[§eShop§7] §7New Account balance: §e" + sellerData.getCoins());
                    }

                }else{
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    player.sendMessage("§7[§eShop§7] §cNot enough Coins: §e" + playerData.getCoins() + "§f/§c" + offerData.getPrice());
                }
            }
        } else if ("Do you want to sell this item?".equalsIgnoreCase(event.getView().getTitle())) {
        event.setCancelled(true);
        if (!hasLocalID(currentItem)) return;

        if (getLocalID(currentItem).equals("back") || getLocalID(currentItem).equals("cancel")) {
            player.closeInventory();

        } else if (getLocalID(currentItem).equals("sell")) {

            String priceStr = currentItem.getItemMeta()
                    .getPersistentDataContainer()
                    .get(new NamespacedKey(main, "sellprice"), PersistentDataType.STRING);

            if (priceStr == null) return;
            int price = Integer.parseInt(priceStr);

            ItemStack sellItem = event.getView().getTopInventory().getItem(13);
            if (sellItem == null || sellItem.getType() == Material.AIR) {
                player.sendMessage("§cItem not found!");
                player.closeInventory();
                return;
            }

            int offerCounter = Main.getInstance().getConfig().getInt("JsonOfferCounter");
            OfferData data = new OfferData(offerCounter, player.getUniqueId(), player.getName(), price, sellItem);
            Main.getJsonManager().createJSON(data, offerCounter + ".json", Main.offerFolder);

            player.getInventory().removeItem(sellItem);

            player.closeInventory();
            player.sendMessage("§7[§eShop§7] §7Item listed for §e" + price + " §7Coins!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
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
