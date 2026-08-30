package org.example.commands.guiInterface;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

public class CommandGuiAdmin implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

   private final CheckPermission checkPermission;
   private final GuiManager guiManager;

    public CommandGuiAdmin(CheckPermission checkPermission, GuiManager guiManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.guiManager = guiManager;
        this.checkPermission = checkPermission;
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
            sender.sendMessage("This command cannot open in console");
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        guiManager.openPanel(player);

        return true;
    }
}
