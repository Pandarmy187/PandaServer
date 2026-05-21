package de.pandarmy.pandaServer.util;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Inventories {

    public static Inventory writeShopInventory(){
        Inventory inventory = Bukkit.createInventory(null, 6*9, "Shop");

        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").build());
        }

        for (int i = 45; i <= 53; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname(" ").setID("glass").build());
        }

        inventory.setItem(49, new ItemBuilder(Material.BARRIER).setDisplayname("§cback").setID("back").build());

        int counter = 9;
        List<OfferData> offers = Main.getJsonManager().getAllOffers();

        for (OfferData offer : offers){
            inventory.setItem(counter, offer.getItem());
            //Hier noch abfragen ob der Maximale Slot erreicht ist, also 44
            counter++;
        }
        return inventory;
    }
}
