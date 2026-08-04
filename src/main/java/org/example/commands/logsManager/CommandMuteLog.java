package org.example.commands.logsManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentData;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

import java.util.List;

public class CommandMuteLog implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final PunishmentLogManager punishmentLogManager;

    public CommandMuteLog(CheckPermission checkPermission, PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.punishmentLogManager = punishmentLogManager;
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

        if(!checkPermission.checkIsAdmin(sender))
        {
            sender.sendMessage("No permissions!");
            return true;
        }

        if(args.length < 1)
        {
            sender.sendMessage("Usage: /mutelog <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player " + args[0] + " has never joined the server.");
            return true;
        }

        List<PunishmentData> history = punishmentLogManager.getPunishmentLog(target.getUniqueId());

        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = args[0];
        }

        boolean foundMuteLog = false;

        sender.sendMessage("Mute history of " + targetName + ":");

        for(PunishmentData data : history)
        {
            if(data.punishment() != PunishmentType.MUTE && data.punishment() != PunishmentType.TEMP_MUTE && data.punishment() != PunishmentType.UNMUTE)
            {
                continue;
            }

            foundMuteLog = true;

            sender.sendMessage("--------------------");
            sender.sendMessage("Type: " + data.punishment());
            sender.sendMessage("Reason: " + data.reason());
            sender.sendMessage("Moderator: " + data.moderator());
            sender.sendMessage("Created: " + data.createdAt());

            if(data.expiresAt() == null)
            {
                sender.sendMessage("Expires: Never");
            }
            else
            {
                sender.sendMessage("Expires: " + data.expiresAt());
            }
        }

        if(!foundMuteLog)
        {
            sender.sendMessage("Mute history is empty.");
        }

        return true;
    }
}