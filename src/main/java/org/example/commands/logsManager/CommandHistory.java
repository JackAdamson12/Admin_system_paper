package org.example.commands.logsManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.punishmentLogData.PunishmentData;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class CommandHistory implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final PunishmentLogManager punishmentLogManager;

    public CommandHistory(CheckPermission checkPermission, PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
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

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        if(args.length < 1)
        {
            sender.sendMessage("Use /history <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        List<PunishmentData> history = punishmentLogManager.getPunishmentLog(target.getUniqueId());

        if(history.isEmpty())
        {
            sender.sendMessage("Punishment history is empty.");
            return true;
        }

        history.sort(Comparator.comparing(PunishmentData::createdAt).reversed());

        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = args[0];
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

        sender.sendMessage("Punishment history of " + targetName + ":");

        for(PunishmentData data : history)
        {
            sender.sendMessage("--------------------");
            sender.sendMessage("Type: " + data.punishment());
            sender.sendMessage("Reason: " + getValue(data.reason()));
            sender.sendMessage("Moderator: " + getValue(data.moderator()));
            sender.sendMessage("Created: " + formatter.format(data.createdAt()));

            if(data.expiresAt() == null)
            {
                sender.sendMessage("Expires: Never");
            }
            else
            {
                sender.sendMessage("Expires: " + formatter.format(data.expiresAt()));
            }
        }

        return true;
    }

    private String getValue(String value)
    {
        if(value == null || value.isBlank())
        {
            return "Unknown";
        }

        return value;
    }
}