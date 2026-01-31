package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Config;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.SortedMap;

public class BanTime {

    public static class BanResult {
        private double banTime;
        private boolean lostLive;

        public BanResult() {
            this.banTime = 0;
            this.lostLive = false;
        }

        public void setBanTime(double banTime) {
            this.banTime = banTime;
        }
        public void setLostLive(boolean lostLive) {
            this.lostLive = lostLive;
        }

        public double getBanTime() {
            return banTime;
        }
        public boolean isLostLive() {
            return lostLive;
        }
    }

    public static BanResult calculate(Player p, boolean life, boolean ... ignoreLives)
    {
        // Refresh permissions
        p.recalculatePermissions();
        String uuid = p.getUniqueId().toString();

        UserData userData = UserDataManager.load(uuid);
        BanResult banResult = new BanResult();

        // Player is op and don't allow for ban
        if (p.isOp() && !ConfigManager.config.ALLOW_BAN_OP) return banResult;

        // Check if player can have permission that will prevent ban
        if (!ConfigManager.config.BAN_PERMISSION.equals("disabled"))
        {
            // No death permission and player have it
            if (ConfigManager.config.BAN_PERMISSION.equals("nodeath") &&
                    PermissionsManager.checkPermissions(p, Data.Permission.NODEATH)) return banResult;

            // Death permission and player doesn't have it
            if (ConfigManager.config.BAN_PERMISSION.equals("death") &&
                !PermissionsManager.checkPermissions(p, Data.Permission.DEATH)) return banResult;
        }

        // Player still has lives
        if (ConfigManager.config.LIVES_ENABLE && (userData.lives - 1 >= 0 && ignoreLives.length == 0))
        {
            // Remove life if is not check
            if (life)
            {
                userData.lives -= 1;
                UserDataManager.save(userData);
                banResult.lostLive = true;
            }
            return banResult;
        }
        else // Player doesn't have any lives left
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
                        if (p.hasPermission(rank))
                        {
                            banResult.setBanTime(banTime);
                            return banResult;
                        }
                    }

                    // Rank not found or player doesn't have one
                    banResult.banTime = ConfigManager.config.BAN_RANK_TIME_RANKS.get("default");
                    return banResult;
                }
                else // Everyone have the same ban time
                {
                    banResult.banTime = ConfigManager.config.BAN_TIME;
                    return banResult;
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
                            // Get ban length from function
                            banResult.setBanTime(foundBanLength(rankBanLength, userData));
                            return banResult;
                        }
                    }

                    // Player doesn't have any permission from the list
                    if (!permissionFound)
                    {
                        SortedMap<Integer, Double> defaultBans = ConfigManager.config.BAN_RANK_LENGTH_RANKS.get("default");

                        // Get ban length from function
                        banResult.setBanTime(foundBanLength(defaultBans, userData));
                        return banResult;
                    }
                }
                else // Ban time is always the same for every player
                {
                    // Get ban length from function
                    banResult.setBanTime(foundBanLength(ConfigManager.config.BAN_LENGTH, userData));
                    return banResult;
                }
            }
        }

        return banResult;
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
