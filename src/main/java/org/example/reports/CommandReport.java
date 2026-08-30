package org.example.reports;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.minePermissions.CheckPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.reports.sourse.ReportManager;
import org.example.reports.sourse.data.ReportStruct;

import java.time.LocalDateTime;

public class CommandReport implements CommandExecutor
{

    private final ReportManager reportManager;
    private final CheckPermission checkPermission;
    private final PlayerProfileManager playerProfileManager;

    public CommandReport(ReportManager reportManager, CheckPermission checkPermission, PlayerProfileManager playerProfileManager)
    {
        this.checkPermission = checkPermission;
        this.reportManager = reportManager;
        this.playerProfileManager = playerProfileManager;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command not for console");
            return true;
        }

        if(!checkPermission.checkPermission(sender, command.getName()))
        {
            sender.sendMessage("No permissions");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null)
        {
            sender.sendMessage("Player not found");
            return true;
        }

        PlayerProfile suspect = playerProfileManager.getProfile(target);
        PlayerProfile reporter = playerProfileManager.getProfile(sender);

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        if(reason.isBlank())
        {
            sender.sendMessage("Write reason");
            return true;
        }

        ReportStruct reportStruct = new ReportStruct(reporter,suspect,reason, LocalDateTime.now());

        reportManager.createReport(reportStruct);
        sender.sendMessage(ChatColor.YELLOW + "Report is sending. Thank you!");


        return true;
    }
}
