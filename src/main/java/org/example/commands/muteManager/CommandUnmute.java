package org.example.commands.muteManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

import java.time.Instant;

public class CommandUnmute implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final MuteManager muteManager;
    private final PunishmentLogManager punishmentLogManager;

    public CommandUnmute(CheckPermission checkPermission, MuteManager muteManager, PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.punishmentLogManager = punishmentLogManager;
        this.checkPermission = checkPermission;
        this.muteManager = muteManager;
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

        if(args.length == 0)
        {
            sender.sendMessage("Use /unmute <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player is not found");
            return true;
        }

        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = args[0];
        }

        if(!muteManager.isMuted(target.getUniqueId()))
        {
            sender.sendMessage("Player: " + targetName + " is not muted");
            return true;
        }

        muteManager.unmute(target.getUniqueId());
        sender.sendMessage("Player: " + targetName + " has been unmuted!");

        Player onlineTarget = target.getPlayer();

        if(onlineTarget != null)
        {
            onlineTarget.sendMessage("You are unmuted. Moderator: " + sender.getName());
        }

        punishmentLogManager.saveLog(target, "No reason", sender, PunishmentType.UNMUTE, Instant.now(), null);

        return true;
    }
}
