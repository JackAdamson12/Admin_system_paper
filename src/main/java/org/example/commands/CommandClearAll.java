package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandClearAll implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public CommandClearAll(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
    }

    private void clearAll(Player player)
    {
        player.getInventory().clear();
        player.getEnderChest().clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
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
            player.sendMessage("Use /clearall confirm or /clearall <player> confirm");
            return true;
        }

        if(args.length == 1)
        {
            if(!args[0].equalsIgnoreCase("confirm"))
            {
                player.sendMessage("Use /clearall confirm");
                return true;
            }

            clearAll(player);
            player.sendMessage("Your inventory and ender chest have been cleared!");

            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");
            return true;
        }

        if(args.length == 2)
        {
            if(!args[1].equalsIgnoreCase("confirm"))
            {
                player.sendMessage("Use /clearall " + target.getName() + " confirm");
                return true;
            }

            clearAll(target);

            player.sendMessage("You cleared inventory and ender chest of " + target.getName());
            target.sendMessage("Your inventory and ender chest were cleared by admin " + player.getName());

            return true;
        }

        player.sendMessage("Use /clearall confirm or /clearall <player> confirm");
        return true;
    }
}

