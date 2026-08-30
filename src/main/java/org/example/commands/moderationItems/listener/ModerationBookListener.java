package org.example.commands.moderationItems.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.minePermissions.CheckPermission;

public class ModerationBookListener implements Listener {

    private final GuiManager guiManager;
    private final CheckPermission checkPermission;

    public ModerationBookListener(GuiManager guiManager, CheckPermission checkPermission)
    {
        this.guiManager = guiManager;
        this.checkPermission = checkPermission;
    }

    @EventHandler
    public void onBookClick(PlayerInteractEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND)
        {
            return;
        }

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        ItemStack item = event.getItem();


        if (item == null || item.getType() != Material.BOOK)
        {
            return;
        }
        if (!item.hasItemMeta())
        {
            return;
        }

        if (!item.getItemMeta().hasDisplayName())
        {
            return;
        }

        if (!item.getItemMeta().getDisplayName().equals("§cModeration Menu"))
        {
            return;
        }
        if (!checkPermission.checkIsAdmin(event.getPlayer())) {
            event.getPlayer().sendMessage("No permission!");
            return;
        }

        event.setCancelled(true);

        guiManager.openPanel(event.getPlayer());
    }
}