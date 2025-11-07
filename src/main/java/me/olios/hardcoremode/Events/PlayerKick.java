/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Events;

import me.olios.hardcoremode.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKick implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private static void onPlayerKick(PlayerKickEvent e)
	{
		Player p = e.getPlayer();

		if (Data.kickList.contains(p.getUniqueId().toString()))
		{
			e.setLeaveMessage("");
			Data.kickList.remove(p.getUniqueId().toString());
		}
	}

}
