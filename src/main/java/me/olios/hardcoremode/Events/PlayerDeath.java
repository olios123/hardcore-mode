/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Events;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Database.MySQL;
import me.olios.hardcoremode.Librrary.BanTime;
import me.olios.hardcoremode.Librrary.ConvertTime;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Cache;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerDeath(PlayerDeathEvent e)
    {
        // Get info about damage
        Entity entity = e.getEntity();
        assert entity != null;
        EntityDamageEvent entityDamageEvent = entity.getLastDamageCause();
        assert entityDamageEvent != null;
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        assert damageCause != null;

        // Player and recalculate permissions
        Player p = e.getEntity();
        String uuid = p.getUniqueId().toString();
        p.recalculatePermissions();

        // Killer
        Entity killerEntity = p.getKiller();
        Entity damager = getEntityDamager(entityDamageEvent);
        String killer = "";

        // Player
        if (killerEntity != null) killer = killerEntity.getName();
        // Damage by mob
        else if (damager != null)
        {
            String mobType = damager.getType().name().toLowerCase();
            killer = mobType.substring(0, 1).toUpperCase() + mobType.substring(1);
        }
        else killer = "Unknown";


        // Clear bleeding
        for (String key : EntityDamage.playerBleedingMap.keySet())
        {
            if (!key.equals(uuid)) continue;

            List<Integer> values = EntityDamage.playerBleedingMap.get(key);

            for (Integer v : values)
            {
                BukkitTask taskToCancel = Bukkit.getScheduler().getPendingTasks()
                        .stream()
                        .filter(task -> task.getTaskId() == v)
                        .findFirst()
                        .orElse(null);

                EntityDamage.cancelBleedingTask(taskToCancel);
            }
        }
        EntityDamage.playerBleedingMap.removeAll(uuid);


        // Giving killer live
        if (ConfigManager.config.LIVES_ENABLE && ConfigManager.config.LIVES_KILLER_GIVE_LIVE)
        {
            if (p.getKiller() != null)
            {
                Player killerPlayer = (Player) p.getKiller();

                UserData userData = UserDataManager.load(killerPlayer.getUniqueId().toString());
                if (userData.lives >= ConfigManager.config.LIVES_MAX_COUNT) // Killer max lives
                {
                    MessagesManager.sendMessage(killerPlayer,
                            Data.Message.TAKEOVER_LIVE_MAX,
                            p);
                }
                else
                {
                    userData.lives += 1;

                    UserDataManager.save(userData);

                    MessagesManager.sendMessage(killerPlayer,
                            Data.Message.TAKEOVER_LIVE,
                            p);
                }
            }
        }


        // Death message
        String deathMsg = e.getDeathMessage();
        String deathMessage = "";
        List<String> reason;

        String deathReason = damageCause.toString();
        Data.DeathReason dReason = Data.getDeathReason(deathReason);

        // Ban time
        Double banTime = BanTime.get(p, true);

        UserData userData = UserDataManager.load(p.getUniqueId().toString());

        // MYSQL Variables
        String MYSQL_uuid = p.getUniqueId().toString();
        String MYSQL_name = p.getName();
        String MYSQL_displayName = p.getDisplayName();
        boolean MYSQL_banned = false;
        int MYSQL_deathLevelBefore = userData.deathLevel;
        int MYSQL_deathLevelNow = -1;
        int MYSQL_livesBefore = userData.lives + 1;
        int MYSQL_livesNow = userData.lives;
        int MYSQL_banStartDate = -1;
        int MYSQL_banEndDate = -1;
        double MYSQL_banTime = -1;
        String MYSQL_banReason = null; // TODO
        // MYSQL Variables

        // Settings from config
        if (ConfigManager.config.DEATH_REASON_ENABLE) e.setDeathMessage(null);
        if (ConfigManager.config.DEATH_LIGHTNING)
            p.getWorld().strikeLightningEffect(p.getLocation());

        // Lives and renewing
        if (ConfigManager.config.LIVES_ENABLE && ConfigManager.config.LIVES_RENEWING_LIVES_ENABLE)
        {
            int timestamp = (int) (new Date().getTime() / 1000);
            int timeWithoutDeath = (int) ConfigManager.config.LIVES_RENEWING_LIVES_TIME_WITHOUT_DEATH * 60;

            // Break renewing lives
            Map<String, Integer> map = Cache.renewingLives;
            if (map.containsKey(uuid))
            {
                map.replace(uuid, map.get(uuid), timestamp + timeWithoutDeath);
            }
            else map.put(uuid, timestamp + timeWithoutDeath);
            Cache.renewingLives = map;
//            Data.saveRenewingLives(map);
        }

        // Cancel lowering ban task
        if (Data.tasksLoweringBans.containsKey(uuid))
            Bukkit.getScheduler().cancelTask(Data.tasksLoweringBans.get(uuid));

        // Cancel renewing lives task
        if (Data.tasksRenewingLives.containsKey(uuid))
            Bukkit.getScheduler().cancelTask(Data.tasksRenewingLives.get(uuid));

        // Ban player
        if (banTime > 0)
        {
            MYSQL_banned = true;
            MYSQL_banTime = banTime;

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, banTime.intValue());
            calendar.add(Calendar.SECOND, ConvertTime.secTime(banTime));
            Date date = calendar.getTime();

            // Formats
            SimpleDateFormat sdfFullDate = new SimpleDateFormat(ConfigManager.config.FULL_DATE_FORMAT);
            SimpleDateFormat sdfDate = new SimpleDateFormat(ConfigManager.config.DATE_FORMAT);
            SimpleDateFormat sdfTime = new SimpleDateFormat(ConfigManager.config.TIME_FORMAT);

            // Reason
            Map<String, Object> placeholders = new HashMap<>();
            placeholders.put("%time%", ConvertTime.convertTime(banTime).time);
            placeholders.put("%date%", sdfDate.format(date));
            placeholders.put("%full-date%", sdfFullDate.format(date));

            MYSQL_banStartDate = Integer.parseInt(String.valueOf(new Date().getTime() / 1000));
            MYSQL_banEndDate = Integer.parseInt(String.valueOf(date.getTime() / 1000));

            reason = (List<String>) MessagesManager.getMessage(
                    Data.Message.BAN_REASON,
                    placeholders);

            String finalReason = "\n";
            for (String x : reason) finalReason += x + "\n";

            placeholders.clear();
            placeholders.put("%player%", p.getName());
            placeholders.put("%killer%", killer);

            String deathBanReason =
                    MessagesManager.getMessage(Data.Message.DEATH_PLAYER_INFO, placeholders) +
                            " " +
                    MessagesManager.getDeathMessage(dReason, placeholders);

            MYSQL_banReason = deathBanReason;

            placeholders.put("%reason%", deathBanReason);
            finalReason = StringReplace.string(
                    finalReason,
                    Bukkit.getOfflinePlayer(UUID.fromString(uuid)),
                    placeholders
            );

            // Ban player
            // Use custom commands
            if (ConfigManager.config.CUSTOM_BAN_COMMAND_ENABLE)
            {
                ConsoleCommandSender sender = Bukkit.getServer().getConsoleSender();

                // Placeholders
                placeholders.clear();
                placeholders.put("%reason%", deathBanReason);
                placeholders.put("%date%", date.getTime() / 1000);
                placeholders.put("%time%", ConvertTime.min(banTime).replace("min", ""));

                // Only one command
                if (ConfigManager.config.CUSTOM_BAN_COMMAND_CMD instanceof String)
                {
                    String cmd = StringReplace.string(
                            (String) ConfigManager.config.CUSTOM_BAN_COMMAND_CMD,
                            p,
                            placeholders);

                    Bukkit.getServer().dispatchCommand(sender, cmd);
                }
                // Multiple commands
                else if (ConfigManager.config.CUSTOM_BAN_COMMAND_CMD instanceof List<?>)
                {
                    List<String> cmds = (List<String>) ConfigManager.config.CUSTOM_BAN_COMMAND_CMD;

                    for (String x : cmds)
                    {
                        String cmd = StringReplace.string(x, p, placeholders);

                        Bukkit.getServer().dispatchCommand(sender, cmd);
                    }
                }
            }
            // Use a default system
            else
            {
                // Ban player
                Bukkit.getBanList(BanList.Type.NAME).addBan(
                        p.getName(),
                        finalReason,
                        date,
                        null);
                Data.kickList.add(uuid);

                // Kick player
                final String kickReason = finalReason;
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.kickPlayer(kickReason);
                    }
                }.runTaskLater(Data.plugin, 5);
            }

            userData.lastBan = true;
            UserDataManager.save(userData);

            // Add ban level
            if (ConfigManager.config.BAN_TYPE.equals("increasing"))
            {
                // Ban time is baned on rank
                if (ConfigManager.config.BAN_RANK_TIME_ENABLE)
                {
                    // If player doesn't have any permission from the list
                    boolean permissionFound = false;

                    for (Map.Entry<String, SortedMap<Integer, Double>> entry : ConfigManager.config.BAN_RANK_LENGTH_RANKS.entrySet())
                    {
                        String rank = entry.getKey();
                        SortedMap<Integer, Double> rankBanLength = entry.getValue();

                        // Check if player has permission
                        if (p.hasPermission(rank))
                        {
                            permissionFound = true;

                            // Get ban length from function
                            BanTime.foundBanLength(rankBanLength, userData);
                        }
                    }

                    // Player doesn't have any permission from the list
                    if (!permissionFound)
                    {
                        SortedMap<Integer, Double> defaultBans = ConfigManager.config.BAN_RANK_LENGTH_RANKS.get("default");

                        // Get ban length from function
                        BanTime.foundBanLength(defaultBans, userData);
                    }
                }
                else // Ban time is always the same for every player
                {
                    // Get ban length from function
                    BanTime.foundBanLength(ConfigManager.config.BAN_LENGTH, userData);
                }
            }

            // Custom death message
            if (ConfigManager.config.DEATH_REASON_ENABLE)
            {
                // Info - player was banned
                placeholders.clear();
                placeholders.put("%player%", p.getName());
                placeholders.put("%killer%", killer);
                placeholders.put("%time%", ConvertTime.convertTime(banTime).time);
                placeholders.put("%date%", sdfDate.format(date));
                placeholders.put("%full-date%", sdfFullDate.format(date));

                String publicMessage =
                        MessagesManager.getMessage(Data.Message.DEATH_PREFIX, placeholders) + " " +
                        MessagesManager.getMessage(Data.Message.DEATH_PLAYER_INFO, p,  placeholders) + " " +
                        MessagesManager.getDeathMessage(dReason, placeholders) + " " +
                        MessagesManager.getMessage(Data.Message.DEATH_WITH_BAN, placeholders);

                e.setDeathMessage(publicMessage);
//                Bukkit.broadcastMessage(publicMessage);
            }

            if (userData.deathLevel < ConfigManager.config.LOWERING_BAN_MAX_BAN_LEVEL)
            {
                userData.deathLevel += 1;
                UserDataManager.save(userData);
            }

            userData = UserDataManager.load(uuid);
            MYSQL_deathLevelNow = userData.deathLevel;
        }
        else if (ConfigManager.config.DEATH_REASON_ENABLE)
        {
            // Custom death message
            // Info - player was not banned
            Map<String, Object> placeholders = new HashMap<>();
            placeholders.put("%player%", p.getName());
            placeholders.put("%killer%", killer);

            String publicMessage = "";

            // Player didn't lose any live because of plugin configuration
            if (PermissionsManager.checkPermissions(p, Data.Permission.NODEATH) ||
                    p.isOp() && ConfigManager.config.ADMIN_NO_BAN_AFTER_DEATH)
            {
                publicMessage =
                        MessagesManager.getMessage(Data.Message.DEATH_PREFIX, placeholders) + " " +
                        MessagesManager.getMessage(Data.Message.DEATH_PLAYER_INFO, p,placeholders) + " " +
                        MessagesManager.getDeathMessage(dReason, placeholders);
            }
            else
            {
                publicMessage =
                        MessagesManager.getMessage(Data.Message.DEATH_PREFIX, placeholders) + " " +
                        MessagesManager.getMessage(Data.Message.DEATH_PLAYER_INFO, p,placeholders) + " " +
                        MessagesManager.getDeathMessage(dReason, placeholders) + " " +
                        MessagesManager.getMessage(Data.Message.DEATH_WITHOUT_BAN, placeholders);
            }

            e.setDeathMessage(publicMessage);
//            Bukkit.broadcastMessage(publicMessage);
        }

        // Lowering bans
        if (ConfigManager.config.LOWERING_BAN_ENABLE)
        {
            int timestamp = (int) (new Date().getTime()/1000);
            int timeWithoutDeath = (int) (ConfigManager.config.LOWERING_BAN_TIME_WITHOUT_DEATH * 60);

            Map<String, Integer> map = Cache.loweringBans;

            if (map.containsKey(uuid))
                map.replace(uuid, map.get(uuid), (timestamp + timeWithoutDeath));
            else
                map.put(uuid, (timestamp + timeWithoutDeath));

            Cache.loweringBans = map;

//            Data.saveLoweringBans(map);
        }

        // MYSQL
        if (MySQL.isConnected)
        {
            if (MYSQL_banTime == -1) // Player hasn't been banned
            {
                String sqlQuery = "INSERT INTO `deaths`(`UUID`, `name`, `displayName`, `banned`, `deathLevelBefore`, `livesBefore`) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = MySQL.connection.prepareStatement(sqlQuery))
                {
                    preparedStatement.setString(1, MYSQL_uuid);
                    preparedStatement.setString(2, MYSQL_name);
                    preparedStatement.setString(3, MYSQL_displayName);
                    preparedStatement.setBoolean(4, MYSQL_banned);
                    preparedStatement.setInt(5, MYSQL_deathLevelBefore);
                    preparedStatement.setInt(6, MYSQL_livesBefore);

                    preparedStatement.executeUpdate();
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
            else // Player has been baned
            {
                String sqlQuery = "INSERT INTO `deaths`(`UUID`, `name`, `displayName`, `banned`, `deathLevelBefore`, `deathLevelNow`, `livesBefore`, `livesNow`, `banStartDate`, `banEndDate`, `banTime`, `banReason`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = MySQL.connection.prepareStatement(sqlQuery))
                {
                    preparedStatement.setString(1, MYSQL_uuid);
                    preparedStatement.setString(2, MYSQL_name);
                    preparedStatement.setString(3, MYSQL_displayName);
                    preparedStatement.setBoolean(4, MYSQL_banned);
                    preparedStatement.setInt(5, MYSQL_deathLevelBefore);
                    preparedStatement.setInt(6, MYSQL_deathLevelNow);
                    preparedStatement.setInt(7, MYSQL_livesBefore);
                    preparedStatement.setInt(8, MYSQL_livesNow);
                    preparedStatement.setInt(9, MYSQL_banStartDate);
                    preparedStatement.setInt(10, MYSQL_banEndDate);
                    preparedStatement.setDouble(11, MYSQL_banTime);
                    preparedStatement.setString(12, ChatColor.stripColor(MYSQL_banReason));

                    preparedStatement.executeUpdate();
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    private Entity getEntityDamager(EntityDamageEvent event)
    {
        Entity damager = null;
        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            damager = entityEvent.getDamager();
        }
        return damager;
    }
}
