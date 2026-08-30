package org.example.playerProfile;

public enum PlayerStaffStatus
{
    PLAYER(0),
    OPEN(1),
    IN_PROGRESS(2),
    FINISHED(3);

    private final int level;

    PlayerStaffStatus(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }

}