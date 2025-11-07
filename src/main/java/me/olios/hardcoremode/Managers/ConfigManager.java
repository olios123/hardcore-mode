package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Librrary.NickUpdater;
import me.olios.hardcoremode.Librrary.Numeric;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Main;
import me.olios.hardcoremode.Objects.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConfigManager {

    // Config object
    public static Config config = new Config();



    public static void versionUpdater()
    {
        
    }

    public static void checkCorrectness()
    {
//        File localResources = Data.resouces;
//        if (!localResources.exists()) return;
//
//        File localConfigFile = new File(localResources, "config.yml");
//        if (!localConfigFile.exists()) return;
//
//        config.getConfigurationSection("").getKeys(false).forEach(key ->
//        {
//            Main.l(key);
//        });


//        YamlConfiguration cnf = FilesManager.getConfigYml();
//
//        // check-updates
//        if (!checkBoolean(cnf.getString("check-updates")))
//            configWarning("check-updates", ConfigIncorrect.BOOLEAN);
//
//        // clearer-logs
//        if (!checkBoolean(cnf.getString("clearer-logs")))
//            configWarning("clearer-logs", ConfigIncorrect.BOOLEAN);
//
//        // ban-type
//        List<String> options = new ArrayList<>();
//        options.add("increasing");
//        options.add("constant");
//
//        if (!options.contains(cnf.getString("ban-type")))
//            configWarning("ban-type", ConfigIncorrect.OPTION);
//
//        // custom-ban-command
//        if (!checkBoolean(cnf.getString("custom-ban-command.enable")))
//            configWarning("custom-ban-command.enable", ConfigIncorrect.OPTION);
//
//        // ban-time
//        if (checkInteger(cnf.getString("ban-time"), 0, 525600) == -1)
//            configWarning("ban-time", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("ban-time"), 0, 525600) == 0)
//        {
//            Map<String, Object> placeholders = new HashMap<>();
//            placeholders.put("%min%", 0);
//            placeholders.put("%max%", 525600);
//
//            configWarning("ban-time", ConfigIncorrect.NUMBER_RANGE, placeholders);
//        }
//
//        // ban-rank-time
//        if (!checkBoolean(cnf.getString("ban-rank-time.enable")))
//            configWarning("ban-rank-time.enable", ConfigIncorrect.BOOLEAN);
//
//        cnf.getConfigurationSection("ban-rank-time.ranks").getKeys(false).forEach(key ->
//        {
//            String value = cnf.getString("ban-rank-time.ranks." + key);
//
//            if (checkInteger(value, 0, 525600) == -1)
//                configWarning("ban-rank-time.ranks." + key, ConfigIncorrect.NOT_NUMBER);
//
//            if (checkInteger(value, 0, 525600) == 0)
//                configWarning("ban-rank-time.ranks." + key, ConfigIncorrect.NUMBER_RANGE);
//        });
//
//        // ban-length
//        cnf.getConfigurationSection("ban-length").getKeys(false).forEach(key ->
//        {
//            String value = cnf.getString("ban-length." + key);
//            if (checkInteger(value, 0, 525600) == -1)
//                configWarning("ban-length." + key, ConfigIncorrect.NOT_NUMBER);
//
//            if (checkInteger(value, 0, 525600) == 0)
//                configWarning("ban-length." + key, ConfigIncorrect.NUMBER_RANGE);
//
//        });
//
//        // ban-rank-length
//        if (!checkBoolean(cnf.getString("ban-rank-length.enable")))
//            configWarning("ban-rank-length.enable", ConfigIncorrect.BOOLEAN);
//
//        cnf.getConfigurationSection("ban-rank-length.ranks").getKeys(false).forEach(key ->
//        {
//            cnf.getConfigurationSection("ban-rank-length.ranks." + key).getKeys(false).forEach(key2 ->
//            {
//                String value = cnf.getString("ban-rank-length.ranks." + key + "." + key2);
//
//                if (checkInteger(value, 0, 525600) == -1)
//                    configWarning("ban-rank-length.ranks." + key, ConfigIncorrect.NOT_NUMBER);
//
//                if (checkInteger(value, 0, 525600) == 0)
//                    configWarning("ban-rank-length.ranks." + key, ConfigIncorrect.NUMBER_RANGE);
//
//            });
//        });
//
//        // lowering-ban
//        if (!checkBoolean(cnf.getString("lowering-ban.enable")))
//            configWarning("lowering-ban.enable", ConfigIncorrect.BOOLEAN);
//
//        if (checkInteger(cnf.getString("lowering-ban.time-without-death"), 0, 525600) == -1)
//            configWarning("lowering-ban.time-without-death", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("lowering-ban.time-without-death"), 0, 525600) == 0)
//            configWarning("lowering-ban.time-without-death", ConfigIncorrect.NUMBER_RANGE);
//
//        if (checkInteger(cnf.getString("lowering-ban.lowered-level"), 0, 525600) == -1)
//            configWarning("lowering-ban.lowered-level", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("lowering-ban.lowered-level"), 0, 525600) == 0)
//            configWarning("lowering-ban.lowered-level", ConfigIncorrect.NUMBER_RANGE);
//
//        if (checkInteger(cnf.getString("lowering-ban.max-ban-level"), 0, 525600) == -1)
//            configWarning("lowering-ban.max-ban-level", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("lowering-ban.max-ban-level"), 0, 525600) == 0)
//            configWarning("lowering-ban.max-ban-level", ConfigIncorrect.NUMBER_RANGE);
//
//        // lives
//        if (!checkBoolean(cnf.getString("lives.enable")))
//            configWarning("lives.enable", ConfigIncorrect.BOOLEAN);
//
//        if (!checkBoolean(cnf.getString("lives.toggle-info")))
//            configWarning("lives.toggle-info", ConfigIncorrect.BOOLEAN);
//
//        if (!checkBoolean(cnf.getString("lives.default-visible")))
//            configWarning("lives.default-visible", ConfigIncorrect.BOOLEAN);
//
//        if (checkInteger(cnf.getString("lives.default-count"), 1, 525600) == -1)
//            configWarning("lives.default-count", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("lives.default-count"), 1, 525600) == 0)
//            configWarning("lives.default-count", ConfigIncorrect.NUMBER_RANGE);
//
//        if (checkInteger(cnf.getString("lives.max-count"), 1, 525600) == 0)
//            configWarning("lives.max-count", ConfigIncorrect.NUMBER_RANGE);
//
//        if (!checkBoolean(cnf.getString("lives.renewing-lives.enable")))
//            configWarning("lives.renewing-lives.enable", ConfigIncorrect.BOOLEAN);
//
//        if (checkInteger(cnf.getString("lives.renewing-lives.time-without-death"), 1, 525600) == -1)
//            configWarning("lives.renewing-lives.time-without-death", ConfigIncorrect.NOT_NUMBER);
//
//        if (checkInteger(cnf.getString("lives.renewing-lives.time-without-death"), 1, 525600) == 0)
//            configWarning("lives.renewing-lives.time-without-death", ConfigIncorrect.NUMBER_RANGE);
//
//        // admin-no-ban-after-death
//        if (!checkBoolean(cnf.getString("admin-no-ban-after-death")))
//            configWarning("admin-no-ban-after-death", ConfigIncorrect.BOOLEAN);
//
//        // death-reason-enable
//        if (!checkBoolean(cnf.getString("death-reason-enable")))
//            configWarning("death-reason-enable", ConfigIncorrect.BOOLEAN);
//
//        // death-lightning
//        if (!checkBoolean(cnf.getString("death-lightning")))
//            configWarning("death-lightning", ConfigIncorrect.BOOLEAN);
//
//        // blood
//        if (!checkBoolean(cnf.getString("blood")))
//            configWarning("blood", ConfigIncorrect.BOOLEAN);
    }

    private static int getConfigLine(String key)
    {
        File config = FilesManager.configFile;

        try
        {
            FileInputStream inputStream = new FileInputStream(config);
            Yaml yaml = new Yaml();
            Map<String, Object> configMap = yaml.load(inputStream);

            String finalKey = key.split("\\.")[0];

            if (configMap.containsKey(finalKey))
            {
                int lineNumber = findLineNumber(config, finalKey);
                return lineNumber;
            } else return -1;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    private static int findLineNumber(File file, String targetVariable)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputStream.read(buffer);
            inputStream.close();

            String fileContent = new String(buffer, "UTF-8");
            String[] lines = fileContent.split("\n");

            for (int i = 0; i < lines.length; i++)
            {
                if (lines[i].contains(targetVariable))
                {
                    return i + 1;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        return -1; // Not found
    }

    public static void configWarning(String value,
                                     ConfigIncorrect error,
                                     Map<String, Object> ... placeholders)
    {
        Main.warnLog("Incorrect value in config!");

        String reason = error.getReason();
        if (placeholders.length > 0) reason = StringReplace.string(reason, placeholders[0]);

        Main.warnLog(value + ": [" + getConfigLine(value) + "-config.yml] " + reason);
    }

    private static boolean checkBoolean(String str)
    {
        if (str == null) return false;

        if (str.equals("true") || str.equals("false")) return true;
        else return false;
    }

    private static int checkInteger(String str, int min, int max)
    {
        if (str == null) return -1;

        if (!Numeric.check(str))
        {
            return -1;
        }

        double number = Double.parseDouble(str);

        if (number < min || number > max)
        {
            return 0;
        }

        return 1;
    }

    private enum ConfigIncorrect
    {
        BOOLEAN("A value other than \"true\" or \"false\""),
        NOT_NUMBER("Value is not a number!"),
        NUMBER_RANGE("The given number cannot be less than %min% and greater than %max%"),
        OPTION("Out of range option selected");

        private String reason;

        ConfigIncorrect(String str)
        {
            this.reason = str;
        }

        public String getReason()
        {
            return reason;
        }
    }


    public static void loadConfig()
    {
        YamlConfiguration cnf = FilesManager.getConfigYml();

        // Plugin
        config.CHECK_UPDATES = cnf.getBoolean("check-updates");
        config.LANGUAGE = cnf.getString("language");
        config.CLEARER_LOGS = cnf.getBoolean("clearer-logs");
        config.DATABASE = cnf.getBoolean("database");

        // Configuration
        config.BAN_TYPE = cnf.getString("ban-type");
        config.CUSTOM_BAN_COMMAND_ENABLE = cnf.getBoolean("custom-ban-command.enable");
        config.CUSTOM_BAN_COMMAND_CMD = cnf.get("custom-ban-command.cmd");
        config.RETURN_COMMAND_ENABLE = cnf.getBoolean("return-command.enable");
        config.RETURN_COMMAND_CMD = cnf.get("return-command.cmd");
        config.BAN_TIME = cnf.getDouble("ban-time");
        config.BAN_RANK_TIME_ENABLE = cnf.getBoolean("ban-rank-time.enable");

        SortedMap<String, Double> BAN_RANK_TIME_RANKS = new TreeMap<>(Collections.reverseOrder());
        cnf.getConfigurationSection("ban-rank-time.ranks").getKeys(false).forEach(key ->
        {
            Double value = cnf.getDouble("ban-rank-time.ranks." + key);
            BAN_RANK_TIME_RANKS.put(key, value);
        });
        config.BAN_RANK_TIME_RANKS = BAN_RANK_TIME_RANKS;

        SortedMap<Integer, Double> BAN_LENGTH = new TreeMap<>(Collections.reverseOrder());
        cnf.getConfigurationSection("ban-length").getKeys(false).forEach(key ->
        {
            Double value = cnf.getDouble("ban-length." + key);
            BAN_LENGTH.put(Integer.parseInt(key), value);
        });
        config.BAN_LENGTH = BAN_LENGTH;

        config.BAN_RANK_LENGTH_ENABLE = cnf.getBoolean("ban-rank-length.enable");

        SortedMap<String, SortedMap<Integer, Double>> BAN_RANK_LENGTH_RANKS = new TreeMap<>(Collections.reverseOrder());
        cnf.getConfigurationSection("ban-rank-length.ranks").getKeys(false).forEach(key ->
        {
            SortedMap<Integer, Double> rank = new TreeMap<>(Collections.reverseOrder());
            ConfigurationSection section = cnf.getConfigurationSection("ban-rank-length.ranks." + key);

            section.getKeys(false).forEach(key2 ->
            {
                Double value = section.getDouble(key2);
                rank.put(Integer.parseInt(key2), value);
            });

            BAN_RANK_LENGTH_RANKS.put(key, rank);
        });
        config.BAN_RANK_LENGTH_RANKS = BAN_RANK_LENGTH_RANKS;

        config.LOWERING_BAN_ENABLE = cnf.getBoolean("lowering-ban.enable");
        config.LOWERING_BAN_TIME_WITHOUT_DEATH = cnf.getDouble("lowering-ban.time-without-death");
        config.LOWERING_BAN_LOWERED_LEVEL = cnf.getInt("lowering-ban.lowered-level");
        config.LOWERING_BAN_MAX_BAN_LEVEL = cnf.getInt("lowering-ban.max-ban-level");
        config.LIVES_ENABLE = cnf.getBoolean("lives.enable");
        config.LIVES_TOGGLE_INFO = cnf.getBoolean("lives.toggle-info");
        config.LIVES_DEFAULT_VISIBLE = cnf.getBoolean("lives.default-visible");
        config.LIVES_BAR_INFO_ENABLED = cnf.getBoolean("lives.bar-info-enabled");
        config.LIVES_BAR_INFO = cnf.getStringList("lives.bar-info");
        config.NO_LIVES_INFO_ENABLED = cnf.getBoolean("lives.no-lives-info-enabled");
        config.NO_LIVES_INFO = cnf.getStringList("lives.no-lives-info");
        config.LIVES_DEFAULT_COUNT = cnf.getInt("lives.default-count");
        config.LIVES_MAX_COUNT = cnf.getInt("lives.max-count");
        config.LIVES_AFTER_DEATH = cnf.getInt("lives.after-death");
        config.LIVES_KILLER_GIVE_LIVE = cnf.getBoolean("lives.killer-give-live");
        config.LIVES_RENEWING_LIVES_ENABLE = cnf.getBoolean("lives.renewing-lives.enable");
        config.LIVES_RENEWING_LIVES_TIME_WITHOUT_DEATH = cnf.getDouble("lives.renewing-lives.time-without-death");

        // Admin
        config.ADMIN_NO_BAN_AFTER_DEATH = cnf.getBoolean("admin-no-ban-after-death");

        // Banning
        config.DEATH_REASON_ENABLE = cnf.getBoolean("death-reason-enable");

        // Effects
        config.DEATH_LIGHTNING = cnf.getBoolean("death-lightning");
        config.BLOOD = cnf.getBoolean("blood");
        config.NICK_INFO = cnf.getString("nick-info");

        // Other
        config.TIME_FORMAT = cnf.getString("time-format");
        config.DATE_FORMAT = cnf.getString("date-format");
        config.FULL_DATE_FORMAT = cnf.getString("full-date-format");

        // Leaderboard
        config.LEADERBOARD_EMPTY = cnf.getString("leaderboard-empty");
        config.LEADERBOARD_FORMAT_LIVES = cnf.getString("leaderboard-format-lives");
        config.LEADERBOARD_FORMAT_DEATHS = cnf.getString("leaderboard-format-deaths");
        config.LEADERBOARD_FORMAT_KILLS = cnf.getString("leaderboard-format-kills");
    }

    public static void reload()
    {
        // Create files
        FilesManager.manageFiles();

        // Read files
        FilesManager.loadYmlFiles();

        // Change language
        MessagesManager.setLanguageFile();

        // Load cache
        CacheManager.loadCache();

        // Load config
        ConfigManager.loadConfig();

        // Nick updater
        if (!ConfigManager.config.NICK_INFO.isEmpty()) NickUpdater.update();
        else
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                p.getScoreboard().getTeams().forEach(team ->
                {
                    if (team.getName().startsWith("hardcoremode:"))
                        team.removeEntry(p.getName());
                });
            }
        }
    }
}
