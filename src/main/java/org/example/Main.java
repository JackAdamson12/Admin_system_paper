package org.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.ChangeGameMode;
import org.example.commands.CommandFly;
import org.example.commands.CommandHealth;

public final class Main extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        getCommand("gm").setExecutor(new ChangeGameMode());
        getCommand("heal").setExecutor(new CommandHealth());
        getCommand("fly").setExecutor(new CommandFly());

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}