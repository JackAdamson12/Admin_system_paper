package org.example.commands.commandTerminal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.playerProfile.PlayerProfileManager;

public class CommandReloadPlayerProfile implements CommandExecutor
{
    private final PlayerProfileManager playerProfileManager;

    public CommandReloadPlayerProfile(PlayerProfileManager playerProfileManager)
    {
        this.playerProfileManager = playerProfileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            sender.sendMessage("This command can only be used from console.");
            return true;
        }

        if(args.length == 0)
        {
            sender.sendMessage("Usage: /reload -p");
            return true;
        }

        if(args[0].equalsIgnoreCase("-p"))
        {
            playerProfileManager.reload();
            sender.sendMessage("Player profiles reloaded.");
            return true;
        }

        sender.sendMessage("Unknown reload flag.");
        return true;
    }
}
