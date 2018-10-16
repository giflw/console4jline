package com.itquasar.multiverse.proton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class CommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private final ServiceLoader<Command> loader = ServiceLoader.load(Command.class);

    private final String packageToLoad;

    private Map<String, Command> commandMap;

    public CommandManager() {
        this("");
    }

    public CommandManager(String packageToLoad) {
        this.packageToLoad = packageToLoad;
        this.getCommands(true);
    }

    public Map<String, Command> getCommands() {
        return getCommands(false);
    }

    public Map<String, Command> getCommands(boolean refresh) {
        if (refresh) {
            if(!packageToLoad.isEmpty()) {
                LOGGER.warn("Loading commands from package {}", packageToLoad);
            }
            Map<String, Command> commandMap = new HashMap<>();
            Iterator<Command> iterator = loader.iterator();
            while (iterator.hasNext()) {
                Command command = iterator.next();
                if (packageToLoad.isEmpty() || command.getClass().getPackage().getName().equals(packageToLoad)) {
                    LOGGER.debug("Registering command {} [{}]", command.getName(), command.getClass().getCanonicalName());
                    commandMap.put(command.getName(), command);
                } else {
                    LOGGER.warn("Skipping {} [{}]", command.getName(), command.getClass().getCanonicalName());
                }
            }
            this.commandMap = commandMap;
        }
        return commandMap;
    }

    public Set<String> getComandNames() {
        return this.commandMap.keySet();
    }
}
