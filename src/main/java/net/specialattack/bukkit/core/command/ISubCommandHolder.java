package net.specialattack.bukkit.core.command;

import java.util.Map;

public interface ISubCommandHolder {

    void addSubCommand(String name, AbstractSubCommand command);

    void addAlias(String name, AbstractSubCommand command);

    Map<String, AbstractSubCommand> getCommands();

    String getLastUsedAlias();
}
