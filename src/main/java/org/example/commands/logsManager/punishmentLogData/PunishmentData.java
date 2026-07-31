package org.example.commands.logsManager.punishmentLogData;

import java.time.Instant;
import java.util.UUID;

public record PunishmentData(UUID uuid, String target, String reason, String moderator, PunishmentType punishment, Instant createdAt, Instant expiresAt)
{

}
