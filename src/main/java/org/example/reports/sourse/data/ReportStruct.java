package org.example.reports.sourse.data;

import org.example.playerProfile.PlayerProfile;

import java.time.LocalDateTime;

public record ReportStruct(PlayerProfile sender, PlayerProfile target, String reason, LocalDateTime createdAt)
{
}
