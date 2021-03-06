package net.specialattack.spacore.api.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.specialattack.spacore.util.ChatFormat;
import net.specialattack.spacore.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Main command class, sub commands can be registered here to decrease the
 * amount of commands.
 */
public abstract class AbstractMultiCommand implements CommandExecutor, TabCompleter, ISubCommandHolder {

    public final Map<String, AbstractSubCommand> commands;
    public final Map<String, AbstractSubCommand> aliases;
    public String lastAlias;

    /**
     * Constructor, adds default commands to the list.
     */
    public AbstractMultiCommand() {
        this.commands = new TreeMap<>();
        this.aliases = new TreeMap<>();
    }

    public abstract String getDefaultCommand();

    @Override
    public void addSubCommand(String name, AbstractSubCommand command) {
        this.commands.put(name, command);
    }

    @Override
    public void addAlias(String name, AbstractSubCommand command) {
        this.aliases.put(name, command);
    }

    @Override
    public Map<String, AbstractSubCommand> getCommands() {
        return this.commands;
    }

    @Override
    public String getLastUsedAlias() {
        return this.lastAlias;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof BlockCommandSender) {
            // We don't work with command blocks
            return false;
        }

        this.lastAlias = alias;

        try {
            if (args.length == 0) {
                this.commands.get(this.getDefaultCommand()).runCommand(sender);
            } else {
                AbstractSubCommand subCommand = this.commands.get(args[0]);

                if (subCommand == null) {
                    subCommand = this.aliases.get(args[0]);
                }

                if (subCommand == null) {
                    sender.sendMessage(ChatColor.RED + "Unkown command, please type /" + alias + " help for a list of commands.");
                    return true;
                }

                if (!subCommand.canUseCommand(sender)) {
                    sender.sendMessage(ChatColor.RED + "You cannot use this command.");
                    return true;
                }

                if (!subCommand.hasPermission(sender)) {
                    sender.sendMessage(ChatColor.RED + "You do not have permissions to use this command.");
                    return true;
                }

                String[] newArgs = new String[args.length - 1];

                System.arraycopy(args, 1, newArgs, 0, args.length - 1);

                subCommand.parseParameters(sender, args[0], newArgs);
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.DARK_RED + "An error occoured while performing command");
            if (e.message != null && e.params != null) {
                sender.sendMessage(ChatFormat.format(e.message, ChatColor.RED, e.params));
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occoured while performing command");
            sender.sendMessage(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> possibles = new ArrayList<>();

            Set<Entry<String, AbstractSubCommand>> commandSet = this.commands.entrySet();

            for (Entry<String, AbstractSubCommand> entry : commandSet) {
                AbstractSubCommand subCommand = entry.getValue();

                if (!subCommand.canUseCommand(sender)) {
                    continue;
                }
                if (!subCommand.hasPermission(sender)) {
                    continue;
                }

                possibles.add(subCommand.name);

                if (args[0].length() > 0) {
                    Collections.addAll(possibles, subCommand.aliases);
                }
            }

            String lower = args[args.length - 1].toLowerCase();

            return possibles.stream().map(String::toLowerCase).filter(possible -> possible.startsWith(lower)).collect(Collectors.toList());
        } else {
            AbstractSubCommand subCommand = this.commands.get(args[0]);

            if (subCommand == null) {
                subCommand = this.aliases.get(args[0]);
            }

            if (subCommand == null) {
                return ChatUtil.TAB_RESULT_EMPTY;
            }

            if (!subCommand.canUseCommand(sender)) {
                return ChatUtil.TAB_RESULT_EMPTY;
            }

            if (!subCommand.hasPermission(sender)) {
                return ChatUtil.TAB_RESULT_EMPTY;
            }

            String[] newArgs = new String[args.length - 1];

            System.arraycopy(args, 1, newArgs, 0, args.length - 1);

            List<String> possibles;
            try {
                possibles = subCommand.getTabCompleteResults(sender, newArgs);
            } catch (CommandException e) {
                return ChatUtil.TAB_RESULT_EMPTY;
            }

            if (possibles != null) {
                String lower = args[args.length - 1].toLowerCase();
                return possibles.stream().map(String::toLowerCase).filter(possible -> possible.startsWith(lower)).collect(Collectors.toList());
            } else {
                return null;
            }
        }
    }
}
