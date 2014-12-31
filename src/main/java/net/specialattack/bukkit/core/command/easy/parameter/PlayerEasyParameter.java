package net.specialattack.bukkit.core.command.easy.parameter;

import java.util.List;
import net.specialattack.bukkit.core.command.CommandException;
import net.specialattack.bukkit.core.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerEasyParameter extends AbstractEasyParameter<Player> {

    public PlayerEasyParameter() {
        this.setName("player");
    }

    @Override
    public boolean parse(CommandSender sender, String value) {
        Location location = sender instanceof Entity ? ((Entity) sender).getLocation() : null;
        Player result = null;
        try {
            List<Entity> matched = Util.matchEntities(value, location, EntityType.PLAYER);
            if (matched.isEmpty()) {
                return false;
            }
            for (Entity entity : matched) {
                if (entity instanceof Player) {
                    result = (Player) entity;
                }
            }
            this.setValue(result);
        } catch (IllegalArgumentException e) {
            this.setValue(null);
            throw new CommandException(e.getMessage(), e);
        }
        return result != null;
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String input) {
        return null;
    }

}
