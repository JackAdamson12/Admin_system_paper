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
import org.example.minePermissions.CheckPermission;

public class CommandUnspectate implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private CheckPermission checkPermission;
    private CommandSpectate commandSpectate;
    private Main plugin;

    public CommandUnspectate(CheckPermission checkPermission, Main plugin, CommandSpectate commandSpectate, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.plugin = plugin;
        this.commandSpectate = commandSpectate;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(!commandSpectate.isSpectating(player))
        {
            player.sendMessage("You are not spectating anyone!");
            return true;
        }

        Location oldPosition = commandSpectate.getOldPosition(player);
        GameMode oldGameMode = commandSpectate.getOldGameMode(player);

        player.setSpectatorTarget(null);

        if(oldGameMode != null)
        {
            player.setGameMode(oldGameMode);
        }

        if(oldPosition != null)
        {
            player.teleport(oldPosition);
        }

        show(player);

        commandSpectate.removeSavedData(player);

        player.sendMessage("Spectate mode disabled!");

        return true;
    }
}
