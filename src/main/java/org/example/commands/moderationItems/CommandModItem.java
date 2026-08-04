package org.example.commands.moderationItems;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

public class CommandModItem implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final GiveModeretionItems giveModeretionItems;

    public CommandModItem(CheckPermission checkPermission, GiveModeretionItems giveModeretionItems, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.giveModeretionItems = giveModeretionItems;
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
            sender.sendMessage("This command not for console");
            return true;
        }

        if(!checkPermission.checkIsAdmin(sender))
        {
            sender.sendMessage("No permissions!");
            return true;
        }
        Player admin = (Player) sender;

        if (giveModeretionItems.hasModerationItem(admin))
        {
            admin.sendMessage("You already have the moderation item.");
            return true;
        }


        giveModeretionItems.giveModerationItem(admin);



        return true;

    }
}
