package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.muteManager.MuteManager;
import org.example.utils.CheckPermission;

public class CommandUnmute implements CommandExecutor
{
    private CheckPermission checkPermission;
    private MuteManager muteManager;

    public CommandUnmute(CheckPermission checkPermission, MuteManager muteManager)
    {
        this.checkPermission = checkPermission;
        this.muteManager = muteManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if (!checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("No permissions!");
            return true;
        }
        if(args.length == 0)
        {
            player.sendMessage("Use /unmute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player is not found");
            return true;
        }

        if(!muteManager.isMuted(target.getUniqueId()))
        {
            player.sendMessage("Player: " + target.getName() + " is not muted");
            return true;
        }
        muteManager.unmute(target.getUniqueId());

        player.sendMessage("Player: " + target.getName()+ " is already unmuted!");
        target.sendMessage("You are unmuted. Moderator: " + player.getName());
        return true;

    }
}
