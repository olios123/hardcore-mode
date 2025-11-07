/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Events;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Librrary.BanTime;
import me.olios.hardcoremode.Librrary.ConvertTime;
import me.olios.hardcoremode.Librrary.Replace.ListReplace;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerJoin implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private static void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();

		UserDataManager.createUserData(uuid);

		UserData userData = UserDataManager.load(p.getUniqueId().toString());
		if (userData == null) return;
		double banTime = BanTime.get(p, false);

		if (Data.canUpdate && p.isOp()) MessagesManager.sendUpdateInfo(p);

		if (ConfigManager.config.LIVES_ENABLE && userData.lives > 0)
		{
			userData.lastBan = false;
			UserDataManager.save(userData);
		}

		if (userData.lastBan)
		{
			Map<String, Object> placeholders = new HashMap<>();
			placeholders.put("%time%", ConvertTime.convertTime(banTime).time);

			MessagesManager.sendMessage(p, Data.Message.NEXT_DEATH_INFO_MESSAGE, placeholders);

			if (ConfigManager.config.LIVES_AFTER_DEATH > 0)
			{
				userData.lives = ConfigManager.config.LIVES_AFTER_DEATH;
			}

			userData.lastBan = false;
			UserDataManager.save(userData);

			// Execute custom commands
			if (ConfigManager.config.RETURN_COMMAND_ENABLE)
			{
				// Single string
				if (ConfigManager.config.RETURN_COMMAND_CMD instanceof String)
				{
					String comand = (String) ConfigManager.config.RETURN_COMMAND_CMD;
					comand = StringReplace.string(comand, p);

					// Execute command
					Bukkit.getServer().dispatchCommand(
							Bukkit.getServer().getConsoleSender(), comand);
				}
				// List
				else if (ConfigManager.config.RETURN_COMMAND_CMD instanceof List<?>)
				{
					List<String> commands = (List<String>) ConfigManager.config.RETURN_COMMAND_CMD;
					commands = ListReplace.list(commands, p);

					// Execute commands
					commands.forEach(cmd ->
					{
						Bukkit.getServer().dispatchCommand(
								Bukkit.getServer().getConsoleSender(), cmd);
					});
				}
			}
		}

		// Nick prefix
		if (!ConfigManager.config.NICK_INFO.isEmpty())
		{
			String prefix = StringReplace.string(ConfigManager.config.NICK_INFO, p);

			Scoreboard scoreboard = p.getScoreboard();
			Team team = scoreboard.getTeam("hardcoremode:" + prefix);
			if (team == null) team = scoreboard.registerNewTeam("hardcoremode:" + prefix);

			team.setPrefix(prefix);

			team.addEntry(p.getName());
		}
	}
}
