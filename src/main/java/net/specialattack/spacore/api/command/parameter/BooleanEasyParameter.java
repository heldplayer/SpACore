package net.specialattack.spacore.api.command.parameter;

import java.util.List;
import net.specialattack.spacore.util.ChatUtil;
import org.bukkit.command.CommandSender;

public class BooleanEasyParameter extends AbstractEasyParameter<Boolean> {

    public BooleanEasyParameter() {
        this.setName("true/false");
    }

    @Override
    public boolean parse(CommandSender sender, String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("allow")) {
            this.setValue(true);
            return true;
        }
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("deny")) {
            this.setValue(false);
            return true;
        }

        this.setValue(null);
        return false;
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String input) {
        return ChatUtil.TAB_RESULT_TRUE_FALSE;
    }
}
