package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Cache;
import me.olios.hardcoremode.Objects.UserData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Lives {

    public static void enable()
    {
        AtomicInteger step = new AtomicInteger(0);

        if (!ConfigManager.config.LIVES_BAR_INFO_ENABLED &&
            !ConfigManager.config.NO_LIVES_INFO_ENABLED) return;

        Bukkit.getScheduler().runTaskTimer(Data.plugin, () ->
        {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            for (Player player : Bukkit.getOnlinePlayers())
            {
                UserData userdata = UserDataManager.load(player.getUniqueId().toString());

                if (userdata == null) continue;
                if (!userdata.livesInfo) continue;

                String message = "";

                // Player still has lives
                if (userdata.lives > 0)
                {
                    if (step.get() == ConfigManager.config.LIVES_BAR_INFO.size()) step.set(0);
                    message = ConfigManager.config.LIVES_BAR_INFO.get(step.get());
                }
                else // The Last player lives
                {
                    if (step.get() == ConfigManager.config.NO_LIVES_INFO.size()) step.set(0);
                    message = ConfigManager.config.NO_LIVES_INFO.get(step.get());
                }

                message = StringReplace.string(message, player);

                // Lives and enabled
                if (userdata.lives > 0 && ConfigManager.config.LIVES_BAR_INFO_ENABLED)
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
                // No lives and enabled
                else if (userdata.lives == 0 && ConfigManager.config.NO_LIVES_INFO_ENABLED)
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }

                step.set(step.get() + 1);
            }
        }, 0, 20);
    }

    public static void run()
    {
        Bukkit.getScheduler().runTaskTimer(Data.plugin, () ->
        {
            Map<String, Integer> renewingLives = Cache.renewingLives;

            if (renewingLives == null || renewingLives.isEmpty()) return;
            for (Map.Entry<String, Integer> entry : renewingLives.entrySet())
            {
                String uuid = entry.getKey();
                int endTime = entry.getValue();
                int currentTimestamp = (int) (new Date().getTime()/1000);
                int timeWithoutDeath = (int) ConfigManager.config.LIVES_RENEWING_LIVES_TIME_WITHOUT_DEATH * 60;

                UserData userData = UserDataManager.load(uuid);
                if (userData == null) continue;

                int lives = userData.lives;

                if (lives >= ConfigManager.config.LIVES_MAX_COUNT) continue;

                if (currentTimestamp >= endTime)
                {
                    userData.lives += 1;
                    UserDataManager.save(userData);

                    if (lives + 1 >= ConfigManager.config.LIVES_DEFAULT_COUNT)
                    {
                        Map<String, Integer> map = Cache.renewingLives;
                        map.remove(uuid);
                        Cache.renewingLives = map;
//                        Data.saveRenewingLives(map);
                        continue;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    if (offlinePlayer.isOnline())
                    {
                        MessagesManager.sendMessage(offlinePlayer.getPlayer(), Data.Message.RENEWED_LIFE);
                    }

                    /**
                     * Update next time
                     */
                    Map<String, Integer> map = Cache.renewingLives;

                    if (map.containsKey(uuid))
                    {
                        map.replace(uuid,
                                map.get(uuid),
                                (currentTimestamp + timeWithoutDeath));
                    }
                    else
                    {
                        map.put(uuid, (currentTimestamp + timeWithoutDeath));
                    }

                    Cache.renewingLives = map;
//                    Data.saveRenewingLives(map);
                }
            }
        }, 0, 100);
        /**
         * 5 sec
         */
    }
}
