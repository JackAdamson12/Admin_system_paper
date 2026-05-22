package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeGameMode implements CommandExecutor
{


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

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

            if(args[0].equalsIgnoreCase("c"))
            {
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage("Game mode: Creative");
            }

            else if(args[0].equalsIgnoreCase("s"))
            {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage("Game mode: Survival");
            }

            else if(args[0].equalsIgnoreCase("a"))
            {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage("Game mode: Adventure");
            }

            else if(args[0].equalsIgnoreCase("sp"))
            {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage("Game mode: Spectator");
            }

            else
            {
                player.sendMessage("Unknown gamemode!");
            }
        }

        return true;

    }


}
