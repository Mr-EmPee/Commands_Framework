package ml.empee.commandsManager.command;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CommandContext {

    private static final String INVALID_SENDER = "&4&l > &cYou aren't an allowed sender type of this command";

    private final HashMap<String, Object> arguments = new HashMap<>();
    private final CommandSender source;

    public CommandContext(CommandSender source) {
        this.source = source;
    }

    public void addArgument(String id, Object arg) {
        arguments.put(id, arg);
    }
    public <T> T getArgument(String id) {
        return (T) arguments.get(id);
    }

    void addArguments(Map.Entry<String, Object>[] arguments) {
        for(Map.Entry<String, Object> arg : arguments) {
            String key = arg.getKey();
            if(key != null && !key.isEmpty()) {
                this.arguments.put(key, arg.getValue());
            }
        }
    }

    /**
     * Gets the command source
     * @throws CommandException if the source doesn't match the sourceClazz
     * @param sourceClazz The class that the command source must match
     */
    public <T> T getSource(Class<? extends T> sourceClazz) {
        if(sourceClazz.isInstance(source)) {
            return (T) source;
        }
        throw new CommandException(INVALID_SENDER);
    }

}
