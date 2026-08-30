package org.example.reports.sourse;

import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.punishmentManager.PunishmentManager;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.minePermissions.CheckPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.playerProfile.PlayerStaffStatus;
import org.example.reports.sourse.data.ReportStatus;
import org.example.reports.sourse.data.ReportStruct;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportManager
{
    private final CheckPermission checkPermission;
    private final PlayerProfileManager playerProfileManager;
    private final ReportStorage reportStorage;
    private final PunishmentManager punishmentManager;
    private final ReportFinish reportFinish;

    private final Map<UUID, ReportCase> openReports = new HashMap<>();
    private final Map<UUID, ReportCase> inProgressReports = new HashMap<>();
    private final Map<UUID, UUID> assignedReports = new HashMap<>();
    private final Map<UUID,ReportCase> finishedReports = new HashMap<>();


    public ReportManager(CheckPermission checkPermission, PlayerProfileManager playerProfileManager,PunishmentManager punishmentManager, Main plugin)
    {
        this.checkPermission = checkPermission;
        this.playerProfileManager = playerProfileManager;
        this.punishmentManager = punishmentManager;
        this.reportStorage = new ReportStorage(plugin);
        this.reportFinish = new ReportFinish(finishedReports,playerProfileManager,punishmentManager,plugin);

        reloadReports();
    }

    public void createReport(ReportStruct reportStruct)
    {
        if(reportStruct == null)
        {
            return;
        }

        int newPriority = createPriority(reportStruct);

        if(reportStorage.hasReport(reportStruct.target().getUuid()))
        {
            int currentPriority = reportStorage.getPriority(reportStruct.target().getUuid());

            if(currentPriority > newPriority)
            {
                newPriority = currentPriority;
            }
        }

        reportStorage.callReport(reportStruct, newPriority);

        reloadReports();
    }

    public void updateReport(Player admin, UUID targetUuid, ReportCase reportCase)
    {
        if(admin == null || targetUuid == null || reportCase == null)
        {
            return;
        }

        ReportStatus newReportStatus = changeStatus(reportCase.getStatus());

        reportCase.setStatus(newReportStatus);

        if(newReportStatus == ReportStatus.IN_PROGRESS)
        {
            if(assignedReports.containsKey(admin.getUniqueId()))
            {
                return;
            }

            openReports.remove(targetUuid);

            UUID adminUuid = admin.getUniqueId();

            assignedReports.put(adminUuid, targetUuid);

            reportCase.setAssignedStaffUuid(adminUuid);

            PlayerProfile staffProfile = playerProfileManager.getProfile(adminUuid);

            if(staffProfile != null)
            {
                staffProfile.setPlayerStaffStatus(PlayerStaffStatus.IN_PROGRESS);
            }

            inProgressReports.put(targetUuid, reportCase);

            reportStorage.setStatus(targetUuid, newReportStatus, adminUuid);
        }

        if(newReportStatus == ReportStatus.FINISHED)
        {
            inProgressReports.remove(targetUuid);
            finishedReports.put(targetUuid,reportCase);

            UUID staffUuid = reportCase.getAssignedStaffUuid();

            if(staffUuid != null)
            {
                assignedReports.remove(staffUuid);

                PlayerProfile staffProfile = playerProfileManager.getProfile(staffUuid);

                if(staffProfile != null)
                {
                    staffProfile.setPlayerStaffStatus(PlayerStaffStatus.OPEN);
                }
            }

            reportFinish.putInHistory();
            reportStorage.removeReport(targetUuid);
            finishedReports.remove(targetUuid);
        }
    }

    public boolean takeReport(Player admin, UUID targetUuid)
    {
        if(admin == null || targetUuid == null)
        {
            return false;
        }

        if(assignedReports.containsKey(admin.getUniqueId()))
        {
            return false;
        }

        ReportCase reportCase = openReports.get(targetUuid);

        if(reportCase == null)
        {
            return false;
        }

        updateReport(admin,targetUuid,reportCase);

        return reportCase.getStatus() == ReportStatus.IN_PROGRESS;
    }

    private void reloadReports()
    {
        openReports.clear();
        inProgressReports.clear();
        assignedReports.clear();

        Map<UUID,ReportCase> loadedReports = reportStorage.loadReports();

        for(ReportCase reportCase : loadedReports.values())
        {
            if(reportCase.getStatus() == ReportStatus.OPEN)
            {
                openReports.put(reportCase.getTargetUuid(),reportCase);
            }
            else if(reportCase.getStatus() == ReportStatus.IN_PROGRESS)
            {
                UUID staffUuid = reportCase.getAssignedStaffUuid();

                if(staffUuid == null)
                {
                    reportCase.setStatus(ReportStatus.OPEN);
                    openReports.put(reportCase.getTargetUuid(),reportCase);
                    reportStorage.setStatus(reportCase.getTargetUuid(),ReportStatus.OPEN,null);
                    continue;
                }

                inProgressReports.put(reportCase.getTargetUuid(),reportCase);
                assignedReports.put(staffUuid,reportCase.getTargetUuid());

                PlayerProfile staffProfile = playerProfileManager.getProfile(staffUuid);

                if(staffProfile != null)
                {
                    staffProfile.setPlayerStaffStatus(PlayerStaffStatus.IN_PROGRESS);
                }
            }
        }
    }

    private ReportStatus changeStatus(ReportStatus reportStatus)
    {
        int currentStatusLvl = reportStatus.getLevel();
        int newStatusLvl = currentStatusLvl + 1;

        if(newStatusLvl <= 0)
        {
            return ReportStatus.OPEN;
        }

        if(newStatusLvl >= 2)
        {
            return ReportStatus.FINISHED;
        }

        return ReportStatus.IN_PROGRESS;
    }

    public int createPriority(ReportStruct reportStruct)
    {
        if(reportStruct.sender().getStaffRole() == StaffRole.HELPER)
        {
            return 1;
        }

        if(reportStruct.sender().getStaffRole() == StaffRole.PLAYER)
        {
            return 0;
        }

        return 2;
    }

    public Map<UUID, ReportCase> getActiveReports()
    {
        return openReports;
    }

    public Map<UUID, ReportCase> getInProgressReports()
    {
        return inProgressReports;
    }
    public ReportCase getReportCase(UUID targetUuid)
    {
        ReportCase reportCase = openReports.get(targetUuid);

        if(reportCase != null)
        {
            return reportCase;
        }

        return inProgressReports.get(targetUuid);
    }

    public Map<UUID, ReportCase> getReportsForStaff(Player staff)
    {
        Map<UUID, ReportCase> reports = new HashMap<>();

        if(staff == null)
        {
            return reports;
        }

        PlayerProfile playerProfile = playerProfileManager.getProfile(staff);

        if(playerProfile != null && playerProfile.getStaffRole() == StaffRole.HELPER)
        {
            reports.putAll(getActiveReportsForHelper());
        }
        else
        {
            reports.putAll(openReports);
        }

        UUID targetUuid = assignedReports.get(staff.getUniqueId());

        if(targetUuid != null)
        {
            ReportCase reportCase = inProgressReports.get(targetUuid);

            if(reportCase != null)
            {
                reports.put(targetUuid,reportCase);
            }
        }

        return reports;
    }

    public Map<UUID, ReportCase> getActiveReportsForHelper()
    {
        Map<UUID, ReportCase> activeReportsHelper = new HashMap<>();

        for(ReportCase reportCase : openReports.values())
        {
            if(reportCase.getPriority() == 0)
            {
                activeReportsHelper.put(reportCase.getTargetUuid(), reportCase);
            }
        }

        return activeReportsHelper;
    }


}
