package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

import java.util.HashMap;
import java.util.UUID;

public class CommandSpectate implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final Main plugin;

    private final HashMap<UUID, Location> oldPositions = new HashMap<>();
    private final HashMap<UUID, GameMode> oldGameModes = new HashMap<>();

    public CommandSpectate(CheckPermission checkPermission, Main plugin, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.plugin = plugin;
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

    public Location getOldPosition(Player player)
    {
        return oldPositions.get(player.getUniqueId());
    }

    public GameMode getOldGameMode(Player player)
    {
        return oldGameModes.get(player.getUniqueId());
    }

    public void removeSavedData(Player player)
    {
        oldPositions.remove(player.getUniqueId());
        oldGameModes.remove(player.getUniqueId());
    }

    public boolean isSpectating(Player player)
    {
        return oldPositions.containsKey(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player restrictionPlayer = null;

        if(sender instanceof Player)
        {
            restrictionPlayer = (Player) sender;
        }

        if(commandRestrictionManager.isDisabled(command.getName(), restrictionPlayer))
        {
            sender.sendMessage("This command is disabled.");
            return true;
        }

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
            player.sendMessage("Use /spectate <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");
            return true;
        }

        if(target == player)
        {
            player.sendMessage("You cannot spectate yourself!");
            return true;
        }

        if(!isSpectating(player))
        {
            oldPositions.put(player.getUniqueId(), player.getLocation().clone());
            oldGameModes.put(player.getUniqueId(), player.getGameMode());
        }

        hide(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(target);

        player.sendMessage("You are now spectating " + target.getName());

        return true;
    }
}
