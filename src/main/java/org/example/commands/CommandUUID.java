package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandUUID implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public CommandUUID(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;

            checkPermission.showUUID(player);
        }

        return true;
    }
}