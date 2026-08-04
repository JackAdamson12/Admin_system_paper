package org.example.commands.commandRestriction.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;

public class CommandRestrictionListener implements Listener
{
    private final CommandRestrictionManager commandRestrictionManager;

    public CommandRestrictionListener(CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event)
    {
        String commandName = getCommandName(event.getMessage());

        if(commandName.isBlank())
        {
            return;
        }

        if(commandName.equalsIgnoreCase("disable") || commandName.equalsIgnoreCase("enable"))
        {
            return;
        }

        if(commandRestrictionManager.isDisabled(commandName, event.getPlayer()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage("This command is disabled for you.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event)
    {
        String commandName = getCommandName(event.getCommand());

        if(commandName.isBlank())
        {
            return;
        }

        if(commandName.equalsIgnoreCase("disable") || commandName.equalsIgnoreCase("enable"))
        {
            return;
        }

        if(commandRestrictionManager.isDisabled(commandName, null))
        {
            event.setCancelled(true);
            event.getSender().sendMessage("This command is disabled for the server.");
        }
    }

    private String getCommandName(String commandLine)
    {
        if(commandLine == null || commandLine.isBlank())
        {
            return "";
        }

        String commandName = commandLine.trim();

        if(commandName.startsWith("/"))
        {
            commandName = commandName.substring(1);
        }

        int spaceIndex = commandName.indexOf(' ');

        if(spaceIndex >= 0)
        {
            commandName = commandName.substring(0, spaceIndex);
        }

        int namespaceIndex = commandName.indexOf(':');

        if(namespaceIndex >= 0 && namespaceIndex + 1 < commandName.length())
        {
            commandName = commandName.substring(namespaceIndex + 1);
        }

        return commandName.toLowerCase();
    }
}
