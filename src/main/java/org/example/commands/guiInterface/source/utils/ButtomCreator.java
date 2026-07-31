package org.example.commands.guiInterface.source.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ButtomCreator
{

    public ItemStack createButton(Material material, String name)
    {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);

        return item;
    }

    public void addPlayerHead(Player target, Inventory inventory)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwningPlayer(target);
        meta.setDisplayName(target.getName());

        head.setItemMeta(meta);

        inventory.addItem(head);
    }

    public void createNavigationBottom(Inventory inventory)
    {
        inventory.setItem(45, createButton(Material.ARROW, "Back"));
        inventory.setItem(49, createButton(Material.COMPASS, "Search"));
        inventory.setItem(53, createButton(Material.ARROW, "Next"));
    }

    public void createFunctionsBottom(Inventory inventory)
    {
        inventory.setItem(1, createButton(Material.BEDROCK, "Ban"));
        inventory.setItem(2, createButton(Material.DIAMOND, "TempBan"));
        inventory.setItem(3, createButton(Material.GOLD_INGOT, "Mute"));
        inventory.setItem(4, createButton(Material.EMERALD, "TempMute"));
        inventory.setItem(5, createButton(Material.BEDROCK, "Kick"));
    }

    public void createTempBanBottom(Inventory inventory)
    {

        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(16, createButton(Material.CLOCK, "§e7 days"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }

    public void createTempMuteBottom(Inventory inventory)
    {
        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(16, createButton(Material.CLOCK, "§e7 days"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }
}
