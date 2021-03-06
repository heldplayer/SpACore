package net.specialattack.spacore.command;

import net.specialattack.spacore.api.command.AbstractSubCommand;
import net.specialattack.spacore.api.command.ISubCommandHolder;
import net.specialattack.spacore.util.ChatFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Command that displays version info about a plugin.
 */
public class VersionSubCommand extends AbstractSubCommand {

    private final PluginDescriptionFile description;

    public VersionSubCommand(ISubCommandHolder command, PluginDescriptionFile description, String name, String permissions, String... aliases) {
        super(command, name, permissions, aliases);
        this.description = description;
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        sender.sendMessage(ChatFormat.format("========== %s ==========", ChatColor.GRAY, ChatColor.GREEN, this.description.getFullName()));
        String[] authors = this.description.getAuthors().toArray(new String[this.description.getAuthors().size()]);
        String joined = "";
        for (int i = 0; i < authors.length; i++) {
            if (i != 0) {
                joined += ", ";
            }

            joined += "%s";
        }
        sender.sendMessage(ChatFormat.format("Authors: " + joined, ChatColor.YELLOW, ChatColor.GRAY, (Object[]) authors));
    }
}
