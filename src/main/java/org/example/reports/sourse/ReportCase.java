package org.example.reports.sourse;

import org.example.reports.sourse.data.LoadedReport;
import org.example.reports.sourse.data.ReportResult;
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
    private ReportResult reportResult;

    public ReportCase(UUID targetUuid, String targetName, int priority, List<LoadedReport> reports)
    {
        this(targetUuid, targetName, priority, reports, ReportStatus.OPEN,ReportResult.UNKNOW, null);
    }

    public ReportCase(UUID targetUuid, String targetName, int priority, List<LoadedReport> reports, ReportStatus status,ReportResult reportResult ,UUID assignedStaffUuid)
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

        if(reportResult == null)
        {
            this.reportResult = ReportResult.UNKNOW;
        }
        else
        {
            this.reportResult = reportResult;
        }
        this.assignedStaffUuid = assignedStaffUuid;
    }

    public void setStatus(ReportStatus status) { this.status = status; }

    public void setReportResult(ReportResult reportResult)
    {
        this.reportResult = reportResult;
    }

    public ReportResult getReportResult()
    {
        return reportResult;
    }

    public ReportStatus getStatus() { return status; }

    public void setAssignedStaffUuid(UUID staffUuid) { this.assignedStaffUuid = staffUuid; }

    public UUID getAssignedStaffUuid() { return assignedStaffUuid; }

    public List<LoadedReport> getReports() { return reports; }

    public UUID getTargetUuid() { return targetUuid; }


    public String getTargetName() { return targetName; }

    public int getPriority() { return priority; }
}
