package org.example.minePermissions.roleCommandsManager.role;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;

import java.io.File;
import java.util.List;

public class HelperPermisson extends PlayerPermission
{

    private final File file;
    private final File permissionsFolder;
    private final FileConfiguration config;

    public HelperPermisson(Main plugin)
    {
        super(plugin);


        this.permissionsFolder = new File(plugin.getDataFolder(), "permissions");

        if(!permissionsFolder.exists())
        {
            permissionsFolder.mkdirs();
        }

        this.file = new File(permissionsFolder, "permission_role.yml");

        this.config = YamlConfiguration.loadConfiguration(file);

        List<String> playerCommands = config.getStringList("HELPER.commands");

        permission.addAll(playerCommands);
    }

    public boolean isPermission(String command)
    {
        return permission.contains(command);
    }

}
