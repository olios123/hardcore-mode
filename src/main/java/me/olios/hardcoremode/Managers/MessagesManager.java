//
// CC Creative Commons 2022
// Attribution-NoDerivatives 4.0 International
// Author olios
//

package me.olios.hardcoremode.Managers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.olios.hardcoremode.API.PAPICustom;
import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Librrary.Replace.ListReplace;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesManager {

    public static YamlConfiguration languageYml = new YamlConfiguration();
    public static String languageCode = "";

    public static void setLanguageFile()
    {
        languageCode = FilesManager.getConfigYml().getString("language");
        File languageFile = new File(Data.languagesPath + languageCode + ".yml");

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("%language%", languageCode);

        try
        {
            if (languageFile.exists())
            {
                languageYml.load(languageFile);
                assert languageCode != null;
                sendLogMessage(Data.Message.LOADED_LANGUAGE_FILE, placeholders);
            }
            else
            {
                languageYml.load(FilesManager.enFile);
                assert languageCode != null;
                sendLogMessage(Data.Message.LANGUAGE_FILE_NOT_FOUND, placeholders);
            }
        }
        catch(IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }



    // Given player, message, target, placeholders
    public static void sendMessage(Player p,
                                   Data.Message message,
                                   OfflinePlayer target,
                                   Map<String, Object> additionalPlaceholders)
    {
        Object msg = languageYml.get(message.toString().toLowerCase().replace("_", "-"));
        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        additionalPlaceholders.forEach(placeholders::putIfAbsent);

        // Message is list
        if (msg instanceof List<?>)
        {
            List<String> strings = (List<String>) msg;

            // Placeholders
            if (target != null) strings = ListReplace.list(strings, target, placeholders);
            else strings = ListReplace.listPlayer(strings, p, placeholders);

            strings.forEach(p::sendMessage);
        }
        // Message is single string
        else if (msg instanceof String)
        {
            String string = (String) msg;

            // Placeholders
            if (target != null) string = StringReplace.string(string, target, placeholders);
            else string = StringReplace.stringPlayer(string, p, placeholders);

            p.sendMessage(string);
        }
    }

    // Given player, message, player, placeholders
    public static void sendMessagePlayer(Player p,
                                   Data.Message message,
                                   Player target,
                                   Map<String, Object> placeholders)
    {
        sendMessage(p, message, Bukkit.getOfflinePlayer(target.getUniqueId()), placeholders);
    }



    // Given player, message and target
    public static void sendMessage(Player p,
                                   Data.Message message,
                                   OfflinePlayer target)
    {
        sendMessage(p, message, target, new HashMap<>());
    }

    // Given player, message and player
    public static void sendMessage(Player p,
                                   Data.Message message,
                                   Player target)
    {
        sendMessage(p, message, Bukkit.getOfflinePlayer(target.getUniqueId()), new HashMap<>());
    }



    // Given player, message and placeholders
    public static void sendMessage(Player p,
                                   Data.Message message,
                                   Map<String, Object> placeholders)
    {
        sendMessage(p, message, null, placeholders);
    }



    // Given player and message
    public static void sendMessage(Player p,
                                   Data.Message message)
    {
        sendMessage(p, message, null, new HashMap<>());
    }



    public static void sendLogMessage(Data.Message message,
                                      OfflinePlayer target,
                                      Map<String, Object> additionalPlaceholders)
    {
        Object msg = languageYml.get(message.toString().toLowerCase().replace("_", "-"));
        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        additionalPlaceholders.forEach(placeholders::putIfAbsent);

        // Message is list
        if (msg instanceof List<?>)
        {
            List<String> strings = (List<String>) msg;
            List<String> convertedList = new ArrayList<>();

            // Set placeholders for target
            if (target != null && Data.PAPI) strings = PlaceholderAPI.setPlaceholders(target, strings);

            // Placeholders
            String m = "";
            for (String x : strings)
            {
                m = x;
                if (target != null && Data.PAPI) m = PlaceholderAPI.setPlaceholders(target, m);
                for (Map.Entry<String, Object> entry : placeholders.entrySet())
                {
                    m = m.replace(entry.getKey(), String.valueOf(entry.getValue()));
                }

                // Colors
                m = m.replace("&c", Main.ANSI_RED);
                m = m.replace("&a", Main.ANSI_GREEN);
                m = m.replace("&9", Main.ANSI_BLUE);
                m = m.replace("&6", Main.ANSI_GOLD);
                m = m.replace("&f", Main.ANSI_RESET);
                m = m.replace("&r", Main.ANSI_RESET);

                convertedList.add(m);
            }

            convertedList.forEach(Main::log);
        }
        // Message is single string
        else if (msg instanceof String)
        {
            String string = (String) msg;

            // Set placeholders for target
            if (target != null && Data.PAPI) string = PlaceholderAPI.setPlaceholders(target, string);

            // Placeholders
            for (Map.Entry<String, Object> entry : placeholders.entrySet())
            {
                string = string.replace(entry.getKey(), String.valueOf(entry.getValue()));
            }

            // Colors
            string = string.replace("&c", Main.ANSI_RED);
            string = string.replace("&a", Main.ANSI_GREEN);
            string = string.replace("&9", Main.ANSI_BLUE);
            string = string.replace("&6", Main.ANSI_GOLD);
            string = string.replace("&f", Main.ANSI_RESET);
            string = string.replace("&r", Main.ANSI_RESET);

            Main.log(string);
        }
    }

    // Given message and target
    public static void sendLogMessage(Data.Message message,
                                      OfflinePlayer target)
    {
        sendLogMessage(message, target, new HashMap<>());
    }

    // Given message
    public static void sendLogMessage(Data.Message message)
    {
        sendLogMessage(message, null, new HashMap<>());
    }

    // Given message and placeholders
    public static void sendLogMessage(Data.Message message,
                                      Map<String, Object> placeholders)
    {
        sendLogMessage(message, null, placeholders);
    }

    public static Object getMessage(Data.Message message,
                                    Player p,
                                    Map<String, Object> ... additionalPlaceholders)
    {
        Object msg = languageYml.get(message.toString().toLowerCase().replace("_", "-"));
        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        if (additionalPlaceholders.length > 0) additionalPlaceholders[0].forEach(placeholders::putIfAbsent);

        // Message is list
        if (msg instanceof List<?>)
        {
            List<String> strings = (List<String>) msg;

            // Placeholders
            if (p != null)
                strings = ListReplace.list(
                    strings,
                    Bukkit.getOfflinePlayer(p.getUniqueId()),
                    placeholders);
            else
                strings = ListReplace.list(
                        strings,
                        placeholders);

            return strings;
        }
        // Message is single string
        else if (msg instanceof String)
        {
            String string = (String) msg;

            // Placeholders
            if (p != null)
                string = StringReplace.string(
                        string,
                        Bukkit.getOfflinePlayer(p.getUniqueId()),
                        placeholders);
            else
                string = StringReplace.string(
                        string,
                        placeholders);

            return string;
        }

        return null;
    }

    // Given message and placeholders
    public static Object getMessage(Data.Message message,
                                    Map<String, Object> ... additionalPlaceholders)
    {
        return getMessage(message, null, additionalPlaceholders);
    }

    public static String getDeathMessage(Data.DeathReason deathReason,
                                         Map<String, Object> ... additionalPlaceholders)
    {
        String msg = languageYml.getString("death-reason." + deathReason.toString());
        Map<String, Object> placeholders = PAPICustom.getStaticPlaceholders();

        // Add additional placeholders if given
        if (additionalPlaceholders.length > 0) additionalPlaceholders[0].forEach(placeholders::putIfAbsent);

        String string = msg;

        // Placeholders
        string = StringReplace.string(string, placeholders);

        return string;
    }

    public static Object getRawMessage(Data.Message message)
    {
        Object msg = languageYml.get(message.toString().toLowerCase().replace("_", "-"));

        // Message is list
        if (msg instanceof List<?>)
        {
            List<String> strings = (List<String>) msg;
            return strings;
        }
        // Message is single string
        else if (msg instanceof String)
        {
            return msg;
        }

        return null;
    }

    private static List<String> getMessages(String messageCode)
    {
        return languageYml.getStringList(messageCode);
    }

    public static String getLoadedLanguage()
    {
        return FilesManager.getConfigYml().getString("language");
    }

    public static void sendUpdateInfo(Player p)
    {
        TextComponent link = new TextComponent();
        link.setText(StringReplace.string("&a&nspigotmc.org HardcoreMode (click)"));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Data.resourceURL));
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Data.resourceURL)));

        p.spigot().sendMessage(new TextComponent("There is a newer version of HardcoreMode plugin available, you can download it from "), link);
    }

    public static void sendUpdateInfo()
    {
        Main.log(Main.ANSI_RED + "   __  ____  __  __  ___  ___  ___ " + Main.ANSI_GOLD + "  _   _ ___ ___   _ _____ ___ ");
        Main.log(Main.ANSI_RED + "  / / |__ / |  \\/  |/ _ \\|   \\| __|" + Main.ANSI_GOLD + " | | | | _ \\   \\ /_\\_   _| __|");
        Main.log(Main.ANSI_RED + " < <   |_ \\ | |\\/| | (_) | |) | _| " + Main.ANSI_GOLD + " | |_| |  _/ |) / _ \\| | | _| ");
        Main.log(Main.ANSI_RED + "  \\_\\ |___/ |_|  |_|\\___/|___/|___|" + Main.ANSI_GOLD + "  \\___/|_| |___/_/ \\_\\_| |___|");
        Main.log(Main.ANSI_RED + "                                   " + Main.ANSI_GOLD + "                             ");
        Main.log(Main.ANSI_RESET + "The plugin HardcoreMode can be updated. Use this link to download the latest version");
        Main.log(Main.ANSI_GREEN + Data.resourceURL);
        Main.log("");
    }
}
