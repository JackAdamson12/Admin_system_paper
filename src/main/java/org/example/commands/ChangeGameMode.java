package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class ChangeGameMode implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public ChangeGameMode(CheckPermission checkPermission)
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

        if(!checkPermission.checkIsAdmin(sender))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /gm <c/s/a/sp> [player]");
            return true;
        }

        Player target = player;

        if(args.length == 2)
        {
            target = Bukkit.getPlayer(args[1]);

            if(target == null)
            {
                player.sendMessage("Player not found!");
                return true;
            }
        }

        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("c"))
            {
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage("Gamemode: Creative");
                player.sendMessage("You change " + target.getName() + " gamemode to creative");
                return true;
            }
            else if(args[0].equalsIgnoreCase("s"))
            {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage("Gamemode: Survival");
                player.sendMessage("You change " + target.getName() + " gamemode to survival");
                return true;
            }
            else if(args[0].equalsIgnoreCase("a"))
            {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage("Gamemode: Adventure");
                player.sendMessage("You change " + target.getName() + " gamemode to adventure");
                return true;
            }
            else if(args[0].equalsIgnoreCase("sp"))
            {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage("Gamemode: Spectator");
                player.sendMessage("You change " + target.getName() + " gamemode to Spectator");
                return true;
            }
            else
            {
                player.sendMessage("Unknown gamemode!");
            }
        }

        if(args[0].equalsIgnoreCase("c"))
        {
            target.setGameMode(GameMode.CREATIVE);
            target.sendMessage("Gamemode: Creative");
            return true;
        }
        else if(args[0].equalsIgnoreCase("s"))
        {
            target.setGameMode(GameMode.SURVIVAL);
            target.sendMessage("Gamemode: Survival");
            return true;
        }
        else if(args[0].equalsIgnoreCase("a"))
        {
            target.setGameMode(GameMode.ADVENTURE);
            target.sendMessage("Gamemode: Adventure");
            return true;
        }
        else if(args[0].equalsIgnoreCase("sp"))
        {
            target.setGameMode(GameMode.SPECTATOR);
            target.sendMessage("Gamemode: Spectator");
            return true;
        }
        else
        {
            player.sendMessage("Unknown gamemode!");
        }



        return true;
    }
}
