package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.utils.CheckPermission;

import java.util.HashSet;
import java.util.UUID;

public class CommandVanish implements CommandExecutor
{
    public HashSet<UUID> vanishList = new HashSet<>();

    private final Main plugin;

    private final CheckPermission checkPermission;

    public CommandVanish(Main plugin, CheckPermission checkPermission)
    {
        this.plugin = plugin;
        this.checkPermission = checkPermission;
    }

    public Main getPlugin()
    {
        return plugin;
    }

    private void hide(Player player)
    {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayer != player)
            {
                onlinePlayer.hidePlayer(plugin, player);
            }
        }
    }

    private void show(Player player)
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
        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(sender))
        {
            player.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if(args.length == 0)
        {
            if(vanishList.contains(player.getUniqueId()))
            {
                vanishList.remove(player.getUniqueId());

                show(player);

                player.sendMessage(ChatColor.RED + "Vanish disabled!");

                return true;
            }

            vanishList.add(player.getUniqueId());

            hide(player);

            player.sendMessage(ChatColor.GREEN + "Vanish enabled!");

            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        if(vanishList.contains(target.getUniqueId()))
        {
            vanishList.remove(target.getUniqueId());

            show(target);

            player.sendMessage(ChatColor.RED + "Vanish disabled for " + target.getName());

            return true;
        }

        vanishList.add(target.getUniqueId());

        hide(target);

        player.sendMessage(ChatColor.GREEN + "Vanish enabled for " + target.getName());

        return true;
    }
}
