package de.pandarmy.pandaServer.player;

import de.pandarmy.pandaServer.Main;

import java.io.File;
import java.util.UUID;

public class PlayerData {
    private final String playerName;
    private final UUID uuid;
    private int coins;

    public PlayerData(String playerName, UUID uuid) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.coins = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
        File file = new File(Main.playerFolder, this.uuid + ".json");
        Main.getJsonManager().saveJSON(this, file);
    }

}