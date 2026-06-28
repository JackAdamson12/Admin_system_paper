package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;
import org.bukkit.Bukkit;
import java.util.HashSet;
import java.util.UUID;

public class CommandFreeze implements CommandExecutor
{
    private CheckPermission checkPermission;

    HashSet<UUID> freezePlayers = new HashSet<>();

    public CommandFreeze(CheckPermission checkPermission)
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
            return true;
        }

        if(args[0].equalsIgnoreCase("all"))
        {
            freezePlayers.clear();

            for(Player player1 :Bukkit.getOnlinePlayers())
            {
                freezePlayers.add(player1.getUniqueId());


            }

        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            return true;
        }


       if(freezePlayers.contains(target))
       {
           freezePlayers.remove(target);
           target.sendMessage("You unlock");
           player.sendMessage("You unlock " + target.getName() + "| id: " + target.getUniqueId());

       }

       if(!freezePlayers.contains(target))
       {
           freezePlayers.add(target.getUniqueId());
           target.sendMessage("You are lock: admin" + player.getName());
           player.sendMessage("You freeze player: " + target.getName());
       }


        return true;
    }
}
