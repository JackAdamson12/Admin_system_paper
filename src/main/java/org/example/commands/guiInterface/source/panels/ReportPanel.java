package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.playerProfile.PlayerProfileManager;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.ReportManager;

import java.util.*;

public class ReportPanel
{
    private final ButtomCreator buttomCreator;
    private final ReportManager reportManager;
    private final Map<UUID, Integer> pages = new HashMap<>();
    private final PlayerProfileManager playerProfileManager;

    private final int panelSize = 54;
    private final int pageSize = panelSize - 9;

    public ReportPanel(ButtomCreator buttomCreator, ReportManager reportManager, PlayerProfileManager playerProfileManager)
    {
        this.reportManager = reportManager;
        this.buttomCreator = buttomCreator;
        this.playerProfileManager = playerProfileManager;
    }

    public void openReportPanel(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, panelSize, GuiTitles.REPORT_PANEL);

        List<ReportCase> sortedReports = new ArrayList<>(reportManager.getReportsForStaff(player).values());

        sortedReports.sort(Comparator.comparingInt(ReportCase::getPriority).reversed());



        int totalPages = getTotalPages(sortedReports.size());
        int page = Math.min(getPage(player), totalPages);
        pages.put(player.getUniqueId(), page);

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, sortedReports.size());

        for(int i = fromIndex; i < toIndex; i++)
        {
            buttomCreator.addReportHead(sortedReports.get(i), inventory);
        }

        buttomCreator.createReportPanelBottom(inventory);
        player.openInventory(inventory);
    }

    public void nextPage(Player player)
    {
        int totalPages = getTotalPages(reportManager.getReportsForStaff(player).size());
        int page = getPage(player);

        if(page < totalPages)
        {
            pages.put(player.getUniqueId(), page + 1);
            openReportPanel(player);
        }
    }

    public void backPage(Player player)
    {
        int page = getPage(player);

        if(page > 1)
        {
            pages.put(player.getUniqueId(), page - 1);
            openReportPanel(player);
        }
    }

    public void resetPage(Player player)
    {
        pages.remove(player.getUniqueId());
    }

    private int getPage(Player player)
    {
        return pages.getOrDefault(player.getUniqueId(), 1);
    }

    private int getTotalPages(int totalReports)
    {

        return Math.max(1, (int) Math.ceil((double) totalReports / pageSize));
    }
}
