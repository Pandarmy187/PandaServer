package de.pandarmy.pandaServer.offer;

import org.bukkit.inventory.ItemStack;
import java.util.Base64;
import java.util.UUID;

public class OfferData {

    private final UUID playerUUID;
    private final String playerName;
    private final int price;
    private final String itemData;
    private final long timestamp;
    private final int id;

    public OfferData(int id, UUID playerUUID, String playerName, int price, ItemStack item) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.playerName = playerName;
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

    public String getPlayerName() {
        return playerName;
    }

    public int getID() {
        return id;
    }
}