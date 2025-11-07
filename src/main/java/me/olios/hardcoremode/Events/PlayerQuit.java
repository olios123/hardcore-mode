package me.olios.hardcoremode.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public static void onPlayerQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();

        // Remove from scoreboard
        p.getScoreboard().getTeams().forEach(team ->
        {
            if (team.getName().startsWith("hardcoremode:"))
                team.removeEntry(p.getName());
        });
    }

}
