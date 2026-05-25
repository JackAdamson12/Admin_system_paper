package org.example;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.*;
import org.example.events.GodModeEvent;
import org.example.events.VanishEvent;
import org.example.utils.CheckPermission;

public final class Main extends JavaPlugin
{
    private CheckPermission checkPermission;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        checkPermission = new CheckPermission(this);

        checkPermission.loadAdmins();

        getCommand("UUID").setExecutor(new CommandUUID(checkPermission));

        getCommand("who").setExecutor(new CommandWho(checkPermission));

        getCommand("gm").setExecutor(new ChangeGameMode(checkPermission));

        getCommand("heal").setExecutor(new CommandHealth(checkPermission));

        getCommand("fly").setExecutor(new CommandFly(checkPermission));

        CommandGodMode commandGodMode = new CommandGodMode(checkPermission);
        getCommand("god").setExecutor(commandGodMode);

        getServer().getPluginManager().registerEvents(new GodModeEvent(commandGodMode),this);

        CommandVanish commandVanish = new CommandVanish(this,checkPermission);
        getCommand("vanish").setExecutor(commandVanish);

        getServer().getPluginManager().registerEvents(new VanishEvent(commandVanish), this);

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}