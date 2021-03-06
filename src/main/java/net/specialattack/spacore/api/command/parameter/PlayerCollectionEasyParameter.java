package net.specialattack.spacore.api.command.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.specialattack.spacore.api.command.CommandException;
import net.specialattack.spacore.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerCollectionEasyParameter extends AbstractEasyParameter.Multi<List<Player>> {

    public PlayerCollectionEasyParameter() {
        this.setName("player");
    }

    @Override
    public boolean parse(CommandSender sender, String value) {
        Location location = sender instanceof Entity ? ((Entity) sender).getLocation() : null;
        try {
            Set<Entity> matched = Util.matchEntities(value, location, EntityType.PLAYER);
            if (matched.isEmpty()) {
                return false;
            }
            List<Player> players = new ArrayList<Player>(matched.size());
            players.addAll(matched.stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).collect(Collectors.toList()));
            this.setValue(new ArrayList<>(players));
            return true;
        } catch (IllegalArgumentException e) {
            this.setValue(null);
            throw new CommandException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String input) {
        return null;
    }
}
