package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements  CommandExecutor
{

    Boolean toggleFly(Player player)
    {
        if(!player.getAllowFlight())
        {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("Fly enabled!");
            return true;
        }

        player.setAllowFlight(false);
        player.setFlying(true);
        player.sendMessage("Fly disabled!");

        return false;
    }

    @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof  Player)
        {
            Player player = (Player) sender;


            if(args.length == 0)
            {
                toggleFly(player);

                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null)
            {
                player.sendMessage(ChatColor.RED + "Player not found!");

                return true;
            }
            else
            {
                toggleFly(target);
            }

        }
        return true;
    }
}
