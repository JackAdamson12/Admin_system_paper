package org.example.reports.sourse;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;
import org.example.commands.punishmentManager.PunishmentManager;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.reports.sourse.data.PunishmentStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ReportFinish
{
    private final Map<UUID,ReportCase> finishedReports;
    private final Set<UUID> reportUUID;
    private final Main plugin;
    private final File file;
    private final FileConfiguration config;
    private final PlayerProfileManager playerProfileManager;
    private final PunishmentManager punishmentManager;

    public ReportFinish(Map<UUID,ReportCase> finishedReports, PlayerProfileManager playerProfileManager,PunishmentManager punishmentManager ,Main plugin)
    {
        this.finishedReports = finishedReports;
        this.plugin = plugin;
        this.reportUUID = finishedReports.keySet();
        this.playerProfileManager =playerProfileManager;
        this.punishmentManager = punishmentManager;


        File fileDirection = new File(plugin.getDataFolder(), "reports");

        if(!fileDirection.exists())
        {
            fileDirection.mkdir();
        }
        this.file = new File(fileDirection,"history_reports.yml");

        if(!file.exists())
        {
            try
            {

                file.createNewFile();

            }
            catch(IOException exception)
            {
                plugin.getLogger().severe("Error with history_reports.yml");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);

    }
    public void putInHistory()
    {
        for(UUID uuid : finishedReports.keySet())
        {
            String path = "Player." + uuid;

            ReportCase reportCase = finishedReports.get(uuid);

            int total_reports = reportCase.getReports().size();

            config.set(path + ".Nickname", reportCase.getTargetName());
            config.set(path + ".TotalReports", total_reports);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            config.set(path + ".CaseFinishedAt", LocalDateTime.now().format(formatter));

            if(punishmentManager.isBanned(uuid))
            {
                config.set(path + ".Punishment", PunishmentStatus.BANNED.toString());
            }
            else if(punishmentManager.isMuted(uuid))
            {
                config.set(path + ".Punishment", PunishmentStatus.MUTED.toString());
            }
            else
            {
                config.set(path + ".Punishment", null);
            }

            config.set(path + ".Status", reportCase.getReportResult().name());
        }

        saveFile();
    }

    private void saveFile()
    {
        try
        {
            config.save(file);
        }
        catch(IOException exception)
        {
            plugin.getLogger().severe("Could not save history_reports.yml");
            exception.printStackTrace();
        }
    }



}
