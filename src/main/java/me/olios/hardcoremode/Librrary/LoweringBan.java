package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Cache;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.Map;

public class LoweringBan {

    public static void run()
    {
        Bukkit.getScheduler().runTaskTimer(Data.plugin, () ->
        {
            Map<String, Integer> loweringBans = Cache.loweringBans;

            for (Map.Entry<String, Integer> entry : loweringBans.entrySet())
            {
                String uuid = entry.getKey();
                int endTime = entry.getValue();
                int currentTimestamp = (int) (new Date().getTime()/1000);
                int timeWithoutDeath = (int) ConfigManager.config.LOWERING_BAN_TIME_WITHOUT_DEATH * 60;

                int loweredLevel = ConfigManager.config.LOWERING_BAN_LOWERED_LEVEL;

                UserData userData = UserDataManager.load(uuid);
                if (userData == null) continue;

                int deathLevel = userData.deathLevel;

                if (deathLevel > 0)
                {
                    if (currentTimestamp >= endTime)
                    {
                        if (deathLevel - loweredLevel < 0)
                        {
                            userData.deathLevel = 0;
                        } else
                        {
                            userData.deathLevel -= loweredLevel;
                        }

                        if (userData.deathLevel == 0)
                        {
                            Map<String, Integer> map = Cache.loweringBans;
                            map.remove(uuid);
                            Cache.loweringBans = map;
//                            Data.saveLoweringBans(map);
                            UserDataManager.save(userData);
                        }
                        else
                        {
                            UserDataManager.save(userData);

                            /**
                             * Update next time
                             */
                            Map<String, Integer> map = Cache.loweringBans;

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
                            Cache.loweringBans = map;
//                            Data.saveLoweringBans(map);
                        }
                    }
                }
                else
                {
                    Map<String, Integer> map = Cache.loweringBans;
                    map.remove(uuid);
                    Cache.loweringBans = map;
//                    Data.saveLoweringBans(map);
                }
            }
        }, 0, 100);
        /**
         * 5 sec
         */
    }

}
