package de.pandarmy.pandaServer;

import de.pandarmy.pandaServer.commands.PingCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("ping").setExecutor(new PingCommandExecutor());
    }

    @Override
    public void onDisable() {
    }
}
