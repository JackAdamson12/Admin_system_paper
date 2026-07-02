package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

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
        player.setFlying(false);
        player.sendMessage("Fly disabled!");

        return false;
    }

    private final CheckPermission checkPermission;

    public CommandFly(CheckPermission checkPermission)
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
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0 )
        {

            if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR )
            {
                sender.sendMessage("You have active fly in creative/spectator game mode\n");
                return true;
            }
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
            if(target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR )
            {
                sender.sendMessage("You have active fly in creative/spectator game mode\n");
                return true;
            }
            toggleFly(target);
        }

        return true;

    }

}

