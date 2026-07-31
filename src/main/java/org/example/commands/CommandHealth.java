package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import org.example.utils.CheckPermission;

public class CommandHealth implements CommandExecutor
{

    private final CheckPermission checkPermission;

    public CommandHealth(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
    }

    void healPlayer(Player player)
    {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.sendMessage(ChatColor.GREEN + player.getName()+ " you healed!!\n");
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
                healPlayer(player);

                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null)
            {
                player.sendMessage(ChatColor.RED + "Player not found!");

                return true;
            }

            healPlayer(target);

            player.sendMessage(ChatColor.GREEN + "You healed " + target.getName());


        return true;
    }

}
