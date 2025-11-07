/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDataManager implements Listener
{

    public static void createUserData(String uuid)
    {
        File userDataFile = new File(Data.userdataPath, uuid + ".yml");
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

        if (userDataFile.exists()) return;

        try
        {
            if (userDataFile.createNewFile())
            {
                UserData userData = new UserData();

                userData.uuid = uuid;
                userData.username = offlinePlayer.getName();
                userData.lastBan = false;
                userData.deathLevel = 1;
                if (ConfigManager.config.LIVES_ENABLE)
                    userData.lives = ConfigManager.config.LIVES_DEFAULT_COUNT;
                else userData.lives = 0;
                userData.livesInfo = ConfigManager.config.LIVES_DEFAULT_VISIBLE;

                save(userData);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void save(UserData userData)
    {
        File userDataFile = new File(Data.userdataPath, userData.uuid + ".yml");
        YamlConfiguration yml = new YamlConfiguration();

        try
        {
            yml.load(userDataFile);

            yml.set("uuid", userData.uuid);
            yml.set("username", userData.username);
            yml.set("last-ban", userData.lastBan);
            yml.set("death-level", userData.deathLevel);
            yml.set("lives", userData.lives);
            yml.set("lives-info", userData.livesInfo);

            yml.save(userDataFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public static UserData load(String uuid)
    {
        File userDataFile = new File(Data.userdataPath, uuid + ".yml");
        if (!userDataFile.exists()) return null;

        YamlConfiguration yml = new YamlConfiguration();

        UserData userData = new UserData();

        try
        {
            yml.load(userDataFile);

            userData.uuid = yml.getString("uuid");
            userData.username = yml.getString("username");
            userData.lastBan = yml.getBoolean("last-ban");
            userData.deathLevel = yml.getInt("death-level");
            userData.lives = yml.getInt("lives");
            userData.livesInfo = yml.getBoolean("lives-info");
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        return userData;
    }

    public static List<UserData> getAllUserData()
    {
        List<UserData> userDataList = new ArrayList<>();
        List<OfflinePlayer> allPlayers = getAllPlayers();

        for (OfflinePlayer player : allPlayers)
        {
            String uuid = player.getUniqueId().toString();
            createUserData(uuid);

            userDataList.add(load(uuid));
        }

        return userDataList;
    }

    private static List<OfflinePlayer> getAllPlayers()
    {
        List<OfflinePlayer> players = new ArrayList<>();
        File playerDataFolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata");

        if (playerDataFolder.exists() && playerDataFolder.isDirectory())
        {
            for (File file : playerDataFolder.listFiles())
            {
                String fileName = file.getName();
                try
                {
                    UUID uuid = UUID.fromString(fileName.substring(0, fileName.length() - 4));
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    players.add(player);
                }
                catch (IllegalArgumentException e)
                {
                    // Empty
                }
            }
        }

        return players;
    }

//    private static YamlConfiguration userdataFileYml = new YamlConfiguration();
//    private static File userdataFile = null;
////
////    private static List<String> userdataFileVariables()
////    {
////        List<String> variables = new ArrayList<>();
////        variables.add("uuid");
////        variables.add("username");
////        variables.add("last-ban");
////        variables.add("death-level");
////        variables.add("lives");
////        return variables;
////    }
//
//    public static void createUserData(Player p)
//    {
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//
//        if (userdataFile.exists()) return;
//        if (!FilesManager.userdataFolder.exists()) return;
//
//        try
//        {
//            if (userdataFile.createNewFile())
//            {
//                userdataFileYml = new YamlConfiguration();
//
//                userdataFileYml.set("uuid", p.getUniqueId().toString());
//                userdataFileYml.set("username", p.getName());
//                userdataFileYml.set("last-ban", false);
//                userdataFileYml.set("death-level", 0);
//
//                YamlConfiguration cnf = FilesManager.getConfigYml();
//                if (cnf.getBoolean("lives.enable") && cnf.getInt("lives.default-count") > 0)
//                {
//                    userdataFileYml.set("lives", cnf.getInt("lives.default-count"));
//                } else userdataFileYml.set("lives", 0);
//                userdataFileYml.set("lives-info", cnf.get("lives.default-visible"));
//            }
//            save(p);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static YamlConfiguration getUserDataYml(Player p)
//    {
//        createUserData(p);
//        userDataCheck();
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//        if (!FilesManager.userdataFolder.exists()) return new YamlConfiguration();
//        try
//        {
//            userdataFileYml.load(userdataFile);
//        }catch(IOException | InvalidConfigurationException e)
//        {
//            e.printStackTrace();
//        }
//
//        return userdataFileYml;
//    }
//    public static void save(String uuid, YamlConfiguration yamlConfiguration)
//    {
//        File file = new File(FilesManager.userdataFolder, uuid + ".yml");
//        if (!FilesManager.userdataFolder.exists()) return;
//        try
//        {
//            yamlConfiguration.save(file);
//        }catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    private void playerJoinServer(PlayerJoinEvent e)
//    {
//        Player p = e.getPlayer();
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//        if (!FilesManager.userdataFolder.exists()) return;
//        createUserData(p);
//    }
//
//    // OfflinePlayer
//    public static void createUserData(OfflinePlayer p)
//    {
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//
//        if (userdataFile.exists()) return;
//        if (!FilesManager.userdataFolder.exists()) return;
//        try
//        {
//            if (userdataFile.createNewFile())
//            {
//                userdataFileYml = new YamlConfiguration();
//
//                userdataFileYml.set("uuid", p.getUniqueId().toString());
//                userdataFileYml.set("username", p.getName());
//                userdataFileYml.set("last-ban", false);
//                userdataFileYml.set("death-level", 0);
//
//                YamlConfiguration cnf = FilesManager.getConfigYml();
//                if (cnf.getBoolean("lives.enable") && cnf.getInt("lives.default-count") > 0)
//                {
//                    userdataFileYml.set("lives", cnf.getInt("lives.default-count"));
//                } else userdataFileYml.set("lives", 0);
//                userdataFileYml.set("lives-info", cnf.get("lives.default-visible"));
//            }
//
//            save(p);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static YamlConfiguration getUserDataYml(OfflinePlayer p)
//    {
//        createUserData(p);
//        userDataCheck();
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//        if (!FilesManager.userdataFolder.exists()) return new YamlConfiguration();
//
//        try
//        {
//            userdataFileYml.load(userdataFile);
//        }
//        catch(IOException | InvalidConfigurationException e)
//        {
//            e.printStackTrace();
//        }
//
//        return userdataFileYml;
//    }
//
//    private static void save(OfflinePlayer p)
//    {
//        userdataFile = new File(FilesManager.userdataFolder, p.getUniqueId() + ".yml");
//        if (!FilesManager.userdataFolder.exists()) return;
//        try
//        {
//            userdataFileYml.save(userdataFile);
//        }catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void userDataCheck()
//    {
//        File[] files = FilesManager.userdataFolder.listFiles();
//        if (!FilesManager.userdataFolder.exists()) return;
//        for (File file : files)
//        {
//            if (file.isDirectory()) continue;
//            if (!file.getName().endsWith(".yml")) continue;
//
//            try
//            {
//                YamlConfiguration userdata = new YamlConfiguration();
//                YamlConfiguration cnf = FilesManager.getConfigYml();
//                userdata.load(file);
//
//                if (userdata.get("last-ban") == null) userdata.set("last-ban", false);
//                if (userdata.get("death-level") == null) userdata.set("death-level", 0);
//                if (userdata.get("lives") == null)
//                {
//                    if (cnf.getBoolean("lives.enable") && cnf.getInt("lives.default-count") > 0)
//                    {
//                        userdata.set("lives", cnf.getInt("lives.default-count"));
//                    } else userdata.set("lives", 0);
//                }
//                if (userdata.get("lives-info") == null) userdata.set("lives-info", cnf.get("lives.default-visible"));
//
//                userdata.save(file);
//            }
//            catch (IOException | InvalidConfigurationException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
}
