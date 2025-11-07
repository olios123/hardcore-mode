/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Data;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FilesManager {

    public static boolean firstStart = false;

    // Folders
    public static final File languageFolder = new File(Data.languagesPath);
    public static final File userdataFolder = new File(Data.userdataPath);
    public static final File cacheFolder = new File(Data.cachePath);

    // Files
    public static File configFile = new File(Data.pluginPath, "config.yml");
    public static File databaseFile = new File(Data.pluginPath, "database.yml");
    public static File enFile = new File(languageFolder, "en.yml");
    public static File esFile = new File(languageFolder, "es.yml");
    public static File plFile = new File(languageFolder, "pl.yml");
    public static File frFile = new File(languageFolder, "fr.yml");
    public static File itFile = new File(languageFolder, "it.yml");
    public static File deFile = new File(languageFolder, "de.yml");
    public static File cacheFile = new File(cacheFolder, "data.yml");

    // YamlConfigurations
    private static final YamlConfiguration configYml = new YamlConfiguration();
    private static final YamlConfiguration databaseYml = new YamlConfiguration();
    private static final YamlConfiguration enYml = new YamlConfiguration();
    private static final YamlConfiguration esYml = new YamlConfiguration();
    private static final YamlConfiguration plYml = new YamlConfiguration();
    private static final YamlConfiguration frYml = new YamlConfiguration();
    private static final YamlConfiguration itYml = new YamlConfiguration();
    private static final YamlConfiguration deYml = new YamlConfiguration();

    public static void manageFiles()
    {
        if (!new File(Data.pluginPath).exists())
        {
            firstStart = true;
        }

        if (!languageFolder.exists()) languageFolder.mkdirs();
        if (!userdataFolder.exists()) userdataFolder.mkdirs();
        if (!cacheFolder.exists()) cacheFolder.mkdirs();
    }

    public static void loadYmlFiles()
    {
        try
        {
            configYml.load(configFile);
            databaseYml.load(databaseFile);

            if (enFile.exists()) enYml.load(enFile);
            if (esFile.exists()) esYml.load(esFile);
            if (plFile.exists()) plYml.load(plFile);
            if (frFile.exists()) frYml.load(frFile);
            if (itFile.exists()) itYml.load(itFile);
            if (deFile.exists()) deYml.load(deFile);
        }
        catch(IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getConfigYml()
    {
        return configYml;
    }
    public static YamlConfiguration getDatabaseYml() { return databaseYml; }
}
