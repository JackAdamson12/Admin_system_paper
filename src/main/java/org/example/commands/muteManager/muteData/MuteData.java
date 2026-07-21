package org.example.commands.muteManager.muteData;

import java.time.Instant;
import java.util.UUID;

public record MuteData(UUID uuid, String reason, String moderator, Instant expiresAt)
{

}
