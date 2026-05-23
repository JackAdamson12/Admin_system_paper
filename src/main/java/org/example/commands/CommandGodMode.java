package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;


public class CommandGodMode implements CommandExecutor
{

    public HashSet<Player> godList = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if(args.length == 0)
        {
            Player player = (Player) sender;

            if(godList.contains(player))
            {
                godList.remove(player);
                player.sendMessage("God mode disabled!");
                return true;
            }

            godList.add(player);
            player.sendMessage("God mode enabled!");
            return true;
        }


        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            sender.sendMessage("Player is not found!\n");
            return true;
        }
        else
        {
            godList.add(target);
        }

        return true;
    }
}
