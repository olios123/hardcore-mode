package me.olios.hardcoremode.API;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.olios.hardcoremode.Librrary.BanTime;
import me.olios.hardcoremode.Librrary.ConvertTime;
import me.olios.hardcoremode.Librrary.Numeric;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Librrary.Sorter;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.FilesManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Cache;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class PAPIExpansion extends PlaceholderExpansion {

//    private final Plugin plugin;

//    public PlaceholderAPIExpansion(Plugin plugin)
//    {
//        this.plugin = plugin;
//    }

    @Override
    public String getAuthor()
    {
        return "olios";
    }

    @Override
    public String getIdentifier()
    {
        return "HardcoreMode";
    }

    @Override
    public String getVersion()
    {
        return "1.1.0";
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override // Player if offline
    public String onRequest(OfflinePlayer offlinePlayer, String params)
    {
        if (offlinePlayer == null) return null;

        String convertedParams = params.toLowerCase();
        UserData userData = UserDataManager.load(offlinePlayer.getUniqueId().toString());
        if (userData == null) return null;
        String uuid = offlinePlayer.getUniqueId().toString();
        YamlConfiguration config = FilesManager.getConfigYml();

        // Placeholders with arguments
        // ban-rank-time
        if (convertedParams.contains("config_ban-rank-time_ranks_"))
        {
            return getValueWithParams("ban-rank-time.ranks", convertedParams);
        }

        // ban-length
        if (convertedParams.contains("config_ban-length_"))
        {
            return getValueWithParams("ban-length", convertedParams);
        }

        // ban-rank-length
        if (convertedParams.contains("config_ban-rank-length_ranks_"))
        {
            String[] slicedParams = convertedParams.split("_");

            if (slicedParams[slicedParams.length - 3].equalsIgnoreCase("ranks"))
            {
                String arg0 = slicedParams[slicedParams.length - 2];
                String arg1 = slicedParams[slicedParams.length - 1];

                return config.getString("ban-rank-length.ranks." + arg0 + "." + arg1);
            }
            else return null;
        }

        // leaderboard - deaths
        if (convertedParams.contains("leaderboard_deaths_"))
        {
            String[] slicedParams = convertedParams.split("_");

            if (Numeric.check(slicedParams[slicedParams.length - 1]))
            {
                int size = Numeric.get(slicedParams[slicedParams.length - 1]);
                if (size > 0)
                {
                    // Leaderboard with specified position
                    if (convertedParams.contains("leaderboard_deaths_pos_"))
                        return getLeaderboard("DEATHS", 100, size);
                        // Leaderboard with specified length
                    else return getLeaderboard("DEATHS", size);
                }
            }
        }

        // leaderboard - deaths
        if (convertedParams.contains("leaderboard_kills_"))
        {
            String[] slicedParams = convertedParams.split("_");

            if (Numeric.check(slicedParams[slicedParams.length - 1]))
            {
                int size = Numeric.get(slicedParams[slicedParams.length - 1]);
                if (size > 0)
                {
                    // Leaderboard with specified position
                    if (convertedParams.contains("leaderboard_kills_pos_"))
                        return getLeaderboard("KILLS", 100, size);
                        // Leaderboard with specified length
                    else return getLeaderboard("KILLS", size);
                }
            }
        }

        // leaderboard - lives
        if (convertedParams.contains("leaderboard_lives_"))
        {
            String[] slicedParams = convertedParams.split("_");

            if (Numeric.check(slicedParams[slicedParams.length - 1]))
            {
                int size = Numeric.get(slicedParams[slicedParams.length - 1]);
                if (size > 0)
                {
                    // Leaderboard with specified position
                    if (convertedParams.contains("leaderboard_lives_pos_"))
                        return getLeaderboard("LIVES", 100, size);
                        // Leaderboard with specified length
                    else return getLeaderboard("LIVES", size);
                }
            }
        }

        switch (convertedParams)
        {
            // Userdata
            case "uuid":
                return offlinePlayer.getUniqueId().toString();

            case "username":
            case "player":
                return offlinePlayer.getName();

            case "last-ban":
                return String.valueOf(userData.lastBan);

            case "death-level":
                return String.valueOf(userData.deathLevel);

            case "deaths":
                return String.valueOf(offlinePlayer.getStatistic(Statistic.DEATHS));

            case "next-ban-time":
                if (!offlinePlayer.isOnline()) return "???";
                return ConvertTime.convertTime(BanTime.get(
                        Bukkit.getPlayer(UUID.fromString(uuid)), false, true)).time;

            case "lives":
                return String.valueOf(userData.lives);

            case "kills":
                return String.valueOf(offlinePlayer.getStatistic(Statistic.PLAYER_KILLS));

            case "lives-info":
                return String.valueOf(userData.livesInfo);

            case "renewing-lives-time":
                Map<String, Integer> renewingLives = Cache.renewingLives;

                if (!renewingLives.containsKey(uuid)) return "-";

                int remainingTime1 = renewingLives.get(uuid);
                long timestamp1 = new Date().getTime() / 1000;
                double leftTime1 = (double) remainingTime1 - timestamp1;

                return ConvertTime.convertTime(leftTime1 / 60).time;

            case "ban-level-time":
                Map<String, Integer> loweringBans = Cache.loweringBans;

                if (!loweringBans.containsKey(uuid)) return "-";

                int remainingTime2 = loweringBans.get(uuid);
                long timestamp2 = new Date().getTime() / 1000;
                double leftTime2 = (double) remainingTime2 - timestamp2;

                return ConvertTime.convertTime(leftTime2 / 60).time;

            case "location":
                String location = "";

                if (!offlinePlayer.isOnline()) return null;
                Player p = offlinePlayer.getPlayer();

                location = p.getLocation().getBlockX() + " " +
                           p.getLocation().getBlockY() + " " +
                           p.getLocation().getBlockZ() + " ";

                return location;

            case "location_x":
                String locationX = "";

                if (!offlinePlayer.isOnline()) return null;
                Player pLoc = offlinePlayer.getPlayer();

                locationX = String.valueOf(pLoc.getLocation().getBlockX());

                return locationX;

            case "location_y":
                String locationY = "";

                if (!offlinePlayer.isOnline()) return null;
                Player pLocX = offlinePlayer.getPlayer();

                locationY = String.valueOf(pLocX.getLocation().getBlockY());

                return locationY;

            case "location_z":
                String locationZ = "";

                if (!offlinePlayer.isOnline()) return null;
                Player pLocZ = offlinePlayer.getPlayer();

                locationZ = String.valueOf(pLocZ.getLocation().getBlockZ());

                return locationZ;
        }

        // Local placeholders
        for (Map.Entry<String, Object> entry : PAPICustom.getStaticPlaceholders().entrySet())
        {
            String fixedPlaceholder = entry.getKey();
            fixedPlaceholder = fixedPlaceholder.replace("%", "");
            fixedPlaceholder = fixedPlaceholder.replace("hardcoremode_", "");

            if (convertedParams.equals(fixedPlaceholder)) return String.valueOf(entry.getValue());
        }

        return null;
    }

    public static String getValueWithParams(String path, String params)
    {
        YamlConfiguration config = FilesManager.getConfigYml();
        String[] slicedParams = params.split("_");
        String variable;

        if (path.contains("."))
        {
            String[] slicedPath = path.split("\\.");
            variable = slicedPath[slicedPath.length - 1];
        }
        else variable = path;

        if (slicedParams[slicedParams.length - 2].equalsIgnoreCase(variable))
        {
            String selectedParams = slicedParams[slicedParams.length - 1];
            return config.getString(path + "." + selectedParams);
        }

        return null;
    }

    public static String getLeaderboard(String type,
                                        int size,
                                        int ... position)
    {
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).toList();
        String leaderboard = "";

        switch (type)
        {
            case "DEATHS":
                Map<OfflinePlayer, Integer> deathsMap = new HashMap<>();
                for (OfflinePlayer offlinePlayer : offlinePlayers)
                {
                    OfflinePlayer contains = containsByNick(deathsMap, offlinePlayer);

                    if (contains != null)
                    {
                        if (deathsMap.get(contains) < offlinePlayer.getStatistic(Statistic.DEATHS)) deathsMap.replace(offlinePlayer, offlinePlayer.getStatistic(Statistic.DEATHS));
                    }
                    else deathsMap.put(offlinePlayer, offlinePlayer.getStatistic(Statistic.DEATHS));
                }

                int currentDeathPos = 0;
                for (Map.Entry<OfflinePlayer, Integer> entry: Sorter.sortByValue(deathsMap).entrySet())
                {
                    Map<String, Object> placeholders = new HashMap<>();
                    placeholders.put("%position%", currentDeathPos + 1);
                    placeholders.put("%value%", String.valueOf(entry.getValue()));

                    leaderboard += StringReplace.string(
                            ConfigManager.config.LEADERBOARD_FORMAT_DEATHS,
                            entry.getKey(),
                            placeholders) + "\n";

                    if (currentDeathPos >= size) break;
                    currentDeathPos++;
                }
                break;

                // TODO problem - nie zawsze zamienia placeholdery gracza

            case "KILLS":
                Map<OfflinePlayer, Integer> killsMap = new HashMap<>();
                for (OfflinePlayer offlinePlayer : offlinePlayers)
                {
                    OfflinePlayer contains = containsByNick(killsMap, offlinePlayer);

                    if (contains != null)
                    {
                        if (killsMap.get(contains) < offlinePlayer.getStatistic(Statistic.PLAYER_KILLS)) killsMap.replace(offlinePlayer, offlinePlayer.getStatistic(Statistic.PLAYER_KILLS));
                    }
                    else killsMap.put(offlinePlayer, offlinePlayer.getStatistic(Statistic.PLAYER_KILLS));
                }

                int currentKillsPos = 0;
                for (Map.Entry<OfflinePlayer, Integer> entry: Sorter.sortByValue(killsMap).entrySet())
                {
                    Map<String, Object> placeholders = new HashMap<>();
                    placeholders.put("%position%", currentKillsPos + 1);
                    placeholders.put("%value%", String.valueOf(entry.getValue()));

                    leaderboard += StringReplace.string(
                            ConfigManager.config.LEADERBOARD_FORMAT_KILLS,
                            entry.getKey(),
                            placeholders) + "\n";

                    if (currentKillsPos >= size) break;
                    currentKillsPos++;
                }
                break;

            case "LIVES":
                Map<OfflinePlayer, Integer> livesMap = new HashMap<>();
                for (OfflinePlayer offlinePlayer : offlinePlayers)
                {
                    UserData userData = UserDataManager.load(offlinePlayer.getUniqueId().toString());
                    if (userData == null) continue;

                    livesMap.put(offlinePlayer, userData.lives);
                }

                int currentLivesPos = 0;
                for (Map.Entry<OfflinePlayer, Integer> entry: Sorter.sortByValue(livesMap).entrySet())
                {
                    Map<String, Object> placeholders = new HashMap<>();
                    placeholders.put("%position%", currentLivesPos + 1);
                    placeholders.put("%value%", String.valueOf(entry.getValue()));

                    leaderboard += StringReplace.string(
                            ConfigManager.config.LEADERBOARD_FORMAT_LIVES,
                            entry.getKey(),
                            placeholders) + "\n";

                    if (currentLivesPos >= size) break;
                    currentLivesPos++;
                }
                break;
        }

        if (position.length > 0)
        {
            int pos = position[0] - 1;
            String[] splitedLeaderboard = leaderboard.split("\n");

            // Index out of range
            if (splitedLeaderboard.length <= pos)
            {
                if (ConfigManager.config.LEADERBOARD_EMPTY.equalsIgnoreCase("none"))
                {
                    // Return empty string
                    return "";
                }
                else if (ConfigManager.config.LEADERBOARD_EMPTY.equalsIgnoreCase("last"))
                {
                    // Return last element of list
                    return splitedLeaderboard[splitedLeaderboard.length - 1];
                }
                else
                {
                    // Return provided string
                    return ConfigManager.config.LEADERBOARD_EMPTY;
                }
            }
            else return splitedLeaderboard[pos]; // Return leaderboard on position
        }

        return leaderboard.substring(0, leaderboard.length() - 2);
    }

    private static OfflinePlayer containsByNick(Map<OfflinePlayer, ?> map, OfflinePlayer offlinePlayer)
    {
        for (Map.Entry<OfflinePlayer, ?> entry : map.entrySet())
        {
            if (Objects.equals(entry.getKey().getName(), offlinePlayer.getName()))
                return entry.getKey();
        }
        return null;
    }
}
