package me.olios.hardcoremode.Librrary.Replace;

import me.clip.placeholderapi.PlaceholderAPI;
import me.olios.hardcoremode.API.PAPICustom;
import me.olios.hardcoremode.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StringReplace {

    // Given string, player and placeholders
    public static String string(String string,
                                OfflinePlayer target,
                                Map<String, Object> additionalPlaceholders)
    {
        String stringToReplace = "";

        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        additionalPlaceholders.forEach(placeholders::putIfAbsent);

        String temp = string;
        if (target != null && Data.PAPI) temp = PlaceholderAPI.setPlaceholders(target, temp);
        for (Map.Entry<String, Object> entry : placeholders.entrySet())
        {
            temp = temp.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }
        stringToReplace = ChatColor.translateAlternateColorCodes('&', temp);

        return stringToReplace;
    }

    // Given string, player and placeholders
    public static String stringPlayer(String string,
                                Player target,
                                Map<String, Object> placeholders)
    {
        return string(string, Bukkit.getOfflinePlayer(target.getUniqueId()), placeholders);
    }



    // Given string and target
    public static String string(String string,
                                OfflinePlayer target)
    {
        return string(string, target, new HashMap<>());
    }

    // Given string and player
    public static String string(String string,
                                Player target)
    {
        return string(string, Bukkit.getOfflinePlayer(target.getUniqueId()), new HashMap<>());
    }



    // Given string and placeholders
    public static String string(String string,
                                Map<String, Object> placeholders)
    {
        return string(string, null, placeholders);
    }



    // Given only string
    public static String string(String string)
    {
        return string(string, null, new HashMap<>());
    }
}
