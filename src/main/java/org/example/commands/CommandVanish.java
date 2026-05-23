package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;

import java.util.HashSet;

public class CommandVanish implements CommandExecutor
{
    public HashSet<Player> vanishList = new HashSet<>();

    private final Main plugin;

    public CommandVanish(Main plugin)
    {
        this.plugin = plugin;
    }

    public Main getPlugin()
    {
        return plugin;
    }

    void hide(Player player)
    {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayer != player)
            {
                onlinePlayer.hidePlayer(plugin, player);
            }
        }
    }

    void show(Player player)
    {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayer != player)
            {
                onlinePlayer.showPlayer(plugin, player);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;

            if(args.length == 0)
            {
                if(vanishList.contains(player))
                {
                    vanishList.remove(player);

                    show(player);

                    player.sendMessage("Vanish disabled!");

                    return true;
                }

                vanishList.add(player);

                hide(player);

                player.sendMessage("Vanish enabled!");

                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null)
            {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            if(vanishList.contains(target))
            {
                vanishList.remove(target);

                show(target);

                player.sendMessage("Vanish disabled!");

                return true;
            }
            else
            {
                vanishList.add(target);

                hide(target);

                player.sendMessage("Vanish enabled!");

                return true;

            }




        }

        return true;
    }
}