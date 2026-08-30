package org.example.reports.sourse;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;
import org.example.reports.sourse.data.LoadedReport;
import org.example.reports.sourse.data.ReportStatus;
import org.example.reports.sourse.data.ReportStruct;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportStorage
{
    private final Main plugin;
    private final File file;
    private final FileConfiguration config;

    public ReportStorage(Main plugin)
    {
        this.plugin = plugin;

        File reportFolder = new File(plugin.getDataFolder(), "reports");

        if(!reportFolder.exists())
        {
            reportFolder.mkdirs();
        }

        this.file = new File(reportFolder, "report.yml");

        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException exception)
            {
                plugin.getLogger().severe("Error with report.yml");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void callReport(ReportStruct reportStruct, int priority)
    {
        if(reportStruct == null || reportStruct.target() == null)
        {
            return;
        }

        String path = "Player." + reportStruct.target().getUuid();

        config.set(path + ".NickName", reportStruct.target().getNickName());
        config.set(path + ".Priority",priority);

        if(!config.contains(path + ".Status"))
        {
            config.set(path + ".Status", ReportStatus.OPEN.name());
            config.set(path + ".AssignedStaffUuid", null);
        }

        int reportNumber = 1;

        if(config.getConfigurationSection(path + ".Reports") != null)
        {
            reportNumber = config.getConfigurationSection(path + ".Reports").getKeys(false).size() + 1;
        }

        String reportPath = path + ".Reports." + reportNumber;

        config.set(reportPath + ".Reason", reportStruct.reason());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        config.set(reportPath + ".CreatedAt", reportStruct.createdAt().format(formatter));
        config.set(reportPath + ".SenderName", reportStruct.sender().getNickName());
        config.set(reportPath + ".SenderUuid", reportStruct.sender().getUuid().toString());
        config.set(reportPath + ".SenderRole", reportStruct.sender().getStaffRole().toString());

        saveFile();
    }
    public Map<UUID, ReportCase> loadReports()
    {
        Map<UUID, ReportCase> loadedReports = new HashMap<>();

        if(config.getConfigurationSection("Player") == null)
        {
            return loadedReports;
        }

        Set<String> players = config.getConfigurationSection("Player").getKeys(false);

        for(String uuidString : players)
        {
            UUID targetUuid;

            try
            {
                targetUuid = UUID.fromString(uuidString);
            }
            catch(IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid UUID in report.yml: " + uuidString);
                continue;
            }

            String path = "Player." + uuidString;

            String targetName = config.getString(path + ".NickName");
            int priority = config.getInt(path + ".Priority", 0);


            String statusString = config.getString(path + ".Status", ReportStatus.OPEN.name());

            ReportStatus status;

            try
            {
                status = ReportStatus.valueOf(statusString);
            }
            catch(IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid report status for: " + uuidString);

                status = ReportStatus.OPEN;
            }



            String assignedStaffString = config.getString(path + ".AssignedStaffUuid");

            UUID assignedStaffUuid = null;

            if(assignedStaffString != null)
            {
                try
                {
                    assignedStaffUuid = UUID.fromString(assignedStaffString);
                }
                catch(IllegalArgumentException exception)
                {
                    plugin.getLogger().warning("Invalid assigned staff UUID in report.yml: " + assignedStaffString);
                }
            }



            List<LoadedReport> reports = new ArrayList<>();

            if(config.getConfigurationSection(path + ".Reports") != null)
            {
                Set<String> reportIds = config.getConfigurationSection(path + ".Reports").getKeys(false);

                for(String reportId : reportIds)
                {
                    String reportPath = path + ".Reports." + reportId;

                    String reason = config.getString(reportPath + ".Reason");
                    String createdAt = config.getString(reportPath + ".CreatedAt");

                    String senderName = config.getString(reportPath + ".SenderName");

                    String senderUuidString = config.getString(reportPath + ".SenderUuid");

                    String senderRole = config.getString(reportPath + ".SenderRole");


                    if(senderUuidString == null)
                    {
                        continue;
                    }


                    UUID senderUuid;


                    try
                    {
                        senderUuid = UUID.fromString(senderUuidString);

                    }
                    catch(IllegalArgumentException exception)
                    {
                        plugin.getLogger().warning("Invalid sender UUID in report.yml: " + senderUuidString);
                        continue;
                    }



                    LoadedReport report = new LoadedReport(senderUuid, senderName, senderRole, reason, createdAt);

                    reports.add(report);
                }
            }

            ReportCase reportCase = new ReportCase(targetUuid, targetName, priority, reports, status, assignedStaffUuid);

            loadedReports.put(targetUuid, reportCase);
        }

        return loadedReports;
    }

    public boolean hasReport(UUID uuid)
    {
        String path = "Player." + uuid;

        return config.contains(path);
    }

    public int getPriority(UUID uuid)
    {
        String path = "Player." + uuid + ".Priority";

        return config.getInt(path, 0);
    }

    public void removeReport(UUID targetUuid)
    {
        String path = "Player." + targetUuid;

        config.set(path,null);

        saveFile();
    }

    public void setStatus(UUID targetUuid, ReportStatus status, UUID assignedStaffUuid)
    {
        String path = "Player." + targetUuid;

        config.set(path + ".Status", status.name());

        if(assignedStaffUuid != null)
        {
            config.set(path + ".AssignedStaffUuid", assignedStaffUuid.toString());
        }
        else
        {
            config.set(path + ".AssignedStaffUuid", null);
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
            plugin.getLogger().severe("Could not save report.yml");
            exception.printStackTrace();
        }
    }
}
