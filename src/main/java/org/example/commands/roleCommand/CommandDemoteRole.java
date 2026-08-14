package org.example.commands.roleCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.luckPerms.role.RoleManager;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.utils.CheckPermission;

public class CommandDemoteRole implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final PlayerProfileManager playerProfileManager;
    private final RoleManager roleManager;

    public CommandDemoteRole(CheckPermission checkPermission, PlayerProfileManager playerProfileManager, RoleManager roleManager)
    {
        this.roleManager = roleManager;
        this.checkPermission = checkPermission;
        this.playerProfileManager = playerProfileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is not for console");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Use /demote + <player>");
            return true;
        }

        Player target_player = Bukkit.getPlayer(args[0]);

        if (target_player == null) {
            sender.sendMessage("Player is not found");
            return true;
        }

        PlayerProfile actor = playerProfileManager.getProfile(sender);
        PlayerProfile target = playerProfileManager.getProfile(target_player);

        boolean demoted = roleManager.demote(actor, target);

        if (!demoted)
        {
            sender.sendMessage(ChatColor.RED + "Demote failed.");

            if (!roleManager.canManage(actor, target))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to demote this player.");
                sender.sendMessage(ChatColor.GRAY + "Your role: " + ChatColor.BLUE + actor.getStaffRole());
                sender.sendMessage(ChatColor.GRAY + "Target role: " + ChatColor.BLUE + target.getStaffRole());

                return true;
            }

            if (target.getStaffRole() == StaffRole.PLAYER) {
                sender.sendMessage(ChatColor.RED + "This player already has the lowest possible role.");
                return true;
            }

            sender.sendMessage(ChatColor.RED + "The player could not be demoted.");
            return true;
        }

        sender.sendMessage("Demote " + target.getNickName());
        sender.sendMessage("New role: " + ChatColor.BLUE + target.getStaffRole());

        target_player.sendMessage("Your role was changed:");
        target_player.sendMessage("Your new actual role: " + ChatColor.BLUE + target.getStaffRole());

        return true;
    }
}