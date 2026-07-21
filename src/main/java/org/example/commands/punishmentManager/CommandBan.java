package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class CommandBan implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final ProfileBanList banList;

    public CommandBan(CheckPermission checkPermission)
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

        if(args.length < 2)
        {
            player.sendMessage("Use /ban <player> <reason>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String targetName = getTargetName(target, args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            player.sendMessage("Player has never joined this server.");
            return true;
        }

        if(target.getUniqueId().equals(player.getUniqueId()))
        {
            player.sendMessage("You cannot ban yourself.");
            return true;
        }

        Player onlinePlayer = target.getPlayer();

        if(onlinePlayer != null)
        {
            if(checkPermission.checkIsAdmin(onlinePlayer))
            {
                player.sendMessage("You cannot ban administrators.");
                return true;
            }
        }

        if(target.isBanned())
        {
            long lastLogin = target.getLastLogin();

            String formattedDate = "Unknown";

            if(lastLogin != 0)
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

                formattedDate = Instant.ofEpochMilli(lastLogin).atZone(ZoneId.systemDefault()).format(formatter);
            }

            player.sendMessage("Player " + targetName + " is already banned.\n" + "UUID: " + target.getUniqueId() + "\n" + "Last login: " + formattedDate);

            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        banList.addBan(target.getPlayerProfile(), reason, (Instant) null, player.getName());

        if(onlinePlayer != null)
        {
            onlinePlayer.kick(Component.text("You have been permanently banned.\n" + "Reason: " + reason + "\n" + "Administrator: " + player.getName()));
        }

        player.sendMessage("Player " + targetName + " has been banned.\n" + "UUID: " + target.getUniqueId() + "\n" + "Reason: " + reason);

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
