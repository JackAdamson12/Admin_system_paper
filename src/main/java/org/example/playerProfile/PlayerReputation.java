package org.example.playerProfile;

public enum PlayerReputation
{
    EXCELLENT(90),
    GOOD(75),
    AVERAGE(50),
    BELOW_AVERAGE(25),
    BAD(0),
    ERROR(-1);



    private final int level;

    PlayerReputation(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }
}
