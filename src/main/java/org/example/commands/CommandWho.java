package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandWho implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public CommandWho(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;

            if(checkPermission.checkIsAdmin(player))
            {
                player.sendMessage("Status: Administrator");
                return true;
            }

            player.sendMessage("Status: Player");
        }

        return true;
    }
}