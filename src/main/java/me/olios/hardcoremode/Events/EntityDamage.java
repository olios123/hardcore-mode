package me.olios.hardcoremode.Events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EntityDamage implements Listener {

    public static ListMultimap<String, Integer> playerBleedingMap = ArrayListMultimap.create();

    @EventHandler(priority = EventPriority.HIGH)
    private void onEntityDamageEvent(EntityDamageEvent e)
    {
        Entity entity = e.getEntity();

        // If entity is not a player
        if (!(entity instanceof Player)) return;
        // Is blood is disabled
        if (!ConfigManager.config.BLOOD) return;

        EntityDamageEvent.DamageCause damageCause = e.getCause();
        assert damageCause != null;

        // Player
        Player p = ((Player) entity).getPlayer();
        String uuid = p.getUniqueId().toString();
        p.recalculatePermissions();

        // Check if player can bleed
        List<EntityDamageEvent.DamageCause> disableCause = new ArrayList<>();
        disableCause.add(EntityDamageEvent.DamageCause.DROWNING);
        disableCause.add(EntityDamageEvent.DamageCause.DRYOUT);
        disableCause.add(EntityDamageEvent.DamageCause.POISON);
        disableCause.add(EntityDamageEvent.DamageCause.VOID);

        // If damage cause is disabled
        if (disableCause.contains(damageCause)) return;

        // Particle
        BlockData redConcrete = Bukkit.createBlockData(Material.RED_CONCRETE);
        Location particleLocation = p.getLocation();
        particleLocation.setY(particleLocation.getBlockY() + 1);

        int particles = (int) (50 * e.getDamage());
        if (particles > 500) particles = 500;

        p.getWorld().spawnParticle(
                Particle.BLOCK,
                particleLocation,
                particles,
                0.3,
                0.3,
                0.3,
                0.1,
                redConcrete);

        int bleedingTime = (int) e.getDamage() * 5;

        BukkitTask task = startBleeding(p, bleedingTime);
        Bukkit.getScheduler().runTaskLater(Data.plugin, task::cancel, bleedingTime * 5L);

        playerBleedingMap.put(p.getUniqueId().toString(), task.getTaskId());
    }

    private BukkitTask startBleeding(Player p, int bleedingTime)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            int currentRepeat = 0;
            BlockData redConcrete = Bukkit.createBlockData(Material.RED_CONCRETE);

            @Override
            public void run()
            {
                if (currentRepeat >= bleedingTime)
                {
                    cancel();
                    return;
                }

                Location newParticleLocation = p.getLocation();
                newParticleLocation.setY(newParticleLocation.getBlockY() + 1);

                p.getWorld().spawnParticle(
                        Particle.BLOCK,
                        newParticleLocation,
                        15,
                        0.1,
                        0.1,
                        0.1,
                        10,
                        redConcrete);

                currentRepeat++;
            }
        };

        return runnable.runTaskTimer(Data.plugin, 0, 5);
    }

    public static void cancelBleedingTask(BukkitTask task)
    {
        if (task != null && !task.isCancelled()) task.cancel();
    }
}
