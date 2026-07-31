package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdminPanel
{
    private final ButtomCreator bc;
    private final GuiManager guiManager;
    private  Map<UUID, Integer> pages;

    private int panelSize;
    private int pageSize;


    public AdminPanel(ButtomCreator bc, GuiManager guiManager, Map<UUID, Integer> pages)
    {
        this.pages = pages;
        this.bc = bc;
        this.guiManager = guiManager;
        panelSize = guiManager.getPanelSize();
        pageSize = guiManager.getPageSize();

    }

    public void openAdminPanel(Player admin)
    {
        Inventory inventory = Bukkit.createInventory(null, panelSize, GuiTitles.ADMIN_PANEL);

        bc.createNavigationBottom(inventory);

        fillPlayers(admin,inventory);

        admin.openInventory(inventory);
    }

    public void nextPage(Player admin)
    {
        int totalPages = getTotalPages(admin);

        int page = pages.getOrDefault(admin.getUniqueId(), 1);

        if(page < totalPages)
        {
            pages.put(admin.getUniqueId(), page + 1);
            openAdminPanel(admin);
        }
    }

    public void backPage(Player admin)
    {
        int page = pages.getOrDefault(admin.getUniqueId(), 1);

        if(page > 1)
        {
            pages.put(admin.getUniqueId(), page - 1);
            openAdminPanel(admin);
        }
    }

    public void fillPlayers(Player admin, Inventory inventory)
    {
        int totalPages = getTotalPages(admin);

        if(totalPages == 0)
            totalPages = 1;

        int page = pages.getOrDefault(admin.getUniqueId(), 1);

        if(page > totalPages)
        {
            page = totalPages;
            pages.put(admin.getUniqueId(), page);
        }

        int start = (page - 1) * pageSize;
        int end = start + pageSize;

        int i = 0;

        List<Player> players = getFilteredPlayers(admin);

        for (Player target : players)
        {
            if (i >= start && i < end)
            {
                bc.addPlayerHead(target, inventory);
            }

            i++;
        }
    }
    public List<Player> getFilteredPlayers(Player admin)
    {
        String search = guiManager.getSearchQuery(admin);

        List<Player> players = new ArrayList<>();

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (search.isEmpty() || target.getName().toLowerCase().contains(search.toLowerCase()))
            {
                players.add(target);
            }
        }

        return players;
    }



    public int getTotalPages(Player admin)
    {
        int players = getFilteredPlayers(admin).size();

        int totalPages = (int) Math.ceil((double) players / pageSize);

        return Math.max(totalPages, 1);
    }
}
