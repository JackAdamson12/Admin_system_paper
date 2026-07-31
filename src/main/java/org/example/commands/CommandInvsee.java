package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandInvsee implements CommandExecutor
{

    private CheckPermission checkPermission;

    public CommandInvsee(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
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

        if(args.length == 0)
        {
            player.sendMessage("Use /invsee + player");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null)
        {
            player.sendMessage("Player not found.");
            return true;
        }

        player.openInventory(target.getInventory());





        return true;
    }
}
