package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;

public class CommandTp implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final TeleportUtils teleportUtils;

    public CommandTp (CheckPermission checkPermission, TeleportUtils teleportUtils)
    {
        this.teleportUtils = teleportUtils;
        this.checkPermission = checkPermission;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(sender))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /tp <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");
            return true;
        }

        teleportUtils.teleportTo(player,target);

        return true;
    }
}
