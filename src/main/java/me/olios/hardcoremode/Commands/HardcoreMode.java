/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Commands;

import me.olios.hardcoremode.Data;
import me.olios.hardcoremode.Database.MySQL;
import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import me.olios.hardcoremode.Librrary.TextCreator;
import me.olios.hardcoremode.Main;
import me.olios.hardcoremode.Managers.ConfigManager;
import me.olios.hardcoremode.Managers.MessagesManager;
import me.olios.hardcoremode.Managers.PermissionsManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HardcoreMode {

	public static void executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		// Sender is player
		if (sender instanceof Player)
		{
			Player p = ((Player) sender).getPlayer();
			p.recalculatePermissions(); // Refresh permissions

			// Help
			if (args.length == 0 || args[0].equals("help"))
			{
				p.sendMessage(StringReplace.string("&c❤ &4Hardcore&cMode ❤ (&f" + Data.pluginVersion + "&c)"));
				p.sendMessage(StringReplace.string("&fAuthor: &colios"));

				TextCreator commands = new TextCreator("/hardcoremode commands");
				commands.createCommand();

				p.spigot().sendMessage(
						new TextComponent(StringReplace.string("&fCommands: ")),
						commands.get()
				);

				TextCreator about = new TextCreator("/hardcoremode about");
				about.createCommand();

				p.spigot().sendMessage(
						new TextComponent(StringReplace.string("&fAbout: ")),
						about.get()
				);

				// Discord
				TextCreator discord = new TextCreator("Discord");
				discord.COLOR = ChatColor.BLUE;
				discord.UNDERLINED = true;
				discord.addClickEvent(ClickEvent.Action.OPEN_URL, Data.discord);
				discord.addHoverEvent(HoverEvent.Action.SHOW_TEXT, "&fClick to join &9Discord &fsupport server.");

				// Spigot
				TextCreator spigot = new TextCreator("Spigot/Docs");
				spigot.COLOR = ChatColor.YELLOW;
				spigot.UNDERLINED = true;
				spigot.addClickEvent(ClickEvent.Action.OPEN_URL, Data.resourceURL);
				spigot.addHoverEvent(HoverEvent.Action.SHOW_TEXT, "&fClick to open &eSpigot/Docs &fpage.");

				p.spigot().sendMessage(
						new TextComponent("\n"),
						discord.get(), new TextComponent("   "), spigot.get());
			}

			// About
			else if (args[0].equals("about"))
			{
				p.sendMessage(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- About"));
				p.sendMessage("");
				MessagesManager.sendMessage(p, Data.Message.CMD_ABOUT);
			}

			// Commands
			else if (args[0].equals("commands"))
			{
				if (!PermissionsManager.checkPermissions(p, Data.Permission.ADMIN))
				{
					MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
					return;
				}

				p.sendMessage(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- Commands"));
				p.sendMessage("");

				for (Map.Entry<String, Map<String, Object>> entry :
						Data.plugin.getDescription().getCommands().entrySet())
				{
					String command = entry.getKey();
					Map<String, Object> description = entry.getValue();

					String usage = description.get("usage").toString().replace(command, "");

					// Only for unban command
					if (command.equals("unban"))
					{
						TextCreator unbanCommand = new TextCreator("/unban");
						unbanCommand.COLOR = ChatColor.RED;
						unbanCommand.UNDERLINED = true;
						unbanCommand.addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unban");
						unbanCommand.addHoverEvent(HoverEvent.Action.SHOW_TEXT,
								"&fExecutes the &c/unban &fcommand");

						p.spigot().sendMessage(
								unbanCommand.get(),
								new TextComponent(StringReplace.string("&f" + usage))
						);
					}
					// All command excluding unban
					else
					{
						TextCreator clickableCommand = new TextCreator("/" + command);
						clickableCommand.createCommand();

						p.spigot().sendMessage(
								clickableCommand.get(),
								new TextComponent(StringReplace.string("&f" + usage))
						);
					}

					p.sendMessage(StringReplace.string("  &7" +
							description.get("description").toString()));
				}
			}

			// Permissions
			else if (args[0].equals("permissions"))
			{
				if (!PermissionsManager.checkPermissions(p, Data.Permission.ADMIN))
				{
					MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
					return;
				}

				p.sendMessage(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- Permissions"));
				p.sendMessage("");

				Data.plugin.getDescription().getPermissions().forEach(permission ->
				{
					p.sendMessage(StringReplace.string("&c" + permission.getName()));
					p.sendMessage(StringReplace.string("  &7" + permission.getDescription()));
				});
			}

			// Placeholders
			else if (args[0].equals("placeholders"))
			{
				if (!PermissionsManager.checkPermissions(p, Data.Permission.ADMIN))
				{
					MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
					return;
				}

				p.sendMessage(StringReplace.string("&cThe list of placeholders is available at this link:"));
				p.sendMessage(StringReplace.string("&chttps://www.spigotmc.org/resources/authors/olios123.1116758/"));
			}


			// Reload plugin
			else if (args[0].equals("reload"))
			{
				// Check permissions
				if (!PermissionsManager.checkPermissions(p, Data.Permission.RELOAD))
				{
					MessagesManager.sendMessage(p, Data.Message.NO_PERMISSIONS);
					return;
				}

				ConfigManager.reload();

				MessagesManager.sendMessage(p, Data.Message.PLAYER_RELOAD_COMPLETE);
			}
			
			// Debug
			else if (args[0].equals("debug"))
			{
				TextCreator spigotMC = new TextCreator("SpigotMC");
				spigotMC.createLink(Data.resourceURL, ChatColor.RED);

				TextCreator spigotDocs = new TextCreator("Spigot Docs");
				spigotDocs.createLink(Data.resourceDocs, ChatColor.RED);

				TextCreator discord = new TextCreator("Discord");
				discord.createLink(Data.discord, ChatColor.RED);

				p.sendMessage("");
				p.sendMessage(StringReplace.string("&8───── &c❤ &4Hardcore&cMode &c❤ &8─────"));
				p.spigot().sendMessage(
						new TextComponent(StringReplace.string("&fDeveloper &8⏵ &colios\n")),
						new TextComponent(StringReplace.string("&fVersion &8⏵ &c" + Data.pluginVersion + "\n")),
						new TextComponent(StringReplace.string("&fUpdate? &8⏵ &c" + Data.canUpdate + "\n")),
						new TextComponent(StringReplace.string("&fDatabase &8⏵ " + ((MySQL.isConnected) ? "&cYes - Connected (MySQL)\n" : "&cNo\n"))),
						new TextComponent(StringReplace.string("&fResource ID (Spigot) &8⏵ &c" + Data.resourceId + "\n")),
						new TextComponent(StringReplace.string("&fSpigot page &8⏵ &c")), spigotMC.get(),
						new TextComponent(StringReplace.string("\n&fDocumentation &8⏵ &c")), spigotDocs.get(),
						new TextComponent(StringReplace.string("\n&fSupport &8⏵ &c")), discord.get(),
						new TextComponent(StringReplace.string("\n&fSSID &8⏵ &c" + Data.SSID)),
						new TextComponent(StringReplace.string("\n&fHooks:"))
				);
				if (Data.PAPI)
				{
					p.sendMessage(StringReplace.string("   &8⏵ &cPlaceholderAPI - " + Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion()));
				}
				p.sendMessage(StringReplace.string("&8──────────────────────"));
				// TODO możliwość dopisywania własnych napisów
			}


			// Argument not found
			else
			{
				Map<String, Object> placeholders = new HashMap<>();
				placeholders.put("%command%", "hardcoremode [help/commands/permissions/placeholders/reload/about]");

				MessagesManager.sendMessage(p, Data.Message.ARGUMENT_MISSING, placeholders);
			}
		}
		else // Console
		{
			// Default help
			if (args.length == 0 || args[0].equals("help"))
			{
				Main.log(StringReplace.string("&c❤ &4Hardcore&cMode ❤ (&f" + Data.pluginVersion + "&c)"));
				Main.log(StringReplace.string("&fAuthor: &colios"));
				Main.log(StringReplace.string("&fCommands: &c/hardcoremode commands"));
				Main.log(StringReplace.string("&fAbout: &c/hardcoremode about"));
				Main.log("");
				Main.log(StringReplace.string("&fDiscord: &9" + Data.discord));
				Main.log(StringReplace.string("&fSpigot/Docs: &e" + Data.resourceURL));
			}

			// About
			else if (args[0].equals("about"))
			{
				Main.log(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- About"));
				Main.log("");
				MessagesManager.sendLogMessage(Data.Message.CMD_ABOUT);
			}

			// Commands
			else if (args[0].equals("commands"))
			{
				Main.log(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- Commands"));
				Main.log("");

				for (Map.Entry<String, Map<String, Object>> entry :
						Data.plugin.getDescription().getCommands().entrySet())
				{
					String command = entry.getKey();
					Map<String, Object> description = entry.getValue();

					String usage = description.get("usage").toString().replace(command, "");

					Main.log(StringReplace.string("&c/" + command + "&f" + usage));
					Main.log(StringReplace.string("  &7" +
							description.get("description").toString()));
				}

			}

			// Permissions
			else if (args[0].equals("permissions"))
			{
				Main.log(StringReplace.string("&c❤ &4Hardcore&cMode ❤ &f- Permissions"));
				Main.log("");

				Data.plugin.getDescription().getPermissions().forEach(permission ->
				{
					Main.log(StringReplace.string("&c" + permission.getName()));
					Main.log(StringReplace.string("  &7" + permission.getDescription()));
				});
			}


			// Placeholders
			else if (args[0].equals("placeholders"))
			{
				Main.log(StringReplace.string("&cThe list of placeholders is available at this link:"));
				Main.log(StringReplace.string("&f" + Data.resourceDocs));
			}


			// Reload plugin
			else if (args[0].equals("reload"))
			{
				ConfigManager.reload();

				MessagesManager.sendLogMessage(Data.Message.RELOAD_COMPLETE);
			}


			// Debug
			else if (args[0].equals("debug"))
			{

			}


			// Argument not found
			else
			{
				Map<String, Object> placeholders = new HashMap<>();
				placeholders.put("%command%", "hardcoremode [help/commands/permissions/placeholders/reload/about]");

				MessagesManager.sendLogMessage(Data.Message.ARGUMENT_MISSING, placeholders);
			}
		}
	}
}
