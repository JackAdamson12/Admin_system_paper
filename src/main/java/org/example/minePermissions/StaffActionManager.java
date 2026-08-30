package org.example.minePermissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.minePermissions.notes.MinePermissions;
import org.example.minePermissions.roleCommandsManager.role.AdminPermission;
import org.example.minePermissions.roleCommandsManager.role.HeadAdminPermission;
import org.example.minePermissions.roleCommandsManager.role.HelperPermisson;
import org.example.minePermissions.roleCommandsManager.role.PlayerPermission;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

import java.util.UUID;

public class StaffActionManager
{
    private final PlayerProfileManager playerProfileManager;
    private PlayerProfile playerProfile;

    private final PlayerPermission playerPermission;
    private final HelperPermisson helperPermisson;
    private final AdminPermission adminPermission;
    private final HeadAdminPermission headAdminPermission;

    public StaffActionManager(PlayerProfileManager playerProfileManager, Main plugin)
    {
        this.playerProfileManager = playerProfileManager;

        this.playerPermission = new PlayerPermission(plugin);
        this.helperPermisson = new HelperPermisson(plugin);
        this.adminPermission = new AdminPermission(plugin);
        this.headAdminPermission = new HeadAdminPermission(plugin);
    }

    public boolean cheakPermission(CommandSender sender, String command)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(sender);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.PLAYER)
        {
            return playerPermission.isPermission(command);
        }
        else if(playerProfile.getStaffRole() == StaffRole.HELPER)
        {
            return helperPermisson.isPermission(command);
        }
        else if(playerProfile.getStaffRole() == StaffRole.ADMIN)
        {
            return adminPermission.isPermission(command);
        }
        else if(playerProfile.getStaffRole() == StaffRole.HEAD_ADMIN)
        {
            return headAdminPermission.isPermission(command);
        }
        else if(playerProfile.getStaffRole() == StaffRole.OWNER)
        {
            return true;
        }

        return false;
    }

    public boolean cheakIsOwner(CommandSender sender)
    {
        playerProfile = playerProfileManager.getProfile(sender);
        if(playerProfile == null)
        {
            return false;
        }
        if(!(playerProfile.getStaffRole() == StaffRole.OWNER))
        {
            return false;
        }
        return true;

    }
    public boolean cheakIsOwner(Player admin)
    {
        playerProfile = playerProfileManager.getProfile(admin);
        if(playerProfile == null)
        {
            return false;
        }
        if(!(playerProfile.getStaffRole() == StaffRole.OWNER))
        {
            return false;
        }
        return true;

    }
    public boolean cheakIsOwner(UUID adminUUID)
    {
        playerProfile = playerProfileManager.getProfile(adminUUID);
        if(playerProfile == null)
        {
            return false;
        }
        if(!(playerProfile.getStaffRole() == StaffRole.OWNER))
        {
            return false;
        }
        return true;
    }

    public boolean cheakModerationStatus(Player player)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(player);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.PLAYER)
        {
            return false;
        }
        return true;
    }

    public boolean cheakModerationStatus(CommandSender sender)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(sender);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.PLAYER)
        {
            return false;
        }
        return true;
    }

    public boolean cheakModerationStatus(UUID uuid)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(uuid);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.PLAYER)
        {
            return false;
        }
        return true;
    }


    public boolean cheakIsHelper(Player player)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(player);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.HELPER)
        {
            return true;
        }
        return false;
    }

    public boolean cheakIsHelper(CommandSender sender)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(sender);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.HELPER)
        {
            return true;
        }
        return false;
    }

    public boolean cheakIsHelper(UUID uuid)
    {
        PlayerProfile playerProfile = playerProfileManager.getProfile(uuid);

        if(playerProfile == null)
        {
            return false;
        }

        if(playerProfile.getStaffRole() == StaffRole.HELPER)
        {
            return true;
        }
        return false;
    }


}
