package net.specialattack.bukkit.core.command.easy.parameter;

public interface IEasySource<T> {

    /**
     * Returns the value the source contains.
     *
     * @return The value the source contains, or null if it doesn't contain a value.
     */
    T getValue();

}