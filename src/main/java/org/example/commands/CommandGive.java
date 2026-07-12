package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.utils.CheckPermission;

public class CommandGive implements CommandExecutor
{
    CheckPermission checkPermission;


    public CommandGive(CheckPermission checkPermission)
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
            player.sendMessage("No permission");
            return true;
        }

        if(args.length < 3)
        {
            player.sendMessage(ChatColor.GRAY + "Use name items, not id. For example /give Player diamond 2");
            player.sendMessage(ChatColor.GRAY +"Use give + player + [items] + [quantity]");
            return true;
        }



        Player target = Bukkit.getPlayer(args[0]);

        Material item = Material.matchMaterial(args[1]);

        if(item == null)
        {
            player.sendMessage("Item not found");
            return true;
        }

        int value;

        try
        {
            value = Integer.parseInt(args[2]);

        } catch (NumberFormatException e)
        {
            player.sendMessage("Write correct quantity");
            return true;
        }

        ItemStack dropItem = ItemStack.of(item,value);
        target.getInventory().addItem(dropItem);

        player.sendMessage("You get item: " + args[1] + " quantity: " + args[2]);




        return true;
    }
}
