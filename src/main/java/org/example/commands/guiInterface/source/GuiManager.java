package org.example.commands.guiInterface.source;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.guiInterface.source.panels.AdminPanel;
import org.example.commands.guiInterface.source.panels.PlayerPanel;
import org.example.commands.guiInterface.source.panels.PlayerPanelAdmin;
import org.example.commands.guiInterface.source.panels.TempBanPanel;
import org.example.commands.guiInterface.source.panels.TempMutePanel;
import org.example.commands.guiInterface.source.searchGui.SearchGui;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.utils.CheckPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiManager
{
    private final CheckPermission checkPermission;
    private final Main plugin;

    private final Map<UUID, Integer> pages = new HashMap<>();
    private final Map<UUID, UUID> selectedPlayers = new HashMap<>();
    private final Map<UUID, Boolean> searchingPlayers = new HashMap<>();
    private final Map<UUID, String> searchQueries = new HashMap<>();

    private final int panelSize = 54;
    private final int pageSize = panelSize - 9;

    private final AdminPanel adminPanel;
    private final PlayerPanel playerPanel;
    private final PlayerPanelAdmin playerPanelAdmin;
    private final SearchGui searchGui;
    private final TempMutePanel tempMutePanel;
    private final TempBanPanel tempBanPanel;

    public GuiManager(CheckPermission checkPermission, Main plugin)
    {
        this.checkPermission = checkPermission;
        this.plugin = plugin;

        ButtomCreator buttonCreator = new ButtomCreator();

        this.adminPanel = new AdminPanel(buttonCreator, this, pages);
        this.playerPanel = new PlayerPanel();
        this.playerPanelAdmin = new PlayerPanelAdmin(buttonCreator, this);
        this.searchGui = new SearchGui(this);
        this.tempMutePanel = new TempMutePanel(buttonCreator, this);
        this.tempBanPanel = new TempBanPanel(buttonCreator, this);
    }

    public void openPanel(Player player)
    {
        if(checkPermission.checkIsAdmin(player))
        {
            adminPanel.openAdminPanel(player);
        }
        else
        {
            playerPanel.openPlayerPanel(player);
        }
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
        tempMutePanel.open(admin);
    }

    public void openTempBanPanel(Player admin)
    {
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
        pages.put(
                player.getUniqueId(),
                Math.max(1, page)
        );
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

        return title.equals(GuiTitles.ADMIN_PANEL) || title.equals(GuiTitles.FUNCTIONS) || title.equals(GuiTitles.TEMP_BAN) || title.equals(GuiTitles.TEMP_MUTE);
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

    public void cleanup(Player player)
    {
        UUID playerUuid = player.getUniqueId();

        pages.remove(playerUuid);
        selectedPlayers.remove(playerUuid);
        searchingPlayers.remove(playerUuid);
        searchQueries.remove(playerUuid);
    }
}