package me.olios.hardcoremode.API;

import me.olios.hardcoremode.Managers.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PAPICustom {

    public static Map<String, Object> getStaticPlaceholders()
    {
        Map<String, Object> placeholders = new HashMap<>();

        placeholders.put("%hardcoremode_config_check-updates%", ConfigManager.config.CHECK_UPDATES);
        placeholders.put("%hardcoremode_config_language%", ConfigManager.config.LANGUAGE);
        placeholders.put("%hardcoremode_config_clearer-logs%", ConfigManager.config.CLEARER_LOGS);
        placeholders.put("%hardcoremode_config_ban-type%", ConfigManager.config.BAN_TYPE);
        placeholders.put("%hardcoremode_config_custom-ban-command_enable%", ConfigManager.config.CUSTOM_BAN_COMMAND_ENABLE);

        if (ConfigManager.config.CUSTOM_BAN_COMMAND_CMD instanceof String)
        {
            placeholders.put("%hardcoremode_config_custom-ban-command_cmd%", ConfigManager.config.CUSTOM_BAN_COMMAND_CMD);
        }
        else if (ConfigManager.config.CUSTOM_BAN_COMMAND_CMD instanceof List<?>)
        {
            String msg = "";
            for (String x : (List<String>) ConfigManager.config.CUSTOM_BAN_COMMAND_CMD)
            {
                msg += x + "\n";
            }
            msg = msg.substring(0, 2);

            placeholders.put("%hardcoremode_config_custom-ban-command_cmd%", msg);
        }

        placeholders.put("%hardcoremode_config_ban-time%", ConfigManager.config.BAN_TIME);
        placeholders.put("%hardcoremode_config_ban-rank-time_enable%", ConfigManager.config.BAN_RANK_TIME_ENABLE);
        placeholders.put("%hardcoremode_config_ban-rank-length_enable%", ConfigManager.config.BAN_RANK_LENGTH_ENABLE);
        placeholders.put("%hardcoremode_config_lowering-ban_enable%", ConfigManager.config.LOWERING_BAN_ENABLE);
        placeholders.put("%hardcoremode_config_lowering-ban_time-without-death%", ConfigManager.config.LOWERING_BAN_TIME_WITHOUT_DEATH);
        placeholders.put("%hardcoremode_config_lowering-ban_lowered-level%", ConfigManager.config.LOWERING_BAN_LOWERED_LEVEL);
        placeholders.put("%hardcoremode_config_lowering-ban_max-ban-level%", ConfigManager.config.LOWERING_BAN_MAX_BAN_LEVEL);
        placeholders.put("%hardcoremode_config_lives_enable%", ConfigManager.config.LIVES_ENABLE);
        placeholders.put("%hardcoremode_config_lives_toggle-info%", ConfigManager.config.LIVES_TOGGLE_INFO);
        placeholders.put("%hardcoremode_config_lives_default-visible%", ConfigManager.config.LIVES_DEFAULT_VISIBLE);
        placeholders.put("%hardcoremode_config_lives_default-count%", ConfigManager.config.LIVES_DEFAULT_COUNT);
        placeholders.put("%hardcoremode_config_lives_max-count%", ConfigManager.config.LIVES_MAX_COUNT);
        placeholders.put("%hardcoremode_config_lives_renewing-lives_enable%", ConfigManager.config.LIVES_RENEWING_LIVES_ENABLE);
        placeholders.put("%hardcoremode_config_lives_renewing-lives_time-without-death%", ConfigManager.config.LIVES_RENEWING_LIVES_TIME_WITHOUT_DEATH);
        placeholders.put("%hardcoremode_config_admin-no-ban-after-death%", ConfigManager.config.ADMIN_NO_BAN_AFTER_DEATH);
        placeholders.put("%hardcoremode_config_death-reason-enable%", ConfigManager.config.DEATH_REASON_ENABLE);
        placeholders.put("%hardcoremode_config_death-lightning%", ConfigManager.config.DEATH_LIGHTNING);
        placeholders.put("%hardcoremode_config_blood%", ConfigManager.config.BLOOD);
        placeholders.put("%hardcoremode_config_time-format%", ConfigManager.config.TIME_FORMAT);
        placeholders.put("%hardcoremode_config_date-format%", ConfigManager.config.DATE_FORMAT);
        placeholders.put("%hardcoremode_config_full-date-format%", ConfigManager.config.FULL_DATE_FORMAT);

        return placeholders;
    }
}
