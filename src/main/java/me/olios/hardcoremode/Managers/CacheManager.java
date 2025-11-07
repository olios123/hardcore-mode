package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Objects.Cache;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;

public class CacheManager {

    public static void loadCache()
    {
        YamlConfiguration yml = new YamlConfiguration();

        try
        {
            yml.load(FilesManager.cacheFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        if (yml.get("data") == null) return;

        // Default section
        ConfigurationSection section = yml.getConfigurationSection("data");

        // Renewing lives
        if (section.get("renewing-lives") != null)
        {
            section.getConfigurationSection("renewing-lives").getKeys(false).forEach(key ->
            {
                Integer value = section.getInt("renewing-lives." + key);
                Cache.renewingLives.put(key, value);
            });
        }


        // Lowering bans
        if (section.get("lowering-bans") != null)
        {
            section.getConfigurationSection("lowering-bans").getKeys(false).forEach(key ->
            {
                Integer value = section.getInt("lowering-bans." + key);
                Cache.loweringBans.put(key, value);
            });
        }
    }

    public static void saveCache()
    {
        YamlConfiguration cache = new YamlConfiguration();
        cache.set("data", null);

        // Renewing lives
        for (Map.Entry<String, Integer> entry : Cache.renewingLives.entrySet())
        {
            String key = entry.getKey();
            Integer value = entry.getValue();

            cache.set("data.renewing-lives." + key, value);
        }

        // Lowering bans
        for (Map.Entry<String, Integer> entry : Cache.loweringBans.entrySet())
        {
            String key = entry.getKey();
            Integer value = entry.getValue();

            cache.set("data.lowering-bans." + key, value);
        }

        try
        {
            cache.save(FilesManager.cacheFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
