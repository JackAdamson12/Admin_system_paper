package org.example.commands.punishmentManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.utils.CheckPermission;

import java.time.Instant;
import java.util.Arrays;

public class CommandKick implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final PunishmentLogManager punishmentLogManager;

    public CommandKick(CheckPermission checkPermission, PunishmentLogManager punishmentLogManager)
    {
        this.punishmentLogManager = punishmentLogManager;
        this.checkPermission = checkPermission;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {


        if(!checkPermission.checkIsAdmin(sender))
        {
            sender.sendMessage("No permissions.");
            return true;
        }

        if(args.length == 0)
        {
            sender.sendMessage("Use /kick <player> <reason>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            sender.sendMessage("Player not found.");
            return true;
        }

        String reason = "Kicked by admin.";

        if(args.length >= 2)
        {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        target.kickPlayer(reason);

        punishmentLogManager.saveLog(target,reason,sender, PunishmentType.KICK, Instant.now(),null);
        sender.sendMessage("You kicked " + target.getName() + ". Reason: " + reason);

        return true;
    }

}
