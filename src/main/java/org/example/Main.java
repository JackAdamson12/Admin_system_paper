package org.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.ChangeGameMode;
import org.example.commands.CommandFly;
import org.example.commands.CommandGodMode;
import org.example.commands.CommandHealth;
import org.example.events.GodModeEvent;

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


        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}