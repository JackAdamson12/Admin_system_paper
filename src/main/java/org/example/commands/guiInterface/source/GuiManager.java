package org.example.commands.guiInterface.source;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.guiInterface.source.panels.*;
import org.example.commands.guiInterface.source.searchGui.SearchGui;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.minePermissions.CheckPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.ReportManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiManager
{
    private final CheckPermission checkPermission;
    private final Main plugin;
    private final ReportManager reportManager;

    private final Map<UUID, Integer> pages = new HashMap<>();
    private final Map<UUID, UUID> selectedPlayers = new HashMap<>();
    private final Map<UUID, Boolean> searchingPlayers = new HashMap<>();
    private final Map<UUID, String> searchQueries = new HashMap<>();

    private final int panelSize = 54;
    private final int pageSize = panelSize - 9;

    private final AdminPanel adminPanel;
    private final PlayerPanelAdmin playerPanelAdmin;
    private final SearchGui searchGui;
    private final TempMutePanel tempMutePanel;
    private final TempBanPanel tempBanPanel;
    private final MenuPanel menuPanel;
    private final ReportPanel reportPanel;
    private final ReportCasePanel reportCasePanel;
    private final ReportConfirmPanel reportConfirmPanel;
    private final ReportFinishPanel reportFinishPanel;

    private final PlayerProfileManager playerProfileManager;

    public GuiManager(CheckPermission checkPermission, PlayerProfileManager playerProfileManager, Main plugin,ReportManager reportManager)
    {
        this.checkPermission = checkPermission;
        this.plugin = plugin;
        this.playerProfileManager = playerProfileManager;
        this.reportManager = reportManager;

        ButtomCreator buttonCreator = new ButtomCreator();

        this.adminPanel = new AdminPanel(buttonCreator, this, pages);

        this.playerPanelAdmin = new PlayerPanelAdmin(buttonCreator, this);
        this.searchGui = new SearchGui(this);
        this.tempMutePanel = new TempMutePanel(buttonCreator, this);
        this.tempBanPanel = new TempBanPanel(buttonCreator, this);
        this.menuPanel = new MenuPanel(buttonCreator,this);
        this.reportPanel = new ReportPanel(buttonCreator,reportManager,playerProfileManager);
        this.reportCasePanel = new ReportCasePanel(buttonCreator, this);
        this.reportConfirmPanel = new ReportConfirmPanel(buttonCreator);
        this.reportFinishPanel = new ReportFinishPanel(buttonCreator);
    }

    public void openPanel(Player player)
    {
        PlayerProfile playerProfile;
        playerProfile = playerProfileManager.getProfile(player);

        if(playerProfile.getStaffRole() == StaffRole.PLAYER)
        {
            player.sendMessage("No permissions!");
            return;
        }
        openMenuPanel(player);


    }
    public void openReportCasePanel(Player player)
    {
        reportCasePanel.open(player);
    }

    public void openReportConfirmPanel(Player player)
    {
        reportConfirmPanel.open(player);
    }

    public void openFinishCasePanel(Player player)
    {
        reportFinishPanel.open(player);
    }

    public boolean takeSelectedReportCase(Player player)
    {
        ReportCase reportCase = getSelectedReportCase(player);
        if(reportCase == null)
            return false;

        return reportManager.takeReport(player, reportCase.getTargetUuid());
    }

    public void openReportMenu(Player player)
    {
        reportPanel.openReportPanel(player);
    }

    public void nextReportPage(Player player)
    {
        reportPanel.nextPage(player);
    }

    public void backReportPage(Player player)
    {
        reportPanel.backPage(player);
    }

    public void openMenuPanel(Player player)
    {
        menuPanel.openMenuPanel(player);
    }

    public void openAdminPanel(Player player)
    {
        adminPanel.openAdminPanel(player);
    }

    public void nextPage(Player player)
    {
        adminPanel.nextPage(player);
    }

    public void backPage(Player player)
    {
        adminPanel.backPage(player);
    }

    public void openTempMutePanel(Player admin)
    {
        if(checkPermission.cheakIsHelper(admin))
        {
            tempMutePanel.openForHelper(admin);
            return;
        }

        tempMutePanel.open(admin);
    }

    public void updateReport(Player admin, UUID target, ReportCase reportCase)
    {
        reportManager.updateReport(admin,target,reportCase);
    }

    public void openTempBanPanel(Player admin)
    {
        if(checkPermission.cheakIsHelper(admin))
        {
            tempBanPanel.openForHelper(admin);
            return;
        }

        tempBanPanel.open(admin);
    }

    public void openPlayerPanelForAdmin(Player admin, Player target)
    {

        selectPlayer(admin, target);
        playerPanelAdmin.openPlayerPanelForAdmin(admin, target);
    }

    public void selectPlayer(Player admin, Player target)
    {
        selectedPlayers.put(admin.getUniqueId(), target.getUniqueId());
    }

    public Player getSelectedPlayer(Player admin)
    {
        UUID targetUuid = selectedPlayers.get(admin.getUniqueId());

        if(targetUuid == null)
            return null;

        return Bukkit.getPlayer(targetUuid);
    }

    public void clearSelectedPlayer(Player admin)
    {
        selectedPlayers.remove(admin.getUniqueId());
    }

    public int getPage(Player player)
    {
        return pages.getOrDefault(player.getUniqueId(), 1);
    }

    public void setPage(Player player, int page)
    {
        pages.put(player.getUniqueId(), Math.max(1, page));
    }

    public void resetPage(Player player)
    {
        pages.put(player.getUniqueId(), 1);
    }

    public int getPanelSize()
    {
        return panelSize;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void startSearch(Player player)
    {
        searchingPlayers.put(player.getUniqueId(), true);
    }

    public boolean isSearching(Player player)
    {
        return searchingPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void stopSearch(Player player)
    {
        searchingPlayers.remove(player.getUniqueId());
    }

    public void openSearch(Player player)
    {

        startSearch(player);
        searchGui.open(player);
    }

    public void finishSearch(Player player, String query)
    {
        stopSearch(player);

        String normalizedQuery = query == null ? "" : query.trim();


        if(normalizedQuery.isBlank() || normalizedQuery.equalsIgnoreCase("cancel"))
        {
            clearSearchQuery(player);

            plugin.getServer().getScheduler().runTask(plugin, () -> adminPanel.openAdminPanel(player));

            return;
        }

        setSearchQuery(player, normalizedQuery);

        plugin.getServer().getScheduler().runTask(plugin, () ->
        {
            List<Player> foundPlayers = getVisiblePlayers(player);

            if(foundPlayers.isEmpty())
            {
                player.sendMessage("§cИгроки по запросу \"" + normalizedQuery + "\" не найдены.");
                clearSearchQuery(player);
            }

            adminPanel.openAdminPanel(player);
        });
    }

    public void setSearchQuery(Player player, String query)
    {
        UUID playerUuid = player.getUniqueId();

        String normalizedQuery = query == null ? "" : query.trim().toLowerCase();

        if(normalizedQuery.isBlank())
        {
            searchQueries.remove(playerUuid);
        }
        else
        {
            searchQueries.put(playerUuid, normalizedQuery);
        }

        pages.put(playerUuid, 1);
    }

    public String getSearchQuery(Player player)
    {
        return searchQueries.getOrDefault(player.getUniqueId(), "");
    }

    public void clearSearchQuery(Player player)
    {
        UUID playerUuid = player.getUniqueId();

        searchQueries.remove(playerUuid);
        pages.put(playerUuid, 1);
    }

    public List<Player> getVisiblePlayers(Player admin)
    {
        return adminPanel.getFilteredPlayers(admin);
    }

    public boolean isInAdminPanel(Player player)
    {
        return GuiTitles.ADMIN_PANEL.equals(player.getOpenInventory().getTitle());
    }


    public boolean isPluginGui(String title)
    {
        if(title == null)
            return false;

        return title.equals(GuiTitles.ADMIN_PANEL)
                || title.equals(GuiTitles.FUNCTIONS)
                || title.equals(GuiTitles.TEMP_BAN)
                || title.equals(GuiTitles.TEMP_MUTE)
                || title.equals(GuiTitles.MENU_PANEL)
                || title.equals(GuiTitles.REPORT_PANEL)
                || title.equals(GuiTitles.REPORT_CASE_PANEL)
                || title.equals(GuiTitles.REPORT_CONFIRM_PANEL)
                || title.equals(GuiTitles.REPOT_FINISH_PANEL);
    }

    public void handleGuiClose(Player player)
    {

        plugin.getServer().getScheduler().runTask(plugin, () ->
        {

            if(isSearching(player))
                return;

            String currentTitle = player.getOpenInventory().getTitle();

            if(isPluginGui(currentTitle))
                return;


            clearSearchQuery(player);
            clearSelectedPlayer(player);
            stopSearch(player);
        });
    }
    private final Map<UUID, UUID> selectedReportCases = new HashMap<>();

    public void selectReportCase(Player admin, UUID targetUuid)
    {
        selectedReportCases.put(admin.getUniqueId(), targetUuid);
    }

    public ReportCase getSelectedReportCase(Player admin)
    {
        UUID targetUuid = selectedReportCases.get(admin.getUniqueId());

        if(targetUuid == null)
            return null;

        return reportManager.getReportCase(targetUuid);
    }

    public void clearSelectedReportCase(Player admin)
    {
        selectedReportCases.remove(admin.getUniqueId());
    }

    public void cleanup(Player player)
    {
        UUID playerUuid = player.getUniqueId();

        pages.remove(playerUuid);
        selectedPlayers.remove(playerUuid);
        selectedReportCases.remove(playerUuid);
        reportPanel.resetPage(player);
        searchingPlayers.remove(playerUuid);
        searchQueries.remove(playerUuid);
    }
}