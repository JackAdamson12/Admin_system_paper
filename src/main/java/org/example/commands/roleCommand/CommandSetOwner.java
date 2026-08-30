package org.example.commands.roleCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.minePermissions.CheckPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

public class CommandSetOwner implements CommandExecutor
{
    private final PlayerProfileManager playerProfileManager;

    public CommandSetOwner(PlayerProfileManager playerProfileManager)
    {
        this.playerProfileManager = playerProfileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            sender.sendMessage("This command is only for console.");
            return true;
        }

        if(args.length < 1)
        {
            sender.sendMessage("Use: /setowner <player>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if(targetPlayer == null)
        {
            sender.sendMessage("Player isn't found.");
            return true;
        }

        PlayerProfile targetProfile =
                playerProfileManager.getProfile(targetPlayer);

        if(targetProfile == null)
        {
            sender.sendMessage("Player profile isn't found.");
            return true;
        }

        if(targetProfile.getStaffRole() == StaffRole.OWNER)
        {
            sender.sendMessage(targetPlayer.getName() + " is already OWNER.");
            return true;
        }

        targetProfile.setStaffRole(StaffRole.OWNER);
        playerProfileManager.saveProfile(targetProfile);

        sender.sendMessage(targetPlayer.getName() + " is now OWNER.");
        targetPlayer.sendMessage("Your role has been changed to OWNER.");

        return true;
    }
}
