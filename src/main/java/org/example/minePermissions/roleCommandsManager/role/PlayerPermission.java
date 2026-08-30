package org.example.minePermissions.roleCommandsManager.role;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerPermission
{
    protected Set<String> permission = new HashSet<>();

    private final Main plugin;
    private final File file;
    private final File permissionsFolder;

    private FileConfiguration config;

    public PlayerPermission(Main plugin)
    {
        this.plugin = plugin;

        this.permissionsFolder = new File(plugin.getDataFolder(), "permissions");

        if(!permissionsFolder.exists())
        {
            permissionsFolder.mkdirs();
        }

        this.file = new File(permissionsFolder, "permission_role.yml");

        this.config = YamlConfiguration.loadConfiguration(file);

        List<String> playerCommands = config.getStringList("PLAYER.commands");

        permission.addAll(playerCommands);
    }


    public boolean isPermission(String command)
    {
        return permission.contains(command);
    }
}