package org.example.commands.guiInterface.source.searchGui;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.example.commands.guiInterface.source.GuiManager;

public class SearchChatListener implements Listener
{
    private final GuiManager guiManager;

    public SearchChatListener(GuiManager guiManager)
    {
        this.guiManager = guiManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event)
    {
        Player player = event.getPlayer();

        if(!guiManager.isSearching(player))
            return;

        event.setCancelled(true);

        String query = PlainTextComponentSerializer.plainText().serialize(event.message()).trim();

        guiManager.finishSearch(player, query);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        guiManager.cleanup(event.getPlayer());
    }
}
