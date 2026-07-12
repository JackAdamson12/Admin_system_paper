package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.utils.CheckPermission;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CommandAdminInfo implements CommandExecutor
{

    private final CheckPermission checkPermission;
    private final Main plugin;
    private final File file;

    public CommandAdminInfo(CheckPermission checkPermission, Main plugin)
    {

        this.checkPermission = checkPermission;
        this.plugin = plugin;

        this.file = new File(plugin.getDataFolder(), "adminfo.txt");

        if (!file.exists())
        {
            plugin.saveResource("adminfo.txt", false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            return true;
        }

        if (!checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("No permission!");
            return true;
        }

        try
        {
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines)
            {
                player.sendMessage(line);
            }

        } catch (IOException e)
        {
            player.sendMessage("Cannot read adminfo.txt");
            e.printStackTrace();
        }

        return true;
    }
}
