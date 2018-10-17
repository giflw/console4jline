package com.itquasar.multiverse.proton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

public final class CommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private final ServiceLoader<Command> loader = ServiceLoader.load(Command.class);

    private final String packageToLoad;

    private CommandLine commandLine;

    public CommandManager() {
        this("");
    }

    public CommandManager(String packageToLoad) {
        this.packageToLoad = packageToLoad;
        this.getCommandLine(true);
    }

    public CommandLine getCommandLine() {
        return getCommandLine(false);
    }

    public CommandLine getCommandLine(boolean refresh) {
        if (refresh) {
            if (!packageToLoad.isEmpty()) {
                LOGGER.warn("Loading commands from package {}", packageToLoad);
            }

            CommandLine.Model.CommandSpec commandSpec = CommandLine.Model.CommandSpec.create();
            commandSpec.mixinStandardHelpOptions(true);

            Iterator<Command> iterator = loader.iterator();
            while (iterator.hasNext()) {
                Command command = iterator.next();
                if (packageToLoad.isEmpty() || command.getClass().getPackage().getName().equals(packageToLoad)) {
                    LOGGER.debug("Registering command {} [{}]", command.getName(), command.getClass().getCanonicalName());
                    commandSpec.addSubcommand(
                            command.getName(),
                            new CommandLine(Command.wrapIfNecessary(command))
                    );
                } else {
                    LOGGER.warn("Skipping {} [{}]", command.getName(), command.getClass().getCanonicalName());
                }
            }
            this.commandLine = new CommandLine(commandSpec);
        }
        return commandLine;
    }

    // FIXME create completer to jline using picocli API
    public Set<String> getComandNames() {
        return this.commandLine.getCommandSpec().subcommands().keySet();
    }
}
