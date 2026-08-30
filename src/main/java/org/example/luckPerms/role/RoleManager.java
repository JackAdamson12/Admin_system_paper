package org.example.luckPerms.role;

import org.example.Main;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

public class RoleManager
{
    private final PlayerProfileManager playerProfileManager;

    public RoleManager( PlayerProfileManager playerProfileManager)
    {
        this.playerProfileManager = playerProfileManager;
    }
    public boolean canManage(PlayerProfile actor, PlayerProfile target)
    {
        if(actor == null || target == null)
        {
            return false;
        }

        if(actor.getLevel() <= target.getLevel())
        {
            return false;
        }

        return true;
    }
    public boolean canSetRole(PlayerProfile actor, StaffRole newRole)
    {
        if(actor == null || newRole == null)
        {
            return false;
        }

        if(newRole == StaffRole.OWNER)
        {
            return false;
        }

        if(actor.getStaffRole() == StaffRole.OWNER)
        {
            return true;
        }

        if(actor.getStaffRole() == StaffRole.HEAD_ADMIN)
        {
            return newRole.getLevel() <= StaffRole.ADMIN.getLevel();
        }

        if(actor.getStaffRole() == StaffRole.ADMIN)
        {
            return newRole.getLevel() <= StaffRole.ADMIN.getLevel();
        }

        return false;

    }

    //level up
    public boolean promote(PlayerProfile actor, PlayerProfile target)
    {
        if(!canManage(actor, target))
        {
            return false;
        }

        int newLevel = target.getLevel() + 1;

        StaffRole newRole = StaffRole.getRoleByLevel(newLevel);

        if(newRole == actor.getStaffRole())
        {
            //добавить описаниеЫ
            return false;
        }

        if(!canSetRole(actor, newRole))
        {
            return false;
        }

        target.levelUp();

        playerProfileManager.saveProfile(target);

        return true;
    }

    //level down
    public boolean demote(PlayerProfile actor, PlayerProfile target)
    {
        if(!canManage(actor, target))
        {
            return false;
        }

        int newLevel = target.getLevel() - 1;

        StaffRole newRole = StaffRole.getRoleByLevel(newLevel);

        target.levelDown();

        playerProfileManager.saveProfile(target);

        return true;
    }





}
