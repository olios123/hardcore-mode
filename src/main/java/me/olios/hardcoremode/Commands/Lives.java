package me.olios.hardcoremode.Commands;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Librrary.Numeric;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import me.olios.hardcoremode.Managers.UserDataManager;
import me.olios.hardcoremode.Objects.Cache;
import me.olios.hardcoremode.Objects.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Lives {

    public static void executeCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        boolean isPlayer = sender instanceof Player;
        Player p = (sender instanceof Player) ? ((Player) sender).getPlayer() : null;

        if (args.length == 0 || args[0].equals("info"))
        {
            if (!isPlayer)
            {
                MessagesManager.sendLogMessage(Data.Message.CONSOLE_COMMAND);
                return;
            }

            p.recalculatePermissions(); // Refresh permissions
            String uuid = p.getUniqueId().toString();

            UserData userData = UserDataManager.load(p.getUniqueId().toString());

            MessagesManager.sendMessage(p, Data.Message.CMD_LIVES);

            if (ConfigManager.config.LIVES_RENEWING_LIVES_ENABLE ||
                    ConfigManager.config.LOWERING_BAN_ENABLE) p.sendMessage("");

            // If renewing lives is enabled
            if (ConfigManager.config.LIVES_RENEWING_LIVES_ENABLE)
            {
                Map<String, Integer> renewingLives = Cache.renewingLives;

                if (renewingLives.containsKey(uuid))
                    MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_EXTRA_LIFE, p);
            }

            // If lowering bans is enabled
            if (ConfigManager.config.LOWERING_BAN_ENABLE)
            {
                Map<String, Integer> loweringBans = Cache.loweringBans;

                if (loweringBans.containsKey(uuid))
                    MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_BAN_LEVEL, p);
            }
        }

        else if (args.length == 1 && isPlayer) // Show hide
        {
            if (!ConfigManager.config.LIVES_TOGGLE_INFO)
            {
                MessagesManager.sendMessage(p, Data.Message.CMD_INCORRECT_USE);
                return;
            }

            UserData userData = UserDataManager.load(p.getUniqueId().toString());

            String type = args[0].toLowerCase();

            List<String> options = new ArrayList<>();
            options.add("show");
            options.add("hide");

            if (!options.contains(type))
            {
                MessagesManager.sendMessage(p, Data.Message.CMD_INCORRECT_USE);
                return;
            }

            switch (type)
            {
                case "show":
                    userData.livesInfo = true;
                    MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_SHOW);
                    break;
                case "hide":
                    userData.livesInfo = false;
                    MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_HIDE);
                    break;
            }

            UserDataManager.save(userData);
        }

        else if (args.length == 3) // Management
        {
            String type = args[0].toLowerCase();
            String player = args[1];
            String amount = args[2];

            List<String> types = new ArrayList<>();
            types.add("add");
            types.add("set");
            types.add("remove");

            if (isPlayer && !PermissionsManager.checkPermissions(p, Data.Permission.LIVES))
            {
                MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
                return;
            }

            if (!types.contains(type))
            {
                if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_INCORRECT_USE);
                else MessagesManager.sendLogMessage(Data.Message.CMD_INCORRECT_USE);
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
            if (player.equals("all"))
            {
                if (!Numeric.check(amount))
                {
                    if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_AMOUNT_IS_NOT_NUMBER);
                    else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_AMOUNT_IS_NOT_NUMBER);
                    return;
                }

                int a = Integer.parseInt(amount);
                List<UserData> userDataList = UserDataManager.getAllUserData();

                for (UserData userData : userDataList)
                {
                    offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(userData.uuid));

                    Map<String, Object> placeholders = new HashMap<>();
                    placeholders.put("%player%", offlinePlayer.getName());
                    placeholders.put("%count%", (a > 0) ? String.valueOf(a): 0);

                    switch (type)
                    {
                        case "add":
                            userData.lives += a;
                            break;
                        case "set":
                            if (a < 0) a = 0;
                            userData.lives = a;
                            break;
                        case "remove":
                            if (a > userData.lives) a = userData.lives;
                            userData.lives -= a;
                            break;
                    }
                    UserDataManager.save(userData);
                }

                Map<String, Object> placeholders = new HashMap<>();
                placeholders.put("%count%", (a > 0) ? String.valueOf(a): 0);

                // Summary
                switch (type)
                {
                    case "add":

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_ALL_ADD, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_ALL_ADD, placeholders);

                        break;
                    case "set":

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_ALL_SET, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_ALL_SET, placeholders);

                        break;
                    case "remove":

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_ALL_REMOVE, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_ALL_REMOVE, placeholders);

                        break;
                }
            }
            else
            {
                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
                {
                    if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_PLAYER_NOT_FOUND);
                    else MessagesManager.sendLogMessage(Data.Message.CMD_PLAYER_NOT_FOUND);

                    return;
                }

                if (!Numeric.check(amount))
                {
                    if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_AMOUNT_IS_NOT_NUMBER);
                    else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_AMOUNT_IS_NOT_NUMBER);

                    return;
                }

                UserData userData = UserDataManager.load(offlinePlayer.getUniqueId().toString());
                int a = Integer.parseInt(amount);

                Map<String, Object> placeholders = new HashMap<>();
                placeholders.put("%player%", offlinePlayer.getName());
                placeholders.put("%count%", (a > 0) ? String.valueOf(a): 0);

                switch (type)
                {
                    case "add":
                        userData.lives += a;

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_ADD, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_ADD, placeholders);

                        break;
                    case "set":
                        if (a < 0) a = 0;
                        userData.lives = a;

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_SET, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_SET, placeholders);

                        break;
                    case "remove":
                        if (a > userData.lives) a = userData.lives;
                        userData.lives-= a;

                        if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_LIVES_REMOVE, placeholders);
                        else MessagesManager.sendLogMessage(Data.Message.CMD_LIVES_REMOVE, placeholders);

                        break;
                }
                UserDataManager.save(userData);
            }
        }
        else
        {
            if (isPlayer) MessagesManager.sendMessage(p, Data.Message.CMD_INCORRECT_USE);
            else MessagesManager.sendLogMessage(Data.Message.CMD_INCORRECT_USE);
        }
    }
}
