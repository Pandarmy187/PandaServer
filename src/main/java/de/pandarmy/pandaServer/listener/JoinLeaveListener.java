package de.pandarmy.pandaServer.listener;

import de.pandarmy.pandaServer.Main;
import de.pandarmy.pandaServer.offer.JsonManager;
import de.pandarmy.pandaServer.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage("§1§l> §e" + event.getPlayer().getName());

        Player player = (Player) event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Main.getJsonManager().createJSON(new PlayerData(player.getName(), playerUUID, System.currentTimeMillis()), playerUUID + ".json", Main.playerFolder);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage("§4§l< §e" + event.getPlayer().getName());
    }
}
