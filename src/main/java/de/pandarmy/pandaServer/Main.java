package de.pandarmy.pandaServer;

import de.pandarmy.pandaServer.commands.PingCommandExecutor;
import de.pandarmy.pandaServer.commands.ShopCommandExecutor;
import de.pandarmy.pandaServer.commands.TestCommandExecutor;
import de.pandarmy.pandaServer.listener.GUIListiner;
import de.pandarmy.pandaServer.offer.JsonManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    private static Main Instance;
    private static JsonManager jsonManager;
    public static File offerFolder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Instance = this;

        getCommand("ping").setExecutor(new PingCommandExecutor());
        getCommand("test").setExecutor(new TestCommandExecutor());
        getCommand("shop").setExecutor(new ShopCommandExecutor());

        getServer().getPluginManager().registerEvents(new GUIListiner(), this);

        offerFolder = new File(getDataFolder(), "offerFolder");
        if (!offerFolder.exists()){
            offerFolder.mkdir();
        }
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return Instance;
    }

    public static JsonManager getJsonManager() {
        if (jsonManager == null){
            jsonManager = new JsonManager();
        }
        return jsonManager;
    }
}
