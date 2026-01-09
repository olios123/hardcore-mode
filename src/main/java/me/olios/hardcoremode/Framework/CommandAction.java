package me.olios.hardcoremode.Framework;

import me.olios.hardcoremode.Data;

public abstract class CommandAction {

    protected final int argsLength;
    protected final Data.Permission executePermission;
    protected final String usage;

    protected CommandAction(int argsLength, Data.Permission executePermission, String usage) {
        this.argsLength = argsLength;
        this.executePermission = executePermission;
        this.usage = usage;
    }

    public abstract void cmdPlayer();
    public abstract void cmdConsole();

}
