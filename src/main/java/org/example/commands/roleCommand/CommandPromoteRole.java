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
import org.example.minePermissions.CheckPermission;

public class CommandPromoteRole implements CommandExecutor
{
    private final PlayerProfileManager playerProfileManager;
    private final RoleManager roleManager;
    private final CheckPermission checkPermission;

    public CommandPromoteRole(CheckPermission checkPermission,PlayerProfileManager playerProfileManager, RoleManager roleManager)
    {
        this.checkPermission = checkPermission;
        this.roleManager = roleManager;
        this.playerProfileManager = playerProfileManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is not for console");
            return true;
        }

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            sender.sendMessage( "Use /promote + <player>");
            return true;
        }

        Player target_player = Bukkit.getPlayer(args[0]);
        if(target_player == null)
        {
            sender.sendMessage("Player is not found");
            return true;
        }

        PlayerProfile actor = playerProfileManager.getProfile(sender);
        PlayerProfile target = playerProfileManager.getProfile(target_player);


        boolean promote = roleManager.promote(actor, target);

        if(!promote)
        {
            sender.sendMessage(ChatColor.RED + "Promote failed.");

            if(!roleManager.canManage(actor, target))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to promote this player.");
                sender.sendMessage(ChatColor.GRAY + "Your role: " + ChatColor.BLUE + actor.getStaffRole());
                sender.sendMessage(ChatColor.GRAY + "Target role: " + ChatColor.BLUE + target.getStaffRole());

                return true;
            }

            int newLevel = target.getLevel() + 1;

            if(newLevel > StaffRole.OWNER.getLevel())
            {
                sender.sendMessage(ChatColor.RED + "This player already has the highest possible role.");
                return true;
            }

            StaffRole newStaffRole = StaffRole.getRoleByLevel(newLevel);

            if(!roleManager.canSetRole(actor, newStaffRole))
            {
                sender.sendMessage(ChatColor.RED + "You cannot promote this player to " + ChatColor.BLUE + newStaffRole);

                if(newStaffRole == StaffRole.OWNER)
                {
                    sender.sendMessage(ChatColor.GRAY + "The OWNER role cannot be assigned using /promote.");
                }

                return true;
            }

            sender.sendMessage(ChatColor.RED + "The player could not be promoted.");
            return true;
        }

        sender.sendMessage("Promote " + target.getNickName());
        sender.sendMessage("New role: " + ChatColor.BLUE + target.getStaffRole().toString());

        target_player.sendMessage("Your role was changed:");
        target_player.sendMessage("Your new actual role: " + ChatColor.BLUE + target.getStaffRole().toString());

        return true;
    }

}
