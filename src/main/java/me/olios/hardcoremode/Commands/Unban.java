package me.olios.hardcoremode.Commands;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unban {

    public static void executeCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Sender is player
        if (sender instanceof Player)
        {
            Player p = ((Player) sender).getPlayer();
            p.recalculatePermissions();

            // Check permissions
            if (!PermissionsManager.checkPermissions(p, Data.Permission.UNBAN))
            {
                MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
                return;
            }

            // Check args length
            if (args.length != 1)
            {
                MessagesManager.sendMessage(p, Data.Message.CMD_INCORRECT_USE);
                return;
            }

            String player = args[0];
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);

            // Player not found
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
            {
                MessagesManager.sendMessage(p, Data.Message.CMD_PLAYER_NOT_FOUND);
                return;
            }

            // Unban
            // Not banned
            if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(player))
            {
                MessagesManager.sendMessage(p, Data.Message.CMD_NOT_BANNED);
                return;
            }

            // Final unban
            Bukkit.getBanList(BanList.Type.NAME).pardon(player);

            MessagesManager.sendMessage(p, Data.Message.CMD_UNBANNED);
        }
        else // Console
        {
            // Check args length
            if (args.length != 1)
            {
                MessagesManager.sendLogMessage(Data.Message.CMD_INCORRECT_USE);
                return;
            }

            String player = args[0];
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);

            // Player not found
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
            {
                MessagesManager.sendLogMessage(Data.Message.CMD_PLAYER_NOT_FOUND);
                return;
            }

            // Unban
            // Not banned
            if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(player))
            {
                MessagesManager.sendLogMessage(Data.Message.CMD_NOT_BANNED);
                return;
            }

            // Final unban
            Bukkit.getBanList(BanList.Type.NAME).pardon(player);

            MessagesManager.sendLogMessage(Data.Message.CMD_UNBANNED);
            // TODO: 30.10.2023 sprawdzić 
        }
    }

}
