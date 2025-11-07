package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Librrary.Replace.StringReplace;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class TextCreator {

    private TextComponent textComponent;

    public ChatColor COLOR;
    public boolean UNDERLINED;

    public TextCreator(String text)
    {
        textComponent = new TextComponent(text);
    }

    public void addClickEvent(ClickEvent.Action action, String value)
    {
        textComponent.setClickEvent(new ClickEvent(action, value));
    }

    public void addHoverEvent(HoverEvent.Action action, String value)
    {
        textComponent.setHoverEvent(new HoverEvent(action,
                new ComponentBuilder(StringReplace.string(value)).create()));
    }

    public TextComponent get()
    {
        textComponent.setColor(COLOR.asBungee());
        textComponent.setUnderlined(UNDERLINED);

        return textComponent;
    }

    public void createCommand()
    {
        COLOR = ChatColor.RED;
        UNDERLINED = true;
        addClickEvent(ClickEvent.Action.RUN_COMMAND,
                textComponent.getText());
        addHoverEvent(HoverEvent.Action.SHOW_TEXT,
                "&fExecutes the &c" + textComponent.getText() + " &fcommand");
    }

    public void createLink(String url, ChatColor color)
    {
        UNDERLINED = true;
        COLOR = color;
        addClickEvent(ClickEvent.Action.OPEN_URL, url);
        addHoverEvent(HoverEvent.Action.SHOW_TEXT, "&fOnce pressed, you will be taken to the URL.\n&c" + url);
    }

}
