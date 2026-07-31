package org.example.commands.moderationItems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.utils.CheckPermission;

import java.util.List;

public class GiveModeretionItems implements Listener
{
    private static final String PERMISSION = "moderation.admin";
    private static final int ITEM_SLOT = 8;

    private final JavaPlugin plugin;
    private final GuiManager guiManager;
    private final NamespacedKey moderationItemKey;
    //private final CheckPermission checkPermission;

    public GiveModeretionItems(JavaPlugin plugin, GuiManager guiManager)
    {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.moderationItemKey = new NamespacedKey(plugin, "moderation_panel_item");
        //this.checkPermission = checkPermission;
    }


    public boolean isModerationItem(ItemStack item)
    {
        if(item == null || item.getType() == Material.AIR)
            return false;

        ItemMeta meta = item.getItemMeta();

        if(meta == null)
            return false;

        return meta.getPersistentDataContainer().has(moderationItemKey, PersistentDataType.BYTE);
    }

    public boolean hasModerationItem(Player player)
    {
        return player.getInventory().containsAtLeast(createModerationItem(), 1);
    }

    public void giveModerationItem(Player player)
    {
        player.getInventory().addItem(createModerationItem());
    }
    public ItemStack createModerationItem()
    {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§cModeration Menu");
        item.setItemMeta(meta);

        return item;
    }


    private void removeDuplicateItems(Player player)
    {
        boolean found = false;

        ItemStack[] contents = player.getInventory().getContents();

        for(int slot = 0; slot < contents.length; slot++)
        {
            ItemStack item = contents[slot];

            if(!isModerationItem(item))
                continue;

            if(!found)
            {
                item.setAmount(1);
                found = true;
                continue;
            }

            player.getInventory().setItem(slot, null);
        }
    }

    private void scheduleItemCheck(Player player)
    {
        plugin.getServer().getScheduler().runTaskLater(
                plugin,
                () ->
                {
                    if(!player.isOnline())
                        return;

                    giveModerationItem(player);
                },
                1L
        );
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Item droppedItem = event.getItemDrop();

        if(!isModerationItem(droppedItem.getItemStack()))
            return;

        event.setCancelled(true);

        scheduleItemCheck(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player player))
            return;

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if(!isModerationItem(currentItem) && !isModerationItem(cursorItem))
        {
            return;
        }

        event.setCancelled(true);

        scheduleItemCheck(player);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player player))
            return;

        if(!isModerationItem(event.getOldCursor()))
            return;

        event.setCancelled(true);

        scheduleItemCheck(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        if(!isModerationItem(event.getItem()))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if(!player.hasPermission(PERMISSION))
        {
            player.sendMessage("§cNopermission.");
            return;
        }

        guiManager.openPanel(player);
    }
}