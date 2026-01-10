package me.olios.hardcoremode.Commands;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Framework.CommandAction;
import me.olios.hardcoremode.Main;
import me.olios.hardcoremode.Managers.MessagesManager;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.profile.PlayerProfile;

public class Unban extends CommandAction {

    private static final ProfileBanList banList = Bukkit.getBanList(BanList.Type.PROFILE);

    public Unban(Data.Permission permission, String usage) {
        super(permission, usage);
    }

    @Override
    public void cmd(CommandSender sender, Command cmd, String label, String[] args)
    {
        PlayerProfile bannedPlayer = getBannedPlayer(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        // Player not found
        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore())
        {
            MessagesManager.sendMessageSender(sender, Data.Message.CMD_PLAYER_NOT_FOUND);
            return;
        }

        // Not banned
        if (bannedPlayer == null || banList.getBanEntry(bannedPlayer) == null)
        {
            MessagesManager.sendMessageSender(sender, Data.Message.CMD_NOT_BANNED);
            return;
        }

        BanEntry<PlayerProfile> entry = banList.getBanEntry(bannedPlayer);

        // Unban player
        banList.pardon(entry.getBanTarget());
        MessagesManager.sendMessageSender(sender, Data.Message.CMD_UNBANNED);
    }

    private static PlayerProfile getBannedPlayer(String name)
    {
        return banList.getEntries().stream()
                .filter(entry -> entry.equals(name))
                .map(BanEntry::getBanTarget)
                .findFirst()
                .orElse(null);
    }
}
