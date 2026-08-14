package org.example;


import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.*;
import org.example.commands.commandRestriction.CommandDisableCmd;
import org.example.commands.commandRestriction.CommandEnableCmd;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.commands.commandTerminal.CommandReloadPlayerProfile;
import org.example.commands.guiInterface.CommandGuiAdmin;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.guiListener.GuiListener;
import org.example.commands.guiInterface.source.searchGui.SearchChatListener;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.moderationItems.CommandModItem;
import org.example.commands.moderationItems.GiveModeretionItems;
import org.example.commands.moderationItems.listener.ModerationBookListener;
import org.example.commands.muteManager.*;
import org.example.commands.punishmentManager.*;
import org.example.commands.roleCommand.CommandDemoteRole;
import org.example.commands.roleCommand.CommandPromoteRole;
import org.example.events.ChatEvent;
import org.example.events.FreezeEvent;
import org.example.events.GodModeEvent;
import org.example.events.VanishEvent;
import org.example.luckPerms.role.RoleManager;
import org.example.playerProfile.PlayerProfileManager;
import org.example.playerProfile.listener.PlayerProfileListener;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;
import org.example.commands.logsManager.CommandMuteLog;
import org.example.commands.logsManager.CommandHistory;


public final class Main extends JavaPlugin
{
    private CheckPermission checkPermission;
    private PunishmentLogManager punishmentLogManager;
    private GuiManager guiManager;
    private RoleManager roleManager;
    private PlayerProfileManager playerProfileManager;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        checkPermission = new CheckPermission(this);
        punishmentLogManager = new PunishmentLogManager(this);
        guiManager = new GuiManager(checkPermission,this);
        CommandRestrictionManager commandRestrictionManager = new CommandRestrictionManager(this);
        checkPermission.loadAdmins();

        playerProfileManager = new PlayerProfileManager(this);
        roleManager = new RoleManager(playerProfileManager);

        //Teleport
        TeleportUtils teleportUtils = new TeleportUtils();
        getCommand("tpall").setExecutor(new CommandTpAll(checkPermission, teleportUtils,commandRestrictionManager));
        getCommand("ttp").setExecutor(new CommandTpPlayerToPlayer(checkPermission, teleportUtils,commandRestrictionManager));
        getCommand("tphere").setExecutor(new CommandTpHere(checkPermission,teleportUtils,commandRestrictionManager));
        getCommand("tp").setExecutor(new CommandTp(checkPermission,teleportUtils,commandRestrictionManager));

        //Add or delete admins(first admin adding manual)
        getCommand("addadmin").setExecutor(new CommandAddAdmins(checkPermission,commandRestrictionManager));
        getCommand("rmadmin").setExecutor(new CommandRemoveAdmins(checkPermission,commandRestrictionManager));

        // id player and status player
        getCommand("uuid").setExecutor(new CommandUUID(checkPermission,commandRestrictionManager));
        getCommand("who").setExecutor(new CommandWho(checkPermission,commandRestrictionManager));

        //switch game mode, heal player, get flying mode
        getCommand("gm").setExecutor(new ChangeGameMode(checkPermission,commandRestrictionManager));
        getCommand("heal").setExecutor(new CommandHealth(checkPermission,commandRestrictionManager));
        getCommand("fly").setExecutor(new CommandFly(checkPermission,commandRestrictionManager));

        //admin utils
        CommandGodMode commandGodMode = new CommandGodMode(checkPermission,commandRestrictionManager);
        getCommand("god").setExecutor(commandGodMode);
        getServer().getPluginManager().registerEvents(new GodModeEvent(commandGodMode),this);
        CommandVanish commandVanish = new CommandVanish(this,checkPermission,commandRestrictionManager);
        getCommand("vanish").setExecutor(commandVanish);
        getServer().getPluginManager().registerEvents(new VanishEvent(commandVanish), this);
        getCommand("kick").setExecutor(new CommandKick(checkPermission,punishmentLogManager,commandRestrictionManager));
        //Spectate
        CommandSpectate commandSpectate = new CommandSpectate(checkPermission,this,commandRestrictionManager);
        getCommand("spectate").setExecutor(commandSpectate);
        getCommand("unspectate").setExecutor(new CommandUnspectate(checkPermission,this,commandSpectate,commandRestrictionManager));

