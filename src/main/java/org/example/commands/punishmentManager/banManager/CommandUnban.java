package org.example.commands.punishmentManager.banManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

import java.time.Instant;

public class CommandUnban implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final ProfileBanList banList;
    private final PunishmentLogManager punishmentLogManager;

    public CommandUnban(CheckPermission checkPermission, PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.punishmentLogManager = punishmentLogManager;
        this.banList = Bukkit.getBanList(BanListType.PROFILE);
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player restrictionPlayer = null;

        if(sender instanceof Player)
        {
            restrictionPlayer = (Player) sender;
        }

        if(commandRestrictionManager.isDisabled(command.getName(), restrictionPlayer))
        {
            sender.sendMessage("This command is disabled.");
            return true;
        }



        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            sender.sendMessage("Use /pardon <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player has never joined this server.");
            return true;
        }

        if(!target.isBanned())
        {
            sender.sendMessage("Player " + getTargetName(target, args[0]) + " is not banned.");

            return true;
        }

        banList.pardon(target.getPlayerProfile());


        sender.sendMessage("Player " + getTargetName(target, args[0]) + " has been unbanned.\n" + "UUID: " + target.getUniqueId());
        punishmentLogManager.saveLog(target,"no reason",sender, PunishmentType.UNBAN, Instant.now(), null);

        return true;
    }

    private String getTargetName(OfflinePlayer target, String argument)
    {
        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = argument;
        }

        return targetName;
    }
}