package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;
import java.util.HashSet;
import java.util.UUID;

public class CommandFreeze implements CommandExecutor
{


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

                {
            }

        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            return true;
        }

        {
        }

        freezePlayers.add(target.getUniqueId());


        return true;
    }
}
