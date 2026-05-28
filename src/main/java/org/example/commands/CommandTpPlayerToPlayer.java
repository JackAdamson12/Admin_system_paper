package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;

public class CommandTpPlayerToPlayer implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final TeleportUtils teleportUtils;

    public CommandTpPlayerToPlayer(CheckPermission checkPermission, TeleportUtils teleportUtils)
    {
        this.checkPermission = checkPermission;
        this.teleportUtils = teleportUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length < 2)
        {
            player.sendMessage("Use /tpp <player1> <player2>");
            return true;
        }

        Player target1 = Bukkit.getPlayer(args[0]);
        Player target2 = Bukkit.getPlayer(args[1]);

        if(target1 == null || target2 == null)
        {
            player.sendMessage("Player not found!");

            return true;
        }

        teleportUtils.teleportPlayerToPlayer(target1, target2);

        player.sendMessage(target1.getName() + " teleported to " + target2.getName());

        return true;
    }
}