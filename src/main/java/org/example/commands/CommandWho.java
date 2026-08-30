package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.minePermissions.CheckPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

public class CommandWho implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final PlayerProfileManager playerProfileManager;


    public CommandWho(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager, PlayerProfileManager playerProfileManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.playerProfileManager = playerProfileManager;
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

        if(!(sender instanceof Player))
        {
            return true;
        }

        if(args.length == 0)
        {
            PlayerProfile playerProfile = playerProfileManager.getProfile(sender);

            if (playerProfile.getStaffRole() == StaffRole.PLAYER) {
                sender.sendMessage("Status: " + ChatColor.GRAY + playerProfile.getStaffRole().toString());
                return true;
            }
            if (playerProfile.getStaffRole() == StaffRole.HELPER) {
                sender.sendMessage("Status: " + ChatColor.GREEN + playerProfile.getStaffRole().toString());
                return true;
            }
            if (playerProfile.getStaffRole() == StaffRole.ADMIN) {
                sender.sendMessage("Status: " + ChatColor.AQUA + playerProfile.getStaffRole().toString());
                return true;
            }
            if (playerProfile.getStaffRole() == StaffRole.HEAD_ADMIN) {
                sender.sendMessage("Status: " + ChatColor.RED + playerProfile.getStaffRole().toString());
                return true;
            }

            sender.sendMessage("Status: " + ChatColor.MAGIC + ChatColor.DARK_AQUA + playerProfile.getStaffRole().toString());


            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerProfile playerProfile = playerProfileManager.getProfile(target);

        if (playerProfile.getStaffRole() == StaffRole.PLAYER) {
            sender.sendMessage("Status: " + ChatColor.GRAY + playerProfile.getStaffRole().toString());
            return true;
        }
        if (playerProfile.getStaffRole() == StaffRole.HELPER) {
            sender.sendMessage("Status: " + ChatColor.GREEN + playerProfile.getStaffRole().toString());
            return true;
        }
        if (playerProfile.getStaffRole() == StaffRole.ADMIN) {
            sender.sendMessage("Status: " + ChatColor.AQUA + playerProfile.getStaffRole().toString());
            return true;
        }
        if (playerProfile.getStaffRole() == StaffRole.HEAD_ADMIN) {
            sender.sendMessage("Status: " + ChatColor.RED + playerProfile.getStaffRole().toString());
            return true;
        }

        sender.sendMessage("Status: " + ChatColor.MAGIC + ChatColor.DARK_AQUA + playerProfile.getStaffRole().toString());

        return true;

    }
}