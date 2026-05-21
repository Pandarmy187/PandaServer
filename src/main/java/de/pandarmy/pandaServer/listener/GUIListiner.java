package de.pandarmy.pandaServer.listener;

import de.pandarmy.pandaServer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class GUIListiner implements Listener {

    Main main = Main.getInstance();

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) return;

        if ("Shop".equalsIgnoreCase(event.getView().getTitle())){
            event.setCancelled(true);
        }
    }

    public boolean hasLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "id"));
    }

    public String getLocalID(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "id"), PersistentDataType.STRING);
    }
}
