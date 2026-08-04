package org.example.commands.guiInterface.source.guiListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.commands.muteManager.CommandMute;
import org.example.commands.punishmentManager.CommandBan;

public class GuiListener implements Listener
{
    private final GuiManager guiManager;
    private final CommandMute commandMute;
    private final CommandBan commandBan;
    private final CommandRestrictionManager commandRestrictionManager;

    public GuiListener(
            GuiManager guiManager,
            CommandMute commandMute,
            CommandBan commandBan,
            CommandRestrictionManager commandRestrictionManager
    )
    {
        this.guiManager = guiManager;
        this.commandMute = commandMute;
        this.commandBan = commandBan;
        this.commandRestrictionManager = commandRestrictionManager;
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
                if(isDisabled("ban", admin))
                {
                    return;
                }

                commandBan.getBan(admin, target);
                closeAndClear(admin);
                break;

            case 2:
                if(isDisabled("tempban", admin))
                {
                    return;
                }

                guiManager.openTempBanPanel(admin);
                break;

            case 3:
                if(isDisabled("mute", admin))
                {
                    return;
                }

                commandMute.getMute(admin, target);
                closeAndClear(admin);
                break;

            case 4:
                if(isDisabled("tmute", admin))
                {
                    return;
                }

                guiManager.openTempMutePanel(admin);
                break;

            case 5:
                if(isDisabled("kick", admin))
                {
                    return;
                }

                target.kickPlayer("Kicked by admin");
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