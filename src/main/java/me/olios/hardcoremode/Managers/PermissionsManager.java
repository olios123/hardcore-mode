package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Data;
import org.bukkit.entity.Player;

public class PermissionsManager {

    public static boolean checkPermissions(Player p, Data.Permission permission)
    {
        p.recalculatePermissions(); // Refresh permissions

        String properPermission = "hardcoremode." +  permission.toString().toLowerCase().replace("_", "-");

        if (p.hasPermission(properPermission)) return true;
        else return false;
    }
}
