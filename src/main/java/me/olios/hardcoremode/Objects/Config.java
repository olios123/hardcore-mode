package me.olios.hardcoremode.Objects;

import java.util.List;
import java.util.SortedMap;

public class Config {

    // Plugin
    public static boolean CHECK_UPDATES;
    public static String LANGUAGE;
    public static boolean CLEARER_LOGS;
    public static boolean DATABASE;

    // Configuration
    public static String BAN_TYPE;
    public static boolean CUSTOM_BAN_COMMAND_ENABLE;
    public static Object CUSTOM_BAN_COMMAND_CMD;
    public static boolean RETURN_COMMAND_ENABLE;
    public static Object RETURN_COMMAND_CMD;
    public static double BAN_TIME;
    public static boolean BAN_RANK_TIME_ENABLE;
    public static SortedMap<String, Double> BAN_RANK_TIME_RANKS;
    public static SortedMap<Integer, Double> BAN_LENGTH;
    public static boolean BAN_RANK_LENGTH_ENABLE;
    public static SortedMap<String, SortedMap<Integer, Double>> BAN_RANK_LENGTH_RANKS;
    public static boolean LOWERING_BAN_ENABLE;
    public static double LOWERING_BAN_TIME_WITHOUT_DEATH;
    public static int LOWERING_BAN_LOWERED_LEVEL;
    public static int LOWERING_BAN_MAX_BAN_LEVEL;
    public static boolean LIVES_ENABLE;
    public static boolean LIVES_TOGGLE_INFO;
    public static boolean LIVES_DEFAULT_VISIBLE;
    public static boolean LIVES_BAR_INFO_ENABLED;
    public static List<String> LIVES_BAR_INFO;
    public static boolean NO_LIVES_INFO_ENABLED;
    public static List<String> NO_LIVES_INFO;
    public static int LIVES_DEFAULT_COUNT;
    public static int LIVES_MAX_COUNT;
    public static int LIVES_AFTER_DEATH;
    public static boolean LIVES_KILLER_GIVE_LIVE;
    public static boolean LIVES_RENEWING_LIVES_ENABLE;
    public static double LIVES_RENEWING_LIVES_TIME_WITHOUT_DEATH;

    // Admin
    public static boolean ADMIN_NO_BAN_AFTER_DEATH;

    // Banning
    public static boolean DEATH_REASON_ENABLE;

    // Effects
    public static boolean DEATH_LIGHTNING;
    public static boolean BLOOD;
    public static String NICK_INFO;

    // Other
    public static String TIME_FORMAT;
    public static String DATE_FORMAT;
    public static String FULL_DATE_FORMAT;

    // Leaderboard
    public static String LEADERBOARD_EMPTY;
    public static String LEADERBOARD_FORMAT_LIVES;
    public static String LEADERBOARD_FORMAT_DEATHS;
    public static String LEADERBOARD_FORMAT_KILLS;
}
