/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Managers;

import me.olios.hardcoremode.Commands.HardcoreMode;
import me.olios.hardcoremode.Commands.Lives;
import me.olios.hardcoremode.Commands.Unban;
import me.olios.hardcoremode.Data;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager {

    public static void manageCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        switch(cmd.getName())
        {
            case "hm":
            case "hardcoremode":
                HardcoreMode.executeCommand(sender, cmd, label, args);
                break;
            case "lives":
                Lives.executeCommand(sender, cmd, label, args);
                break;
            case "unban":
                Unban.executeCommand(sender, cmd, label, args);
                break;
        }
    }

    public static List<String> manageCompleter(CommandSender sender, Command cmd, String label, String[] args)
    {
        String command = cmd.getName().toLowerCase();
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) return null;
        Player p = ((Player) sender).getPlayer();
        p.recalculatePermissions(); // Refresh permissions

        switch (command)
        {
            case "hardcoremode":
            case "hm":
                switch (args.length)
                {
                    case 1:
                        completions.add("help");
                        completions.add("about");
                        if (PermissionsManager.checkPermissions(p, Data.Permission.ADMIN))
                        {
                            completions.add("commands");
                            completions.add("permissions");
                            completions.add("placeholders");
                            completions.add("debug");
                        }
                        if (PermissionsManager.checkPermissions(p, Data.Permission.RELOAD))
                        {
                            completions.add("reload");
                        }
                        break;
                }
                return completions;
            case "lives":
                switch (args.length)
                {
                    case 1:
                        if (ConfigManager.config.LIVES_TOGGLE_INFO)
                        {
                            completions.add("hide");
                            completions.add("show");
                        }

                        if (PermissionsManager.checkPermissions(p, Data.Permission.LIVES))
                        {
                            completions.add("add");
                            completions.add("set");
                            completions.add("remove");
                        }

                        completions.add("info");

                        break;
                    case 2:
                        if (PermissionsManager.checkPermissions(p, Data.Permission.LIVES))
                        {
                            completions.add("all");
                            Bukkit.getOnlinePlayers().forEach((player) -> completions.add(player.getName()));
                        }
                        break;
                }
                return completions;
            case "unban":
                switch (args.length)
                {
                    case 1:
                        if (PermissionsManager.checkPermissions(p, Data.Permission.UNBAN))
                        {
                            List<String> players = new ArrayList<>();
                            Bukkit.getBanList(BanList.Type.NAME).getBanEntries()
                                    .forEach(entry -> players.add(entry.getTarget()));

                            completions.addAll(players);
                        }
                        break;
                }
                return completions;
        }
        return null;
    }
}
