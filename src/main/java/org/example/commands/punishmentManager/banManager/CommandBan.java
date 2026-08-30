package org.example.commands.punishmentManager.banManager;

import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class CommandBan implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final PunishmentLogManager punishmentLogManager;
    private final CheckPermission checkPermission;
    private final ProfileBanList banList;

    private String targetName;

    public CommandBan(CheckPermission checkPermission,PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
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

        if(args.length < 2)
        {
            sender.sendMessage("Use /ban <player> <reason>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        targetName = getTargetName(target, args[0]);

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        banPlayer(sender,target,reason);

        return true;
    }

    private void banPlayer(CommandSender sender, OfflinePlayer target, String reason)
    {
        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player has never joined this server.");
            return;
        }

        if(sender instanceof Player player && target.getUniqueId().equals(player.getUniqueId()))
        {
            sender.sendMessage("You cannot ban yourself.");
            return;
        }

        Player onlinePlayer = target.getPlayer();

        if(onlinePlayer != null)
        {
            if(checkPermission.checkIsAdmin(onlinePlayer))
            {
                sender.sendMessage("You cannot ban administrators.");
                return;
            }
        }

        if(target.isBanned())
        {
            long lastLogin = target.getLastLogin();

            String formattedDate = "Unknown";

            if(lastLogin != 0)
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

                formattedDate = Instant.ofEpochMilli(lastLogin).atZone(ZoneId.systemDefault()).format(formatter);
            }

            sender.sendMessage("Player " + targetName + " is already banned.\n" + "UUID: " + target.getUniqueId() + "\n" + "Last login: " + formattedDate);

            return;
        }


        banList.addBan(target.getPlayerProfile(), reason, (Instant) null, sender.getName());

        if(onlinePlayer != null)
        {
            onlinePlayer.kick(Component.text("You have been permanently banned.\n" + "Reason: " + reason + "\n" + "Administrator: " + sender.getName()));
        }

        punishmentLogManager.saveLog(target,reason,sender, PunishmentType.BAN,Instant.now(),null);

        sender.sendMessage("Player " + targetName + " has been banned.\n" + "UUID: " + target.getUniqueId() + "\n" + "Reason: " + reason);

    }

    private void banFromGui(CommandSender sender, OfflinePlayer target)
    {
        banPlayer(sender,target,"Banned by admin!");
    }

    public void getBan(Player admin,Player target)
    {
        banFromGui(admin,target);
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
