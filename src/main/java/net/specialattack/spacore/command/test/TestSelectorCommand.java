package net.specialattack.spacore.command.test;

import java.util.List;
import net.specialattack.spacore.api.command.AbstractSubCommand;
import net.specialattack.spacore.api.command.ISubCommandHolder;
import net.specialattack.spacore.api.command.parameter.EntityCollectionEasyParameter;
import net.specialattack.spacore.util.ChatFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Command that displays version info about SpACore etc...
 */
public class TestSelectorCommand extends AbstractSubCommand {

    private final EntityCollectionEasyParameter entities;
    private int counter;

    public TestSelectorCommand(ISubCommandHolder command, String name, String permissions, String... aliases) {
        super(command, name, permissions, aliases);
        this.addParameter(this.entities = new EntityCollectionEasyParameter().setTakeAll());
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        final List<Entity> entities = this.entities.get();

        sender.sendMessage(ChatFormat.format("Input selected %s entities", ChatColor.GREEN, entities.size()));

        this.counter = 0;

        entities.forEach(entity -> {
            if (TestSelectorCommand.this.counter > 15) {
                return;
            }
            if (entity.getCustomName() != null) {
                sender.sendMessage(ChatFormat.format("%s: %s (%s)", ChatColor.GREEN, entity.getType(), entity.getCustomName(), entity.getUniqueId()));
            } else if (entity instanceof Player) {
                sender.sendMessage(ChatFormat.format("%s: %s (%s)", ChatColor.GREEN, entity.getType(), entity.getName(), entity.getUniqueId()));
            } else {
                sender.sendMessage(ChatFormat.format("%s: %s", ChatColor.GREEN, entity.getType(), entity.getUniqueId()));
            }

            TestSelectorCommand.this.counter++;
        });
    }
}
