package org.example;


import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.*;
import org.example.events.GodModeEvent;
import org.example.events.VanishEvent;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;

public final class Main extends JavaPlugin
{
    private CheckPermission checkPermission;


    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        checkPermission = new CheckPermission(this);
        TeleportUtils teleportUtils = new TeleportUtils();

        checkPermission.loadAdmins();

        getCommand("tpall").setExecutor(new CommandTpAll(checkPermission, teleportUtils));

        getCommand("ttp").setExecutor(new CommandTpPlayerToPlayer(checkPermission, teleportUtils));

        getCommand("tphere").setExecutor(new CommandTpHere(checkPermission,teleportUtils));

        getCommand("tpall").setExecutor(new CommandTpAll(checkPermission,teleportUtils));

        getCommand("tp").setExecutor(new CommandTp(checkPermission,teleportUtils));

        getCommand("addadmin").setExecutor(new CommandAddAdmins(checkPermission));

        getCommand("rmadmin").setExecutor(new CommandRemoveAdmins(checkPermission));

        getCommand("uuid").setExecutor(new CommandUUID(checkPermission));

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

        getCommand("settime").setExecutor(new CommandSetTime(checkPermission));


        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}