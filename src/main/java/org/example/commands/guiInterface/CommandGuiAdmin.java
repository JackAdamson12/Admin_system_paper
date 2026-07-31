package org.example.commands.guiInterface;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.utils.CheckPermission;

public class CommandGuiAdmin implements CommandExecutor
{
   private final CheckPermission checkPermission;
   private final GuiManager guiManager;

    public CommandGuiAdmin(CheckPermission checkPermission, GuiManager guiManager)
    {
        this.guiManager = guiManager;
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command cannot open in console");
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(player))
        {
            sender.sendMessage("No permissions");
            return true;
        }

        guiManager.openPanel(player);

        return true;
    }
}
