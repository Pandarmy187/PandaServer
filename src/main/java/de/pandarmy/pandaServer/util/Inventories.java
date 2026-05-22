package de.pandarmy.pandaServer.util;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Inventories {

    public static Inventory writeShopInventory(){
        Inventory inventory = Bukkit.createInventory(null, 6*9, "Shop");

        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }

        for (int i = 45; i <= 53; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }

        inventory.setItem(49, new ItemBuilder(Material.BARRIER).setDisplayname("§cback").setID("back").build());

        int counter = 9;
        List<OfferData> offers = Main.getJsonManager().getAllEntries(Main.offerFolder, OfferData.class);

        for (OfferData offer : offers){
            ItemStack item = new ItemBuilder(Material.COMPASS).setItem(offer.getItem()).setLore("", "§ePrice: §a" + offer.getPrice(), "", "§5Seller: " + offer.getPlayerName()).setID(String.valueOf(offer.getID())).build();


            inventory.setItem(counter, item);
            if (counter == 44) continue;
            counter++;
        }
        return inventory;
    }

    public static Inventory writeItemBuyInventory(ItemStack item){
        Inventory inventory = Bukkit.createInventory(null, 6*9, "Buying Item...");

        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }

        for (int i = 45; i <= 53; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }

        inventory.setItem(49, new ItemBuilder(Material.BARRIER).setDisplayname("§cback").setID("back").build());
        inventory.setItem(13, new ItemBuilder(Material.COMPASS).setItem(item).build());
        inventory.setItem(33, new ItemBuilder(Material.LIME_CONCRETE).setDisplayname("§eBuy").setCustomId("offerid", getLocalID(item)).setID("buy").build());
        inventory.setItem(29, new ItemBuilder(Material.RED_CONCRETE).setDisplayname("§eCancel").setID("cancel").build());

        return inventory;
    }

    public static String getLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
    }

}
