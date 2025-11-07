package me.olios.hardcoremode.Debug;

import me.olios.hardcoremode.API.PAPICustom;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class DEBUGManager implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private static void onPlayerChat(AsyncPlayerChatEvent e)
    {
        String msg = e.getMessage();
        Player p = e.getPlayer();

        if (!p.getUniqueId().toString().equals("6c3f4e4b-7040-3b51-9216-95930800b3ee")) return;

        /**
         * Comment this to disable a debug option
         * Below debug commands
         */
        switch (msg)
        {
            case "debug placeholders":
                e.setCancelled(true);

                Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

                for (Map.Entry<String, Object> entry : placeholders.entrySet())
                {
                    p.sendMessage(ChatColor.GREEN + entry.getKey() + " \n" + ChatColor.BLUE +
                            StringReplace.string(entry.getKey(), placeholders));
                }

                break;
        }
    }

}
