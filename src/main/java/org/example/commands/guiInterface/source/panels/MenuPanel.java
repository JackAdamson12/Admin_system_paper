package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;

public class MenuPanel
{
    private final ButtomCreator buttomCreator;
    private final GuiManager guiManager;

    public MenuPanel(ButtomCreator buttomCreator,GuiManager guiManager)
    {
        this.guiManager = guiManager;
        this.buttomCreator = buttomCreator;
    }

    public void openMenuPanel(Player player)
    {
        Inventory menu = Bukkit.createInventory(null, 27, GuiTitles.MENU_PANEL);
        buttomCreator.createMenuBottom(menu);

        player.openInventory(menu);

    }
}
