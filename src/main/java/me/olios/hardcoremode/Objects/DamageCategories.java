package me.olios.hardcoremode.Objects;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class DamageCategories {

    // Entity damage causes are shared with players
    // If player caused the death then we use player plugin response
    public static final Map<EntityDamageEvent.DamageCause, Config.PluginDeathResponse> DAMAGE_CATEGORIES =
            new EnumMap<>(EntityDamageEvent.DamageCause.class);

    static {
        // ENTITY
        register(Config.PluginDeathResponse.ENTITY,
                EntityDamageEvent.DamageCause.DRAGON_BREATH,
                EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
                EntityDamageEvent.DamageCause.MAGIC,
                EntityDamageEvent.DamageCause.PROJECTILE,
                EntityDamageEvent.DamageCause.SONIC_BOOM,
                EntityDamageEvent.DamageCause.SUICIDE,
                EntityDamageEvent.DamageCause.THORNS,
                EntityDamageEvent.DamageCause.WITHER
        );

        // ENVIRONMENT
        register(Config.PluginDeathResponse.ENVIRONMENT,
                EntityDamageEvent.DamageCause.CAMPFIRE,
                EntityDamageEvent.DamageCause.CONTACT,
                EntityDamageEvent.DamageCause.DROWNING,
                EntityDamageEvent.DamageCause.DRYOUT,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.FALLING_BLOCK,
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.FLY_INTO_WALL,
                EntityDamageEvent.DamageCause.FREEZE,
                EntityDamageEvent.DamageCause.HOT_FLOOR,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.LIGHTNING,
                EntityDamageEvent.DamageCause.MELTING,
                EntityDamageEvent.DamageCause.POISON,
                EntityDamageEvent.DamageCause.STARVATION,
                EntityDamageEvent.DamageCause.SUFFOCATION
        );

        // OTHER
        register(Config.PluginDeathResponse.OTHER,
                EntityDamageEvent.DamageCause.CRAMMING,
                EntityDamageEvent.DamageCause.CUSTOM,
                EntityDamageEvent.DamageCause.KILL,
                EntityDamageEvent.DamageCause.WORLD_BORDER
        );

        // VOID
        register(Config.PluginDeathResponse.VOID,
                EntityDamageEvent.DamageCause.VOID
        );
    }

    private static void register(Config.PluginDeathResponse response,
                                 EntityDamageEvent.DamageCause... causes) {
        for (EntityDamageEvent.DamageCause cause : causes) {
            DAMAGE_CATEGORIES.put(cause, response);
        }
    }

}
