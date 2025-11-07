package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NickUpdater {

    public static void update()
    {
        Bukkit.getScheduler().runTaskTimer(Data.plugin, () ->
        {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                String prefix = StringReplace.string(ConfigManager.config.NICK_INFO, p);

                Scoreboard scoreboard = p.getScoreboard();
                Team team = scoreboard.getTeam("hardcoremode:" + prefix);
                if (team == null) team = scoreboard.registerNewTeam("hardcoremode:" + prefix);

                team.setPrefix(prefix);

                team.addEntry(p.getName());
            }
        }, 0, 20 * 5); // Every 5 sec
    }

}
