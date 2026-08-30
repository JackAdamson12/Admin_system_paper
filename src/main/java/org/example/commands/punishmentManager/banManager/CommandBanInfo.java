package org.example.commands.punishmentManager.banManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommandBanInfo implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final ProfileBanList banList;
    private final DateTimeFormatter formatter;

    public CommandBanInfo(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.banList = Bukkit.getBanList(BanListType.PROFILE);
        this.formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
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
            sender.sendMessage("Use /baninfo <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        BanEntry<?> banEntry =
                banList.getBanEntry(target.getPlayerProfile());

        if(banEntry == null)
        {
            sender.sendMessage("Player " + getTargetName(target, args[0]) + " is not banned.");

            return true;
        }

        String reason = banEntry.getReason();

        if(reason == null)
        {
            reason = "Not specified";
        }

        String source = banEntry.getSource();

        String createdDate = formatDate(
                banEntry.getCreated()
        );

        String expirationDate = "Permanent";

        Date expiration = banEntry.getExpiration();

        if(expiration != null)
        {
            expirationDate = formatDate(expiration);
        }

        sender.sendMessage("----- Ban information -----\n" +
                        "Player: " + getTargetName(target, args[0]) + "\n" +
                        "UUID: " + target.getUniqueId() + "\n" +
                        "Reason: " + reason + "\n" +
                        "Administrator: " + source + "\n" +
                        "Created: " + createdDate + "\n" +
                        "Expires: " + expirationDate);

        return true;
    }

    private String formatDate(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).format(formatter);
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