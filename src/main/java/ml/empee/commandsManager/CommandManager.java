package ml.empee.commandsManager;

import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import ml.empee.commandsManager.command.Command;
import ml.empee.commandsManager.helpers.CommandMap;
import ml.empee.commandsManager.parsers.ParserManager;
import ml.empee.commandsManager.parsers.types.*;
import ml.empee.commandsManager.parsers.types.annotations.*;
import ml.empee.commandsManager.parsers.types.annotations.greedy.MsgParam;
import ml.empee.commandsManager.parsers.types.greedy.MsgParser;
import ml.empee.commandsManager.services.CompletionService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * This class provides an entry point for accessing the framework
 */
public final class CommandManager {

    private final ArrayList<Command> registeredCommands = new ArrayList<>();
    private final Logger logger;
    private CompletionService completionService = null;

    @Getter final JavaPlugin plugin;
    @Getter private final ParserManager parserManager;
    @Getter private final BukkitAudiences adventure;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.adventure = BukkitAudiences.create(plugin);
        this.logger = plugin.getLogger();

        this.parserManager = new ParserManager();
        registerDefaultParsers();

        setupCompletionService();
    }

    private void registerDefaultParsers() {
        parserManager.registerParser(IntegerParam.class, IntegerParser.class);
        parserManager.registerDefaultParser(int.class, IntegerParser.DEFAULT);
        parserManager.registerDefaultParser(Integer.class, IntegerParser.DEFAULT);

        parserManager.registerParser(FloatParam.class, FloatParser.class);
        parserManager.registerDefaultParser(float.class, FloatParser.DEFAULT);
        parserManager.registerDefaultParser(Float.class, FloatParser.DEFAULT);

        parserManager.registerParser(DoubleParam.class, DoubleParser.class);
        parserManager.registerDefaultParser(double.class, DoubleParser.DEFAULT);
        parserManager.registerDefaultParser(Double.class, DoubleParser.DEFAULT);

        parserManager.registerParser(LongParam.class, LongParser.class);
        parserManager.registerDefaultParser(long.class, LongParser.DEFAULT);
        parserManager.registerDefaultParser(Long.class, LongParser.DEFAULT);

        parserManager.registerParser(BoolParam.class, BoolParser.class);
        parserManager.registerDefaultParser(boolean.class, BoolParser.DEFAULT);
        parserManager.registerDefaultParser(Boolean.class, BoolParser.DEFAULT);

        parserManager.registerParser(PlayerParam.class, PlayerParser.class);
        parserManager.registerDefaultParser(Player.class, PlayerParser.DEFAULT);
        parserManager.registerDefaultParser(OfflinePlayer.class, new PlayerParser(
                "", false, ""
        ));

        parserManager.registerParser(StringParam.class, StringParser.class);
        parserManager.registerDefaultParser(String.class, StringParser.DEFAULT);

        parserManager.registerParser(MsgParam.class, MsgParser.class);

        parserManager.registerParser(ColorParam.class, ColorParser.class);
        parserManager.registerDefaultParser(ChatColor.class, ColorParser.DEFAULT);
    }
    private void setupCompletionService() {
        if(CommodoreProvider.isSupported()) {
            completionService = new CompletionService(CommodoreProvider.getCommodore(plugin));
            logger.info("Hooked to brigadier NMS");
        } else {
            logger.warning("Your server doesn't support the command completion");
        }
    }

    public void registerCommand(Command command) {
        PluginCommand pluginCommand = command.build(this);
        if(!CommandMap.register(pluginCommand)) {
            logger.warning("It already exists the command " + pluginCommand.getName() + ". Use /" + pluginCommand.getPlugin().getName().toLowerCase(Locale.ROOT) + ":" + pluginCommand.getName() + " instead");
        }

        registeredCommands.add(command);
        logger.info("The command '" + pluginCommand.getName() + "' has been registered");

        if(completionService != null) {
            completionService.registerCompletions(command);
            logger.info("Command completions for '" + pluginCommand.getName() + "' have been registered");
        }
    }
    public void unregisterCommands() {
        for(Command command : registeredCommands) {
            unregisterCommand(command);
        }
    }

    public void unregisterCommand(Command command) {
        CommandMap.unregisterCommand(command.getPluginCommand());
        logger.info("The command '" + command.getRootNode().getLabel() + "' has been unregistered");
    }

    public List<Command> getRegisteredCommands() {
        return Collections.unmodifiableList(registeredCommands);
    }

}
