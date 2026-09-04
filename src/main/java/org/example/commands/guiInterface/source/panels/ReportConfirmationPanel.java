package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;

public class ReportConfirmationPanel
{
    //Panel for confirmation report
    private final ButtomCreator buttomCreator;


    public ReportConfirmationPanel(ButtomCreator buttomCreator)
    {
        this.buttomCreator = buttomCreator;
    }

    public void open(Player player)
    {
        Inventory confirmInv = Bukkit.createInventory(player,27, GuiTitles.IS_REPORT_JUSTIFIED);
        buttomCreator.ReportConfirmationBottom(confirmInv);
        player.openInventory(confirmInv);

    }

}
