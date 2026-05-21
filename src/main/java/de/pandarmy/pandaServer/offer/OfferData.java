package de.pandarmy.pandaServer.offer;

import org.bukkit.inventory.ItemStack;
import java.util.Base64;
import java.util.UUID;

public class OfferData {

    private final UUID playerUUID;
    private final int price;
    private final String itemData;
    private final long timestamp;

    public OfferData(UUID playerUUID, int price, ItemStack item) {
        this.playerUUID = playerUUID;
        this.price = price;
        this.itemData = Base64.getEncoder().encodeToString(item.serializeAsBytes());
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(itemData));
    }

    public long getTimestamp() {
        return timestamp;
    }
}