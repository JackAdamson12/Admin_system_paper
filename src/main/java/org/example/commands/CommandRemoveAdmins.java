package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandRemoveAdmins implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public CommandRemoveAdmins(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0)
        {
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            return true;
        }

        checkPermission.removeAdmin(player, target);
        checkPermission.saveAdmin();
        if(checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("Admin removed: " + target.getName());

        }

        return true;
    }
}
