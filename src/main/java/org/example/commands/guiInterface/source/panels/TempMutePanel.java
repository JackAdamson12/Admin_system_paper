package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;

public class TempMutePanel
{
    private final ButtomCreator buttonCreator;
    private final GuiManager guiManager;

    public TempMutePanel(ButtomCreator buttonCreator, GuiManager guiManager)
    {
        this.buttonCreator = buttonCreator;
        this.guiManager = guiManager;
    }

    public void open(Player admin)
    {
        Player target = guiManager.getSelectedPlayer(admin);

        if(target == null)
        {
            admin.sendMessage("§cPlayer left the server.");
            admin.closeInventory();
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 27, GuiTitles.TEMP_MUTE);

        buttonCreator.createTempMuteBottom(inventory);

        admin.openInventory(inventory);
    }
}