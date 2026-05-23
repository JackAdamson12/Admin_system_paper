package org.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.*;
import org.example.events.GodModeEvent;
import org.example.events.VanishEvent;

public final class Main extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        getCommand("gm").setExecutor(new ChangeGameMode());

        getCommand("heal").setExecutor(new CommandHealth());

        getCommand("fly").setExecutor(new CommandFly());

        CommandGodMode commandGodMode = new CommandGodMode();
        getCommand("god").setExecutor(commandGodMode);

        getServer().getPluginManager().registerEvents(new GodModeEvent(commandGodMode),this);

        CommandVanish commandVanish = new CommandVanish(this);
        getCommand("vanish").setExecutor(commandVanish);

        getServer().getPluginManager().registerEvents(new VanishEvent(commandVanish), this);

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}