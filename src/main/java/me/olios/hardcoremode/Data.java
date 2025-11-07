/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
**/

package me.olios.hardcoremode;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class Data {

	// Plugin
	public static Plugin plugin;
	public static File resources;
	public static int resourceId = 104642;
	public static String resourceURL = "https://www.spigotmc.org/resources/hardcoremode.104642/";
	public static String resourceDocs = "https://www.spigotmc.org/resources/hardcoremode.104642//field?field=documentation";
	public static String version = "4.0-BETA";
	public static String pluginVersion = "4.0-BETA";
	public static String discord = "https://discord.gg/kE6eptDu3W";
	public static boolean canUpdate = false;
	public static String SSID;
	// Lists
	public static List<UUID> joinedOpsList = new ArrayList<UUID>();
	public static Map<Integer, Double> bansLength = new HashMap<Integer, Double>();
	public static Map<String, Integer> tasksLoweringBans = new HashMap<String, Integer>();
	public static Map<String, Integer> tasksRenewingLives = new HashMap<String, Integer>();
	public static boolean isStaticBan = false;
	public static List<String> kickList = new ArrayList<>();

	// Paths
	public static String pluginPath = "plugins/HardcoreMode/";
	public static String languagesPath = pluginPath + "languages/";
	public static String userdataPath = pluginPath + "userdata/";
	public static String cachePath = pluginPath + "cache/";

	// Additional plugins
	public static boolean PAPI = false;

	public static String SSID()
	{
		return UUID.randomUUID().toString();
	}

	public enum Message
	{
		ENABLE_PLUGIN,
		DISABLE_PLUGIN,
		LOADED_LANGUAGE_FILE,
		LANGUAGE_FILE_NOT_FOUND,
		RELOAD_COMPLETE,
		LOST_LIFE,
		RENEWED_LIFE,
		TAKEOVER_LIVE,
		TAKEOVER_LIVE_MAX,
		CMD_LIVES_SHOW,
		CMD_LIVES_HIDE,
		CMD_LIVES_SET,
		CMD_LIVES_ADD,
		CMD_LIVES_REMOVE,
		CMD_LIVES_ALL_SET,
		CMD_LIVES_ALL_ADD,
		CMD_LIVES_ALL_REMOVE,
		CMD_LIVES_AMOUNT_IS_NOT_NUMBER,
		CMD_ABOUT,
		CMD_LIVES,
		CMD_LIVES_EXTRA_LIFE,
		CMD_LIVES_BAN_LEVEL,
		CMD_UNBANNED,
		NO_PERMISSIONS,
		CONSOLE_COMMAND,
		PLAYER_RELOAD_COMPLETE,
		ARGUMENT_MISSING,
		CMD_INCORRECT_USE,
		CMD_PLAYER_NOT_FOUND,
		CMD_NOT_BANNED,
		NEXT_DEATH_INFO_MESSAGE,
		BAN_REASON,
		DEATH_PREFIX,
		DEATH_PLAYER_INFO,
		DEATH_WITH_BAN,
		DEATH_WITHOUT_BAN;
	}

	public enum DeathReason
	{
		EXPLOSION,
		CONTACT,
		DRAGON_BREATH,
		DROWNING,
		ENTITY_ATTACK,
		ENTITY_EXPLOSION,
		FALL,
		FALLING_BLOCK,
		FIRE,
		FIRE_TICK,
		FLY_INTO_WALL,
		FREEZE,
		HOT_FLOOR,
		LAVA,
		LIGHTNING,
		MAGIC,
		POISON,
		PROJECTILE,
		SONIC_BOOM,
		STARVATION,
		SUFFOCATION,
		SUICIDE,
		THORNS,
		VOID,
		WITHER,
		OTHER;
	}

	public static DeathReason getDeathReason(String name)
	{
		DeathReason deathReason;

		try
		{
			deathReason = DeathReason.valueOf(name);
		}
		catch (IllegalArgumentException e)
		{
			deathReason = DeathReason.OTHER;
		}

		return deathReason;
	}

	public enum Permission
	{
		ADMIN,
		RELOAD,
		NODEATH,
		LIVES,
		UNBAN
	}

}
