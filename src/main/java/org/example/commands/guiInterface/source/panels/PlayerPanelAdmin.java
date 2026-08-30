package org.example.commands.guiInterface.source.panels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.commands.guiInterface.source.GuiManager;
import org.example.commands.guiInterface.source.utils.ButtomCreator;
import org.example.commands.guiInterface.source.utils.GuiTitles;
import org.example.playerProfile.PlayerProfile;

public class PlayerPanelAdmin
{
    private final ButtomCreator bc;
    private final GuiManager guiManager;

    public PlayerPanelAdmin(ButtomCreator bc, GuiManager guiManager)
    {
        this.bc = bc;
        this.guiManager = guiManager;
    }

    public void openPlayerPanelForAdmin(Player admin, Player target)
    {
        guiManager.selectPlayer(admin, target);

        Inventory inventory = Bukkit.createInventory(null, 9, GuiTitles.FUNCTIONS);


        bc.createFunctionsBottom(inventory);

        admin.openInventory(inventory);
    }
}
