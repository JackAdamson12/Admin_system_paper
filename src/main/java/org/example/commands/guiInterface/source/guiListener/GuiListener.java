package org.example.commands.guiInterface.source.guiListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.Main;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.commands.punishmentManager.muteManager.CommandMute;
import org.example.commands.punishmentManager.banManager.CommandBan;
import org.example.minePermissions.CheckPermission;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.data.ReportResult;
import org.example.reports.sourse.data.ReportStatus;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.playerProfile.playerReputationManager.PlayerReputationManager;

import java.util.UUID;

public class GuiListener implements Listener
{
    private final GuiManager guiManager;
    private final CommandMute commandMute;
    private final CommandBan commandBan;
    private final CommandRestrictionManager commandRestrictionManager;
    private final CheckPermission checkPermission;
    private final PlayerProfileManager playerProfileManager;
    private final PlayerReputationManager playerReputationManager;

    public GuiListener(GuiManager guiManager, CommandMute commandMute, CommandBan commandBan, CommandRestrictionManager commandRestrictionManager, CheckPermission checkPermission, PlayerProfileManager playerProfileManager, PlayerReputationManager playerReputationManager)
    {
        this.guiManager = guiManager;
        this.commandMute = commandMute;
        this.commandBan = commandBan;
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.playerProfileManager = playerProfileManager;
        this.playerReputationManager = playerReputationManager;
    }
    @EventHandler
    public void onReportPanelClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.REPORT_PANEL))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType() == Material.AIR)
            return;


        if(event.getSlot() == 45)
        {
            guiManager.backReportPage(admin);
            return;
        }

        if(event.getSlot() == 48)
        {
            guiManager.openMenuPanel(admin);
            return;
        }

        if(event.getSlot() == 53)
        {
            guiManager.nextReportPage(admin);
            return;
        }

        if(clickedItem.getType() != Material.PLAYER_HEAD)
            return;

        ItemMeta meta = clickedItem.getItemMeta();

        if(meta == null)
            return;

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "report_target_uuid");

        String uuidString = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if(uuidString == null)
            return;

        UUID targetUuid = UUID.fromString(uuidString);

        guiManager.selectReportCase(admin, targetUuid);
        guiManager.openReportCasePanel(admin);
    }

    @EventHandler
    public void onReportCaseClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.REPORT_CASE_PANEL))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        if(event.getSlot() == 49)
        {
            guiManager.clearSelectedReportCase(admin);
            guiManager.openReportMenu(admin);
            return;
        }

        if(event.getSlot() == 48)
        {
            ReportCase reportCase = guiManager.getSelectedReportCase(admin);
            if(reportCase == null)
            {
                return;
            }

            if(reportCase.getStatus() == ReportStatus.OPEN)
            {
                guiManager.openReportConfirmPanel(admin);
                return;
            }
            if(reportCase.getStatus() == ReportStatus.IN_PROGRESS)
            {
                if(!admin.getUniqueId().equals(reportCase.getAssignedStaffUuid()))
                {
                    admin.sendMessage(ChatColor.RED + "This report belongs to another staff member.");
                    return;
                }
                guiManager.openFinishCasePanel(admin);

                return;
            }
        }

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType() != Material.WRITTEN_BOOK)
            return;

        // The report data is already stored inside the written book pages.
        admin.openBook(clickedItem);
    }

    @EventHandler
    public void onReportFinishClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
        {
            return;
        }
        if(!event.getView().getTitle().equals(GuiTitles.REPOT_FINISH_PANEL))
        {
            return;
        }
        if(event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory())
        {
            return;
        }
        event.setCancelled(true);

        if(event.getSlot() == 11)
        {
            guiManager.openReportCasePanel(admin);
            return;
        }
        if(event.getSlot() == 15)
        {
            ReportCase reportCase = guiManager.getSelectedReportCase(admin);

            if(reportCase == null)
            {
                return;
            }

            if(reportCase.getStatus() != ReportStatus.IN_PROGRESS)
            {
                return;
            }

            if(!admin.getUniqueId().equals(reportCase.getAssignedStaffUuid()))
            {
                return;
            }

            guiManager.openReportConfirmStatusPanel(admin);
            return;
        }



    }
    @EventHandler
    public void onReportConfirmStatusClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.IS_REPORT_JUSTIFIED))
            return;

        if(event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        ReportCase reportCase = guiManager.getSelectedReportCase(admin);

        if(reportCase == null)
            return;

        if(reportCase.getStatus() != ReportStatus.IN_PROGRESS)
            return;

        if(!admin.getUniqueId().equals(reportCase.getAssignedStaffUuid()))
            return;

        if(event.getSlot() == 11)
        {
            reportCase.setReportResult(ReportResult.CONFIRMED);

            PlayerProfile target = playerProfileManager.getProfile(reportCase.getTargetUuid());

            if(target != null)
            {
                playerReputationManager.scoreReputation(target);
            }

            guiManager.updateReport(admin, reportCase.getTargetUuid(), reportCase);
            guiManager.openReportMenu(admin);
            return;
        }

        if(event.getSlot() == 15)
        {
            reportCase.setReportResult(ReportResult.REJECTED);

            guiManager.updateReport(admin, reportCase.getTargetUuid(), reportCase);
            guiManager.openReportMenu(admin);
            return;
        }
    }

    @EventHandler
    public void onReportConfirmClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.REPORT_CONFIRM_PANEL))
            return;

        if(event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        if(event.getSlot() == 15)
        {
            guiManager.openReportCasePanel(admin);
            return;
        }

        if(event.getSlot() != 11)
            return;

        if(guiManager.takeSelectedReportCase(admin))
        {
            admin.sendMessage("§aReport case taken.");
            guiManager.clearSelectedReportCase(admin);
            guiManager.openReportMenu(admin);
        }
        else
        {
            admin.sendMessage("§cYou cannot take this report case.");
            guiManager.openReportMenu(admin);
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event)
    {
        //who
        if(!(event.getWhoClicked() instanceof Player admin))
        {
            return;
        }
        //is That Menu Panel?
        if(!(event.getView().getTitle().equals(GuiTitles.MENU_PANEL)))
        {
            return;
        }
        //Menu was clicked?
        if(event.getClickedInventory() == null)
        {
            return;
        }
        //is clicked menu not inventory
        if(event.getClickedInventory() != event.getView().getTopInventory())
        {
            return;
        }
        //нельзя перетасквать айтемы
        event.setCancelled(true);

        switch (event.getSlot())
        {
            case 12:
                guiManager.openReportMenu(admin);
                break;

            case 13:
                guiManager.openAdminPanel(admin);
                break;

            default:
                break;
        }


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.ADMIN_PANEL))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        int slot = event.getSlot();

        if(slot == 53)
        {
            guiManager.nextPage(admin);
            return;
        }
        if(slot == 48)
        {
            guiManager.openMenuPanel(admin);
            return;
        }

        if(slot == 49)
        {
            guiManager.openSearch(admin);
            return;
        }

        if(slot == 45)
        {
            guiManager.backPage(admin);
            return;
        }

        if(slot >= 0 && slot < guiManager.getPageSize())
        {
            ItemStack item = event.getCurrentItem();

            if(item == null || !(item.getItemMeta() instanceof SkullMeta meta))
                return;

            if(meta.getOwningPlayer() == null)
                return;

            Player target = meta.getOwningPlayer().getPlayer();

            if(target == null)
            {
                admin.sendMessage("§cPlayer left the server.");
                return;
            }

            guiManager.openPlayerPanelForAdmin(admin, target);
        }
    }

    @EventHandler
    public void onFunctionsClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.FUNCTIONS))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        Player target = guiManager.getSelectedPlayer(admin);

        if(target == null)
        {
            admin.sendMessage("§cPlayer left the server.");
            closeAndClear(admin);
            return;
        }

        switch(event.getSlot())
        {
            case 1:
                if(!checkPermission.checkPermission(admin, "ban"))
                {
                    admin.sendMessage("§cYou don't have permission to ban players.");
                    return;
                }

                if(isDisabled("ban", admin))
                {
                    return;
                }

                commandBan.getBan(admin, target);
                playerReputationManager.setTrustLevelFromCommands(target,-5);
                closeAndClear(admin);
                break;

            case 2:
                if(!checkPermission.checkPermission(admin, "tempban"))
                {
                    admin.sendMessage("§cYou don't have permission to tempban players.");
                    return;
                }

                if(isDisabled("tempban", admin))
                {
                    return;
                }

                guiManager.openTempBanPanel(admin);
                break;

            case 3:
                if(!checkPermission.checkPermission(admin, "mute"))
                {
                    admin.sendMessage("§cYou don't have permission to mute players.");
                    return;
                }

                if(isDisabled("mute", admin))
                {
                    return;
                }

                commandMute.getMute(admin, target);
                playerReputationManager.setTrustLevelFromCommands(target,-5);
                closeAndClear(admin);
                break;

            case 4:
                if(!checkPermission.checkPermission(admin, "tmute"))
                {
                    admin.sendMessage("§cYou don't have permission to temporarily mute players.");
                    return;
                }

                if(isDisabled("tmute", admin))
                {
                    return;
                }

                guiManager.openTempMutePanel(admin);
                break;

            case 5:
                if(!checkPermission.checkPermission(admin, "kick"))
                {
                    admin.sendMessage("§cYou don't have permission to kick players.");
                    return;
                }

                if(isDisabled("kick", admin))
                {
                    return;
                }

                target.kickPlayer("Kicked by admin");
                playerReputationManager.setTrustLevelFromCommands(target,-2);
                closeAndClear(admin);
                break;

            case 8:
                guiManager.clearSelectedPlayer(admin);
                guiManager.openPanel(admin);
                break;

            default:
                break;
        }
    }

    @EventHandler
    public void onTempBanClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.TEMP_BAN))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        Player target = guiManager.getSelectedPlayer(admin);

        if(target == null)
        {
            admin.sendMessage("§cPlayer left the server.");
            closeAndClear(admin);
            return;
        }

        int slot = event.getSlot();

        if(slot == 22)
        {
            guiManager.openPlayerPanelForAdmin(admin, target);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if(item == null || item.getType().isAir())
        {
            return;
        }

        String duration = getDuration(slot);

        if(duration == null)
            return;

        if(isDisabled("tempban", admin))
        {
            return;
        }

        boolean executed = Bukkit.dispatchCommand(admin, "tempban " + target.getName() + " " + duration + " Punished from admin GUI");

        if(!executed)
        {
            admin.sendMessage("§cTempBan command could not be executed.");
            return;
        }
        playerReputationManager.setTrustLevelFromCommands(target,-5);

        closeAndClear(admin);
    }

    @EventHandler
    public void onTempMuteClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player admin))
            return;

        if(!event.getView().getTitle().equals(GuiTitles.TEMP_MUTE))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory() != event.getView().getTopInventory())
            return;

        event.setCancelled(true);

        Player target = guiManager.getSelectedPlayer(admin);

        if(target == null)
        {
            admin.sendMessage("§cPlayer left the server.");
            closeAndClear(admin);
            return;
        }

        int slot = event.getSlot();

        if(slot == 22)
        {
            guiManager.openPlayerPanelForAdmin(admin, target);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if(item == null || item.getType().isAir())
        {
            return;
        }

        String duration = getDuration(slot);

        if(duration == null)
            return;

        if(isDisabled("tmute", admin))
        {
            return;
        }

        boolean executed = Bukkit.dispatchCommand(admin, "tmute " + target.getName() + " " + duration + " Punished from admin GUI");

        if(!executed)
        {
            admin.sendMessage("§cTempMute command could not be executed.");
            return;
        }
        playerReputationManager.setTrustLevelFromCommands(target,-5);

        closeAndClear(admin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(!(event.getPlayer() instanceof Player player))
            return;

        String closedTitle = event.getView().getTitle();

        if(!guiManager.isPluginGui(closedTitle))
            return;

        guiManager.handleGuiClose(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        guiManager.cleanup(event.getPlayer());
    }

    private String getDuration(int slot)
    {
        return switch(slot)
        {
            case 10 -> "10m";
            case 11 -> "30m";
            case 12 -> "1h";
            case 13 -> "6h";
            case 14 -> "12h";
            case 15 -> "1d";
            case 16 -> "7d";
            default -> null;
        };
    }

    private boolean isDisabled(String command, Player admin)
    {
        if(!commandRestrictionManager.isDisabled(command, admin))
        {
            return false;
        }

        admin.sendMessage("§cCommand " + command + " is disabled.");
        return true;
    }

    private void closeAndClear(Player admin)
    {
        guiManager.clearSelectedPlayer(admin);
        admin.closeInventory();
    }
}