package org.example.commands.guiInterface.source.searchGui;

import org.bukkit.entity.Player;
import org.example.commands.guiInterface.source.GuiManager;

public class SearchGui
{
    private final GuiManager guiManager;

    public SearchGui(GuiManager guiManager)
    {
        this.guiManager = guiManager;
    }

    public void open(Player player)
    {
        guiManager.startSearch(player);
        player.closeInventory();
        player.sendMessage("§eВведите ник игрока в чат для поиска.");
        player.sendMessage("§7Введите §ccancel §7для отмены.");
    }
}
