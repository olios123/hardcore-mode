package me.olios.hardcoremode.Framework;

import me.olios.hardcoremode.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommandAction {

    private final Data.Permission permission;
    /**
     * Based on usage calculate required args
     * Build:
     * <arg> - required args
     * [arg] - optional args
     */
    private final String usage;

    protected CommandAction(Data.Permission permission, String usage) {
        this.permission = permission;
        this.usage = usage;
    }

    public abstract void cmd(CommandSender sender, Command cmd, String label, String[] args);

    public String[] getArgs() {
        return this.usage.split("\\s+");
    }
    public int requiredArgs() {
        int requiredArgs = 0;

        for (String arg : getArgs()) {
            if (arg.startsWith("<")) requiredArgs++;
        }
        return requiredArgs;
    }
    public Data.Permission getPermission() {return this.permission;}
    public String getUsage() {return this.usage;}
}
