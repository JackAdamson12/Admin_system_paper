package org.example.commands.muteManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

import java.time.Instant;
import java.util.Arrays;

public class CommandMute implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final MuteManager muteManager;
    private final PunishmentLogManager punishmentLogManager;
    private final GuiManager guiManager;

    public CommandMute(CheckPermission checkPermission, MuteManager muteManager, PunishmentLogManager punishmentLogManager,GuiManager guiManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.punishmentLogManager = punishmentLogManager;
        this.checkPermission = checkPermission;
        this.muteManager = muteManager;
        this.guiManager = guiManager;
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

        if(args.length < 2)
        {
            sender.sendMessage("/mute <player> <reason>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        mutePlayer(sender, target, reason);

        return true;
    }

    private void muteFromGui(Player admin, Player target)
    {
            mutePlayer(admin, target, "Muted by admin!");
    }


    private void mutePlayer(CommandSender sender, OfflinePlayer target, String reason)
    {
        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player is not found");
            return;
        }

        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = "Unknown";
        }

        if(muteManager.isMuted(target.getUniqueId()))
        {
            sender.sendMessage("Player " + targetName + " is already muted.");
            return;
        }

        muteManager.mute(target.getUniqueId(), reason, sender.getName());

        sender.sendMessage("Player " + targetName + " has been muted. Reason: " + reason);

        Player online = target.getPlayer();

        if(online != null)
        {
            online.sendMessage("You have been muted. Reason: " + reason);
        }
        punishmentLogManager.saveLog(target, reason, sender, PunishmentType.MUTE, Instant.now(), null);
    }

    public void getMute(Player admin, Player target)
    {
        muteFromGui(admin,target);
    }
}
