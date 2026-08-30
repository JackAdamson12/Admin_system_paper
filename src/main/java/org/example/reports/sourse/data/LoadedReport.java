package org.example.reports.sourse.data;

import java.util.UUID;

public record LoadedReport(UUID senderUuid, String senderName, String senderRole, String reason, String createdAt)
{
}