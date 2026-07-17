package org.example;


import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.*;
import org.example.commands.punishmentManager.*;
import org.example.events.FreezeEvent;
import org.example.events.GodModeEvent;
import org.example.events.VanishEvent;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;

import java.nio.file.Path;

public final class Main extends JavaPlugin
{
    private CheckPermission checkPermission;


    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        saveResource("adminfo.txt", false);

        checkPermission = new CheckPermission(this);
        checkPermission.loadAdmins();

        //Teleport
        TeleportUtils teleportUtils = new TeleportUtils();
        getCommand("tpall").setExecutor(new CommandTpAll(checkPermission, teleportUtils));
        getCommand("ttp").setExecutor(new CommandTpPlayerToPlayer(checkPermission, teleportUtils));
        getCommand("tphere").setExecutor(new CommandTpHere(checkPermission,teleportUtils));
        getCommand("tpall").setExecutor(new CommandTpAll(checkPermission,teleportUtils));
        getCommand("tp").setExecutor(new CommandTp(checkPermission,teleportUtils));

        //Add or delete admins(first admin adding manual)
        getCommand("addadmin").setExecutor(new CommandAddAdmins(checkPermission));
        getCommand("rmadmin").setExecutor(new CommandRemoveAdmins(checkPermission));

        // id player and status player
        getCommand("uuid").setExecutor(new CommandUUID(checkPermission));
        getCommand("who").setExecutor(new CommandWho(checkPermission));

        //switch game mode, heal player, get flying mode
        getCommand("gm").setExecutor(new ChangeGameMode(checkPermission));
        getCommand("heal").setExecutor(new CommandHealth(checkPermission));
        getCommand("fly").setExecutor(new CommandFly(checkPermission));

        //admin utils
        CommandGodMode commandGodMode = new CommandGodMode(checkPermission);
        getCommand("god").setExecutor(commandGodMode);
        getServer().getPluginManager().registerEvents(new GodModeEvent(commandGodMode),this);
        CommandVanish commandVanish = new CommandVanish(this,checkPermission);
        getCommand("vanish").setExecutor(commandVanish);
        getServer().getPluginManager().registerEvents(new VanishEvent(commandVanish), this);
        getCommand("kick").setExecutor(new CommandKick(checkPermission));
        //Spectate
        CommandSpectate commandSpectate = new CommandSpectate(checkPermission,this);
        getCommand("spectate").setExecutor(commandSpectate);
        getCommand("unspectate").setExecutor(new CommandUnspectate(checkPermission,this,commandSpectate));

        //time on world
        getCommand("settime").setExecutor(new CommandSetTime(checkPermission));


        //Freeze adn unfreeze player
        CommandFreeze commandFreeze = new CommandFreeze(checkPermission);
        getCommand("freeze").setExecutor(commandFreeze);
        getCommand("melt").setExecutor(new CommandMelt(checkPermission,commandFreeze));
        getServer().getPluginManager().registerEvents(new FreezeEvent(commandFreeze),this);

        //Inventory
        getCommand("invsee").setExecutor(new CommandInvsee(checkPermission));
        getCommand("ecsee").setExecutor(new CommandEcsee(checkPermission));
        getCommand("clear").setExecutor(new CommandClearInv(checkPermission));
        getCommand("clearall").setExecutor(new CommandClearAll(checkPermission));
        getCommand("give").setExecutor(new CommandGive(checkPermission));

        //Info
        getCommand("adminfo").setExecutor(new CommandAdminInfo(checkPermission, this));

        //Punishment
        getCommand("ban").setExecutor(new CommandBan(checkPermission));
        getCommand("pardon").setExecutor(new CommandUnban(checkPermission));
        getCommand("baninfo").setExecutor(new CommandBanInfo(checkPermission));
        getCommand("tempban").setExecutor(new CommandTempBan(checkPermission));





        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}