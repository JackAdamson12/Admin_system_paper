package org.example.reports.sourse;

import org.example.reports.sourse.data.LoadedReport;
import org.example.reports.sourse.data.ReportStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportCase
{
    private final UUID targetUuid;
    private final String targetName;
    private final int priority;
    private final List<LoadedReport> reports;
    private ReportStatus status;
    private UUID assignedStaffUuid;

    public ReportCase(UUID targetUuid, String targetName, int priority, List<LoadedReport> reports)
    {
        this(targetUuid, targetName, priority, reports, ReportStatus.OPEN, null);
    }

    public ReportCase(UUID targetUuid, String targetName, int priority, List<LoadedReport> reports, ReportStatus status, UUID assignedStaffUuid)
    {
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.priority = priority;
        if(reports == null)
        {
            this.reports = new ArrayList<>();
        }
        else
        {
            this.reports = reports;
        }

        if(status == null)
        {
            this.status = ReportStatus.OPEN;
        }
        else
        {
            this.status = status;
        }
        this.assignedStaffUuid = assignedStaffUuid;
    }

    public void setStatus(ReportStatus status) { this.status = status; }

    public ReportStatus getStatus() { return status; }

    public void setAssignedStaffUuid(UUID staffUuid) { this.assignedStaffUuid = staffUuid; }

    public UUID getAssignedStaffUuid() { return assignedStaffUuid; }

    public List<LoadedReport> getReports() { return reports; }

    public UUID getTargetUuid() { return targetUuid; }


    public String getTargetName() { return targetName; }

    public int getPriority() { return priority; }
}