        //time on world
        getCommand("settime").setExecutor(new CommandSetTime(checkPermission,commandRestrictionManager));


        //Freeze adn unfreeze player
        CommandFreeze commandFreeze = new CommandFreeze(checkPermission,commandRestrictionManager);
        getCommand("freeze").setExecutor(commandFreeze);
        getCommand("melt").setExecutor(new CommandMelt(checkPermission,commandFreeze,commandRestrictionManager));
        getServer().getPluginManager().registerEvents(new FreezeEvent(commandFreeze),this);

        //Inventory
        getCommand("invsee").setExecutor(new CommandInvsee(checkPermission,commandRestrictionManager));
        getCommand("ecsee").setExecutor(new CommandEcsee(checkPermission,commandRestrictionManager));
        getCommand("clear").setExecutor(new CommandClearInv(checkPermission,commandRestrictionManager));
        getCommand("clearall").setExecutor(new CommandClearAll(checkPermission,commandRestrictionManager));
        getCommand("give").setExecutor(new CommandGive(checkPermission,commandRestrictionManager));

        //Info
        getCommand("adminfo").setExecutor(new CommandAdminInfo(checkPermission, this,commandRestrictionManager));

        //Ban
        CommandBan commandBan = new CommandBan(checkPermission,punishmentLogManager,commandRestrictionManager);
        getCommand("ban").setExecutor(commandBan);
        getCommand("pardon").setExecutor(new CommandUnban(checkPermission,punishmentLogManager,commandRestrictionManager));
        getCommand("baninfo").setExecutor(new CommandBanInfo(checkPermission,commandRestrictionManager));
        getCommand("tempban").setExecutor(new CommandTempBan(checkPermission,punishmentLogManager,commandRestrictionManager));

        //Mute
        MuteManager muteManager = new MuteManager(this);
        CommandMute commandMute = new CommandMute(checkPermission,muteManager,punishmentLogManager,guiManager,commandRestrictionManager);
        getCommand("mute").setExecutor(commandMute);
        getCommand("tmute").setExecutor(new CommandTempMute(checkPermission,muteManager,punishmentLogManager,commandRestrictionManager));
        getCommand("unmute").setExecutor(new CommandUnmute(checkPermission,muteManager,punishmentLogManager,commandRestrictionManager));
        getCommand("muteinfo").setExecutor(new CommandMuteInfo(checkPermission, muteManager,commandRestrictionManager));
        getCommand("mutelog").setExecutor(new CommandMuteLog(checkPermission, punishmentLogManager,commandRestrictionManager));
        getServer().getPluginManager().registerEvents(new ChatEvent(muteManager),this);

        //PunishmentLogs
        getCommand("history").setExecutor(new CommandHistory(checkPermission, punishmentLogManager,commandRestrictionManager));


        // GUI
        getServer().getPluginManager().registerEvents(new GuiListener(guiManager,commandMute,commandBan,commandRestrictionManager), this);
        getServer().getPluginManager().registerEvents(new SearchChatListener(guiManager), this);
        getCommand("panel").setExecutor(new CommandGuiAdmin(checkPermission, guiManager,commandRestrictionManager));

        //ModerationItems
        GiveModeretionItems moderationItems = new GiveModeretionItems(this, guiManager);
        getServer().getPluginManager().registerEvents(moderationItems, this);
        getCommand("moditem").setExecutor(new CommandModItem(checkPermission,moderationItems,commandRestrictionManager));
        getServer().getPluginManager().registerEvents(new ModerationBookListener(guiManager, checkPermission), this);

        //Restriction
        getCommand("disable").setExecutor(new CommandDisableCmd(checkPermission,commandRestrictionManager));
        getCommand("enable").setExecutor(new CommandEnableCmd(checkPermission,commandRestrictionManager));

        //Role
        getServer().getPluginManager().registerEvents(new PlayerProfileListener(playerProfileManager),this);
        //test
        getCommand("promote").setExecutor(new CommandPromoteRole(checkPermission,playerProfileManager,roleManager));
        getCommand("demote").setExecutor(new CommandDemoteRole(checkPermission,playerProfileManager,roleManager));



        //TerminalCommands
        getCommand("reload").setExecutor(new CommandReloadPlayerProfile(playerProfileManager));





        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable()
    {

    }

}