package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandUnban implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final ProfileBanList banList;

    public CommandUnban(CheckPermission checkPermission)
    {
        this.banList = Bukkit.getBanList(BanListType.PROFILE);
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /pardon <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            player.sendMessage("Player has never joined this server.");
            return true;
        }

        if(!target.isBanned())
        {
            player.sendMessage("Player " + getTargetName(target, args[0]) + " is not banned.");

            return true;
        }

        banList.pardon(target.getPlayerProfile());

        player.sendMessage("Player " + getTargetName(target, args[0]) + " has been unbanned.\n" + "UUID: " + target.getUniqueId());

        return true;
    }

    private String getTargetName(OfflinePlayer target, String argument)
    {
        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = argument;
        }

        return targetName;
    }
}