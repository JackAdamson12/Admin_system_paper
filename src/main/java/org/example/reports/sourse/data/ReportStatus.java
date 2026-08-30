package org.example.reports.sourse.data;

public enum ReportStatus
{
    OPEN(0),
    IN_PROGRESS(1),
    FINISHED(2);

    private final int level;

    ReportStatus(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }

}