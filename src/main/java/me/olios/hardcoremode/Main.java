/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode;

import me.olios.hardcoremode.API.PAPIExpansion;
import me.olios.hardcoremode.Database.MySQL;
import me.olios.hardcoremode.Debug.DEBUGManager;
import me.olios.hardcoremode.Events.*;
import me.olios.hardcoremode.Librrary.Lives;
import me.olios.hardcoremode.Librrary.LoweringBan;
import me.olios.hardcoremode.Librrary.NickUpdater;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Main extends JavaPlugin implements Listener {

	// Console colors
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_GOLD = "\u001B[33m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[93m";
	public static final String ANSI_BLUE = "\u001B[34m";

	public static void l(Object obj)
	{
		Bukkit.getConsoleSender().sendMessage(ANSI_GREEN + "[HardcoreMode | LOG] " + obj);
	}
	public static void log(String msg) {
		if (FilesManager.getConfigYml().getBoolean("clearer-logs"))
		{
			Bukkit.getConsoleSender().sendMessage(ANSI_GREEN + "[HardcoreMode | INFO] " + msg);
			return;
		}

		Bukkit.getConsoleSender().sendMessage("[HardcoreMode | INFO] " + msg);
	}
	public static void errLog(String msg)
	{
		Bukkit.getConsoleSender().sendMessage(ANSI_RED + "[HardcoreMode | ERROR] " + msg);
	}
	public static void warnLog(String msg)
	{
		Bukkit.getConsoleSender().sendMessage(ANSI_YELLOW + "[HardcoreMode | WARN] " + msg);
	}

	@Override
	public void onEnable()
	{
		Data.plugin = this;
		Data.resources = getDataFolder();
		Data.SSID = Data.SSID();

		getServer().getPluginManager().registerEvents(new PlayerDeath(), Data.plugin);
		getServer().getPluginManager().registerEvents(new UserDataManager(), Data.plugin);
		getServer().getPluginManager().registerEvents(new PlayerKick(), Data.plugin);
		getServer().getPluginManager().registerEvents(new PlayerJoin(), Data.plugin);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), Data.plugin);
		getServer().getPluginManager().registerEvents(new EntityDamage(), Data.plugin);
		getServer().getPluginManager().registerEvents(new DEBUGManager(), Data.plugin);

		// Check if plugin PlaceholderAPI is installed on server
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
			Bukkit.getPluginManager().registerEvents(this, this);
			new PAPIExpansion().register();
			Main.log("PlaceholderAPI plugin found, placeholders registered.");
			Data.PAPI = true;
		}
		else
		{
			// Plugin PlaceholderAPI was not found
			Main.errLog("");
			Main.errLog("Download the PlaceholderAPI plugin and add it to your server. ");
			Main.errLog("Placeholders won't work without it!");
			Main.errLog("Download link -> https://www.spigotmc.org/resources/placeholderapi.6245/");
			Main.errLog("");
		}

		getVersion();

		// Files configuration
		FilesManager.manageFiles();
		if (!FilesManager.configFile.exists()) saveResource("config.yml", false);
		FilesManager.languageFolder.mkdirs();
		if (!FilesManager.enFile.exists()) saveResource("languages/" + "en.yml", false);
		if (!FilesManager.esFile.exists()) saveResource("languages/" + "es.yml", false);
		if (!FilesManager.plFile.exists()) saveResource("languages/" + "pl.yml", false);
		if (!FilesManager.frFile.exists()) saveResource("languages/" + "fr.yml", false);
		if (!FilesManager.itFile.exists()) saveResource("languages/" + "it.yml", false);
		if (!FilesManager.deFile.exists()) saveResource("languages/" + "de.yml", false);
		if (!FilesManager.cacheFile.exists()) saveResource("cache/" + "data.yml", false);
		if (!FilesManager.databaseFile.exists()) saveResource("database.yml", false);
		FilesManager.loadYmlFiles();
		saveDefaultConfig();
		MessagesManager.setLanguageFile();

		// Load config and check if everything is ok
		// Check config correctness
		File localConfig = getConfigLocal();
		AtomicBoolean missing = new AtomicBoolean(false);
		if (localConfig.exists())
		{
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(localConfig);
			if (yml != null)
			{
				YamlConfiguration config = FilesManager.getConfigYml();
				List<String> keys = config.getConfigurationSection("").getKeys(false).stream().toList();

				yml.getConfigurationSection("").getKeys(false).forEach(key ->
				{
					if (!keys.contains(key)) missing.set(true);
				});

				localConfig.delete();
			}
		}
		ConfigManager.loadConfig();

		// Load cache
		CacheManager.loadCache();

		// Update Checker
		if (ConfigManager.config.CHECK_UPDATES)
		{
			new UpdateChecker(this, Data.resourceId).getVersion(version ->
			{
				if (!Data.plugin.getDescription().getVersion().equals(version))
				{
					// Plugin can be updated
					Data.canUpdate = true;

					// Info about update (console)
					MessagesManager.sendUpdateInfo();

					// Info about update for players is they are admins
					for (Player player : Bukkit.getServer().getOnlinePlayers())
					{
						if (player.isOp())
						{
							// Info about update (player)
							MessagesManager.sendUpdateInfo(player);
						}
					}
				}
			});
		}

		Main.log("");
		Main.log(ANSI_RED + "   ##  #######  " + ANSI_RESET + "|");
		Main.log(ANSI_RED + "  ##  ##     ## " + ANSI_RESET + "|");
		Main.log(ANSI_RED + " ##          ## " + ANSI_RESET + "| Version: " + ANSI_GREEN + Data.pluginVersion);
		Main.log(ANSI_RED + "##     #######  " + ANSI_RESET + "| SSID: " + ANSI_GREEN + Data.SSID);
		Main.log(ANSI_RED + " ##          ## " + ANSI_RESET + "| Server: " + ANSI_GREEN + Bukkit.getServer().getBukkitVersion());
		Main.log(ANSI_RED + "  ##  ##     ## " + ANSI_RESET + "|");
		Main.log(ANSI_RED + "   ##  #######  " + ANSI_RESET + "|");
		Main.log("");

		// Some variables are missing
		if (missing.get())
		{
			Main.log(
					"\n" + ANSI_RED + "\n+----------------------------------------------------------+\n" +
					"|                      <3 HardcoreMode                     |\n" +
					"|                                                          |\n" +
					"|         An error was found in the config.yml file        |\n" +
					"+----------------------------------------------------------+\n" +
					"|    Values are missing in the main configuration file.    |\n" +
					"|    This may be due to file corruption or updating to     |\n" +
					"| a newer version of the plugin. Re-create the config.yml  |\n" +
					"|  file by deleting it and restarting the server or copy   |\n" +
					"|              the missing values from GitHub.             |\n" +
					"+----------------------------------------------------------+" + ANSI_RESET + "\n");
		}

		// Checking for BETA version of a plugin
		if (Data.pluginVersion.contains("BETA"))
		{
			// Send message to OP-s
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				// Check if OP
				if (player.isOp())
				{
					player.sendMessage(StringReplace.string("&6<3 HardcoreMode - WARNING\n" +
							"&6You are using the BETA version of the HardcoreMode plugin, " +
							"&6be sensitive to errors and possible incorrect functioning of the plugin. " +
							"&6If such a situation occurs, it is recommended to use the latest version" +
							"&6of the plugin without the \"BETA\" tag."));
				}
			}

			// Send message to console
			Main.log(ANSI_GOLD + "<3 HardcoreMode - WARNING");
			Main.log(ANSI_GOLD + "You are using the BETA version of the HardcoreMode plugin,");
			Main.log(ANSI_GOLD + "be sensitive to errors and possible incorrect functioning of the plugin.");
			Main.log(ANSI_GOLD + "If such a situation occurs, it is recommended to use the latest version");
			Main.log(ANSI_GOLD + "of the plugin without the \"BETA\" tag.");
		}


		// Lives system
		if (FilesManager.getConfigYml().getBoolean("lives.enable"))
		{
			Lives.enable();
			if (FilesManager.getConfigYml().getBoolean("lives.renewing-lives.enable")) Lives.run();
		}

		// Lowering bans
		if (ConfigManager.config.LOWERING_BAN_ENABLE)
		{
			LoweringBan.run();
		}

		// Database
		if (ConfigManager.config.DATABASE) MySQL.connect();

		// Nick updater
		if (!ConfigManager.config.NICK_INFO.isEmpty()) NickUpdater.update();

		MessagesManager.sendLogMessage(Data.Message.ENABLE_PLUGIN);
	}

	@Override
	public void onDisable()
	{
		MessagesManager.sendLogMessage(Data.Message.DISABLE_PLUGIN);

		// Save cache
		CacheManager.saveCache();

		// Remove all teams from players
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
		// Check if not empty
		if (!scoreboard.getTeams().isEmpty())
		{
			// Get every team
			for (Team team : scoreboard.getTeams())
			{
				// If start with "hardcoremode:" remove it
				if (team.getName().startsWith("hardcoremode:"))
				{
					team.unregister();
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		CommandsManager.manageCommand(sender, cmd, label, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		return CommandsManager.manageCompleter(sender, cmd, alias, args);
	}

	public void getVersion()
	{
		Data.version = Bukkit.getVersion().split(" ")[2]
				.replace(")", "");
	}

	private File getConfigLocal()
	{
		InputStream is = getResource("config.yml");

		try
		{
			File tempFile = File.createTempFile("tempConfig", ".yml");

			try (OutputStream os = new FileOutputStream(tempFile))
			{
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1)
				{
					os.write(buffer, 0, bytesRead);
				}
			}
			return tempFile;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
