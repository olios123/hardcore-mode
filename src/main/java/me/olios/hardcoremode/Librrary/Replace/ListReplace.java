package me.olios.hardcoremode.Librrary.Replace;

import me.clip.placeholderapi.PlaceholderAPI;
import me.olios.hardcoremode.API.PAPICustom;
import me.olios.hardcoremode.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListReplace {

    // Given list, player and placeholders
    public static List<String> list(List<String> list,
                                    OfflinePlayer target,
                                    Map<String, Object> additionalPlaceholders)
    {
        List<String> convertedList = new ArrayList<>();

        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        additionalPlaceholders.forEach(placeholders::putIfAbsent);

        String msg = "";
        for (String x : list)
        {
            msg = x;
            if (target != null && Data.PAPI) msg = PlaceholderAPI.setPlaceholders(target, msg);
            for (Map.Entry<String, Object> entry : placeholders.entrySet())
            {
                msg = msg.replace(entry.getKey(), String.valueOf(entry.getValue()));
            }
            convertedList.add(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return convertedList;
    }

    // Given list, player and placeholders
    public static List<String> listPlayer(List<String> list,
                                    Player target,
                                    Map<String, Object> placeholoders)
    {
        return list(list, Bukkit.getOfflinePlayer(target.getUniqueId()), placeholoders);
    }



    // Given list and target
    public static List<String> list(List<String> list,
                                    OfflinePlayer target)
    {
        return list(list, target, new HashMap<>());
    }

    // Given list and player
    public static List<String> list(List<String> list,
                                    Player target)
    {
        return list(list, Bukkit.getOfflinePlayer(target.getUniqueId()), new HashMap<>());
    }



    // Given list and placeholders
    public static List<String> list(List<String> list,
                                    Map<String, Object> placeholders)
    {
        return list(list, null, placeholders);
    }



    // Given only list
    public static List<String> list (List<String>list)
    {
        return list(list, null, new HashMap<>());
    }
}
