package de.pandarmy.pandaServer.util;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.OfferData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Inventories {

// Inventories.java

    public static Inventory writeShopInventory(int page) {
        Inventory inventory = Bukkit.createInventory(null, 6*9, "Shop");

        for (int i = 0; i <= 8; i++)
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayname(" ").setID("glass").setHideTooltips(true).build());

        for (int i = 45; i <= 53; i++)
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayname(" ").setID("glass").setHideTooltips(true).build());

        inventory.setItem(49, new ItemBuilder(Material.BARRIER)
                .setDisplayname("§cBack")
                .setID("back")
                .build());

        List<OfferData> allOffers = Main.getJsonManager().getAllEntries(Main.offerFolder, OfferData.class);

        int itemsPerPage = 36;
        int totalPages = (int) Math.ceil((double) allOffers.size() / itemsPerPage);
        if (totalPages == 0) totalPages = 1;
        page = Math.max(0, Math.min(page, totalPages - 1));

        int from = page * itemsPerPage;
        int to = Math.min(allOffers.size(), from + itemsPerPage);
        List<OfferData> pageOffers = allOffers.subList(from, to);

        if (page > 0) {
            inventory.setItem(47, new ItemBuilder(Material.ARROW)
                    .setDisplayname("§ePage " + page + " §8← §ePage " + (page + 1))
                    .setCustomId("page", String.valueOf(page - 1))
                    .setID("prevpage")
                    .build());
        }

        if (page < totalPages - 1) {
            inventory.setItem(51, new ItemBuilder(Material.ARROW)
                    .setDisplayname("§ePage " + (page + 1) + " §8→ §ePage " + (page + 2))
                    .setCustomId("page", String.valueOf(page + 1))
                    .setID("nextpage")
                    .build());
        }

        int slot = 9;
        for (OfferData offer : pageOffers) {
            ItemStack item = new ItemBuilder(Material.COMPASS)
                    .setItem(offer.getItem())
                    .setLore("", "§8Price: §e" + offer.getPrice(),
                            "§8Seller: §3" + offer.getPlayerName(), "",
                            "§8Date: §9" + formatUnixTimestamp(offer.getTimestamp()))
                    .setID(String.valueOf(offer.getID()))
                    .build();
            inventory.setItem(slot, item);
            slot++;
            if (slot == 45) break;
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

        inventory.setItem(49, new ItemBuilder(Material.BARRIER).setDisplayname("§cBack").setID("back").build());
        inventory.setItem(13, new ItemBuilder(Material.COMPASS).setItem(item).build());
        inventory.setItem(33, new ItemBuilder(Material.LIME_CONCRETE).setDisplayname("§eBuy").setCustomId("offerid", getLocalID(item)).setID("buy").build());
        inventory.setItem(29, new ItemBuilder(Material.RED_CONCRETE).setDisplayname("§eCancel").setID("cancel").build());

        return inventory;
    }

    public static Inventory writeSellInventory(ItemStack item, int price) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Do you want to sell this item?");

        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }
        for (int i = 45; i <= 53; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayname(" ").setID("glass").setHideTooltips(true).build());
        }

        inventory.setItem(49, new ItemBuilder(Material.BARRIER)
                .setDisplayname("§cBack").setID("back").build());

        inventory.setItem(13, item.clone());

        // Confirm-Button: Preis als CustomData speichern
        inventory.setItem(33, new ItemBuilder(Material.LIME_CONCRETE)
                .setDisplayname("§7Sell for §e" + price + " Coins")
                .setCustomId("sellprice", String.valueOf(price))
                .setID("sell")
                .build());

        inventory.setItem(29, new ItemBuilder(Material.RED_CONCRETE)
                .setDisplayname("§cCancel").setID("cancel").build());

        return inventory;
    }

    public static String getLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
    }


    public static String formatUnixTimestamp(long unixTimestamp) {
        Instant instant = Instant.ofEpochMilli(unixTimestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
