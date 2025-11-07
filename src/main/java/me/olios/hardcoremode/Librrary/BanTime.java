package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.SortedMap;

public class BanTime {

    public static double get(Player p, boolean life, boolean ... ignoreLives)
    {
        // Refresh permissions
        p.recalculatePermissions();
        String uuid = p.getUniqueId().toString();

        UserData userData = UserDataManager.load(uuid);

        if (PermissionsManager.checkPermissions(p, Data.Permission.NODEATH)) return 0.0;
        if (p.isOp() && ConfigManager.config.ADMIN_NO_BAN_AFTER_DEATH) return 0.0;

        // Player still has lives
        if (userData.lives - 1 >= 0 && ignoreLives.length == 0)
        {
            // Remove life if is not check
            if (life)
            {
                userData.lives -= 1;
                UserDataManager.save(userData);
            }
            return 0.0;
        }
        else // Player don't have any lives left
        {
            // Static ban time
            if (ConfigManager.config.BAN_TYPE.equals("constant"))
            {
                // Ban time is based on rank
                if (ConfigManager.config.BAN_RANK_TIME_ENABLE)
                {
                    for (Map.Entry<String, Double> entry : ConfigManager.config.BAN_RANK_TIME_RANKS.entrySet())
                    {
                        String rank = entry.getKey();
                        double banTime = entry.getValue();

                        // Check if player has rank
                        if (p.hasPermission(rank)) return banTime;
                    }

                    // Rank not found or player doesn't have one
                    return ConfigManager.config.BAN_RANK_TIME_RANKS.get("default");
                }
                else // Everyone have the same ban time
                {
                    return ConfigManager.config.BAN_TIME;
                }
            }
            else if (ConfigManager.config.BAN_TYPE.equals("increasing")) // Dynamic ban time
            {
                // Ban time is baned on rank
                if (ConfigManager.config.BAN_RANK_LENGTH_ENABLE)
                {
                    // If player doesn't have any permission from the list
                    boolean permissionFound = false;

                    for (Map.Entry<String, SortedMap<Integer, Double>> entry : ConfigManager.config.BAN_RANK_LENGTH_RANKS.entrySet())
                    {
                        String rank = entry.getKey();
                        SortedMap<Integer, Double> rankBanLength = entry.getValue();

                        // Check if player has permission
                        if (p.hasPermission(rank))
                        {
                            permissionFound = true;

                            // Get ban length from function
                            return foundBanLength(rankBanLength, userData);
                        }
                    }

                    // Player doesn't have any permission from the list
                    if (!permissionFound)
                    {
                        SortedMap<Integer, Double> defaultBans = ConfigManager.config.BAN_RANK_LENGTH_RANKS.get("default");

                        // Get ban length from function
                        return foundBanLength(defaultBans, userData);
                    }
                }
                else // Ban time is always the same for every player
                {
                    // Get ban length from function
                    return foundBanLength(ConfigManager.config.BAN_LENGTH, userData);
                }
            }
        }

        return 0.0;
    }

    public static double foundBanLength(SortedMap<?, ?> map,
                                         UserData userData)
    {
//        int deathLevel = userData.deathLevel;
//        if (next) deathLevel++;

        if (userData.deathLevel <= 1) return (double) map.get(1);
        else if (userData.deathLevel > 1 && userData.deathLevel <= map.size())
        {
            return (double) map.get(userData.deathLevel);
        }
        else return (double) map.get(map.size());
    }
}
