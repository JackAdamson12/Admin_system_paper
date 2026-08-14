package org.example.luckPerms.role.listRols;

public enum StaffRole
{
    OWNER(4),
    HEAD_ADMIN(3),
    ADMIN(2),
    HELPER(1),
    PLAYER(0);

    private final int level;

    StaffRole(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }
    public static StaffRole getRoleByLevel(int level)
    {
        for(StaffRole role : StaffRole.values())
        {
            if(role.getLevel() == level)
            {
                return role;
            }
        }

        return PLAYER;
    }
}
