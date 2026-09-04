package org.example.commands.guiInterface.source.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.Main;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.data.LoadedReport;

import java.util.ArrayList;
import java.util.List;

public class ButtomCreator
{

    public ItemStack createButton(Material material, String name)
    {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);

        return item;
    }

    public void addReportHead(ReportCase reportCase, Inventory inventory)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();

        OfflinePlayer target = Bukkit.getOfflinePlayer(reportCase.getTargetUuid());

        meta.setOwningPlayer(target);
        meta.setDisplayName(reportCase.getTargetName());

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "report_target_uuid");

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, reportCase.getTargetUuid().toString());

        head.setItemMeta(meta);

        inventory.addItem(head);
    }
    public void addPlayerHead(Player target, Inventory inventory)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwningPlayer(target);
        meta.setDisplayName(target.getName());

        head.setItemMeta(meta);

        inventory.addItem(head);
    }

    public void createNavigationBottom(Inventory inventory)
    {
        inventory.setItem(45, createButton(Material.ARROW, "Back"));
        inventory.setItem(48, createButton(Material.ARROW, "Return"));
        inventory.setItem(49, createButton(Material.COMPASS, "Search"));
        inventory.setItem(53, createButton(Material.ARROW, "Next"));
    }

    public void createFunctionsBottom(Inventory inventory)
    {
        inventory.setItem(1, createButton(Material.BEDROCK, "Ban"));
        inventory.setItem(2, createButton(Material.DIAMOND, "TempBan"));
        inventory.setItem(3, createButton(Material.GOLD_INGOT, "Mute"));
        inventory.setItem(4, createButton(Material.EMERALD, "TempMute"));
        inventory.setItem(5, createButton(Material.BEDROCK, "Kick"));
    }

    public void createTempBanBottom(Inventory inventory)
    {

        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(16, createButton(Material.CLOCK, "§e7 days"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }

    public void createTempMuteBottomForHelper(Inventory inventory)
    {

        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }

    public void createTempBanBottomForHelper(Inventory inventory)
    {

        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }

    public void createTempMuteBottom(Inventory inventory)
    {
        inventory.setItem(10, createButton(Material.CLOCK, "§e10 minutes"));
        inventory.setItem(11, createButton(Material.CLOCK, "§e30 minutes"));
        inventory.setItem(12, createButton(Material.CLOCK, "§e1 hour"));
        inventory.setItem(13, createButton(Material.CLOCK, "§e6 hours"));
        inventory.setItem(14, createButton(Material.CLOCK, "§e12 hours"));
        inventory.setItem(15, createButton(Material.CLOCK, "§e1 day"));
        inventory.setItem(16, createButton(Material.CLOCK, "§e7 days"));
        inventory.setItem(22, createButton(Material.ARROW, "§cBack"));
    }

    public void createMenuBottom(Inventory inventory)
    {
        //reports
        inventory.setItem(12,createButton(Material.PAPER, ChatColor.RED + "Reports"));
        inventory.setItem(13,createButton(Material.PLAYER_HEAD, ChatColor.AQUA + "Players"));


    }

    public void createReportPanelBottom(Inventory inventory)
    {
        inventory.setItem(45, createButton(Material.ARROW, "Back"));
        inventory.setItem(48, createButton(Material.ARROW, "Return"));
        inventory.setItem(53, createButton(Material.ARROW, "Next"));
    }

    public ItemStack createReportPaper(LoadedReport report, int reportIndex)
    {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Report #" + (reportIndex + 1));
        meta.setTitle("Report #" + (reportIndex + 1));
        meta.setAuthor(report.senderName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "From: " + ChatColor.WHITE + report.senderName());
        lore.add(ChatColor.DARK_GRAY + "Click to read");
        meta.setLore(lore);

        meta.addPage(ChatColor.BOLD + "Report #" + (reportIndex + 1) + "\n\n" +
                ChatColor.RESET + "Sender: " + report.senderName() + "\n" +
                "Role: " + report.senderRole() + "\n" +
                "UUID: " + report.senderUuid() + "\n\n" +
                "Created: " + report.createdAt() + "\n\n" +
                "The reason an next page");

        meta.addPage(ChatColor.BOLD + "Reason\n\n" + ChatColor.RESET + report.reason());

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "report_index");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, reportIndex);

        book.setItemMeta(meta);
        return book;
    }

    public void ReportConfirmationBottom(Inventory inventory)
    {
        inventory.setItem(11, createButton(Material.GREEN_BANNER, ChatColor.GREEN + "CONFIRMED"));
        inventory.setItem(15, createButton(Material.RED_BANNER, ChatColor.RED + "REJECTED"));

    }

}
