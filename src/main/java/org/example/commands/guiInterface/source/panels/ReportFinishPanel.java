package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;

public class ReportFinishPanel {

    private final ButtomCreator buttonCreator;
    private Inventory inventory;

    public ReportFinishPanel(ButtomCreator buttonCreator) {
        this.buttonCreator = buttonCreator;
    }

    public void open(Player player)
    {
        inventory = Bukkit.createInventory(null, 27, GuiTitles.REPOT_FINISH_PANEL);
        inventory.setItem(15, buttonCreator.createButton(Material.LIME_WOOL, ChatColor.GREEN + "Finish case"));
        inventory.setItem(11, buttonCreator.createButton(Material.RED_WOOL, ChatColor.RED + "Cancel"));
        player.openInventory(inventory);
    }

    public void close()
    {
        inventory.close();
    }

}
