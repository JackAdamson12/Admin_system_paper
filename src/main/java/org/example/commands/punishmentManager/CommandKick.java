package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class CommandKick implements CommandExecutor
{
    private CheckPermission checkPermission;

    public CommandKick(CheckPermission checkPermission)
    {
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
            player.sendMessage("No permissions.");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /kick <player> <reason>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found.");
            return true;
        }

        String reason = "Kicked by admin.";

        if(args.length >= 2)
        {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        target.kickPlayer(reason);

        player.sendMessage("You kicked " + target.getName() + ". Reason: " + reason);

        return true;
    }

    public static class CommandBanInfo implements CommandExecutor
    {
        private final CheckPermission checkPermission;
        private final ProfileBanList banList;
        private final DateTimeFormatter formatter;

        public CommandBanInfo(CheckPermission checkPermission)
        {
            this.checkPermission = checkPermission;
            this.banList = Bukkit.getBanList(BanListType.PROFILE);
            this.formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
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
                player.sendMessage("Use /baninfo <player>");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            BanEntry<?> banEntry =
                    banList.getBanEntry(target.getPlayerProfile());

            if(banEntry == null)
            {
                player.sendMessage(
                        "Player " + getTargetName(target, args[0]) +
                                " is not banned."
                );

                return true;
            }

            String reason = banEntry.getReason();

            if(reason == null)
            {
                reason = "Not specified";
            }

            String source = banEntry.getSource();

            String createdDate = formatDate(
                    banEntry.getCreated()
            );

            String expirationDate = "Permanent";

            Date expiration = banEntry.getExpiration();

            if(expiration != null)
            {
                expirationDate = formatDate(expiration);
            }

            player.sendMessage(
                    "----- Ban information -----\n" +
                            "Player: " + getTargetName(target, args[0]) + "\n" +
                            "UUID: " + target.getUniqueId() + "\n" +
                            "Reason: " + reason + "\n" +
                            "Administrator: " + source + "\n" +
                            "Created: " + createdDate + "\n" +
                            "Expires: " + expirationDate
            );

            return true;
        }

        private String formatDate(Date date)
        {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .format(formatter);
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
}
