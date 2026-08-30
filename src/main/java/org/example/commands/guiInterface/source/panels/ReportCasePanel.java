package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.data.LoadedReport;
import org.example.reports.sourse.data.ReportStatus;

public class ReportCasePanel
{
    private final ButtomCreator buttomCreator;
    private final GuiManager guiManager;

    public ReportCasePanel(ButtomCreator buttomCreator, GuiManager guiManager)
    {
        this.buttomCreator = buttomCreator;
        this.guiManager = guiManager;
    }

    public void open(Player player)
    {
        ReportCase reportCase = guiManager.getSelectedReportCase(player);

        if(reportCase == null)
        {
            player.sendMessage(ChatColor.RED + "Report case not found.");
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, GuiTitles.REPORT_CASE_PANEL);

        int visibleReports = Math.min(reportCase.getReports().size(), 45);

        for(int i = 0; i < visibleReports; i++)
        {
            LoadedReport report = reportCase.getReports().get(i);
            inventory.addItem(buttomCreator.createReportPaper(report, i));
        }

        if(reportCase.getStatus() == org.example.reports.sourse.data.ReportStatus.OPEN)
        {
            inventory.setItem(48, buttomCreator.createButton(Material.LIME_WOOL, ChatColor.GREEN + "Take case"));
        }

        if(reportCase.getStatus() == ReportStatus.IN_PROGRESS)
        {
            inventory.setItem(48, buttomCreator.createButton(Material.COMPASS, ChatColor.DARK_GRAY + "Close case"));
        }

        inventory.setItem(49, buttomCreator.createButton(Material.ARROW, ChatColor.RED + "Return"));

        player.openInventory(inventory);
    }
}