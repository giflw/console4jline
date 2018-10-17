package com.itquasar.multiverse.proton;

import com.google.common.base.CaseFormat;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.util.Optional;

// FIXME its possible to use Callable?
public interface Command<T> {

    XLogger LOGGER = XLoggerFactory.getXLogger(Command.class);
    String DEFAULT_PICOCLI_COMMAND_NAME = picocliDefaultCommandName();

    static <T> Command<T> wrapIfNecessary(Command<T> command) {
        return command.getClass().getAnnotation(CommandLine.Command.class) != null
                ? command : new WrappedCommand<>(command);
    }

    static String picocliDefaultCommandName() {
        try {
            Method method = CommandLine.Command.class.getMethod("name");
            return (String) method.getDefaultValue();
        } catch (NoSuchMethodException e) {
            LOGGER.error("Error reading default name for picocli Command annontation");
        }
        return null;
    }

    default String getName() {
        CommandLine.Command annotation = this.getClass().getAnnotation(CommandLine.Command.class);
        String name = annotation != null && !annotation.name().equals(DEFAULT_PICOCLI_COMMAND_NAME)
                ? annotation.name() : null;

        return name != null
                ? name
                : CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName());
    }

    Optional<T> invoke(CommandLine commandLine, Console console, Optional<?> previousOutput);

    @CommandLine.Command
    class WrappedCommand<T> implements Command<T> {

        private final Command<T> command;

        public WrappedCommand(Command<T> command) {
            this.command = command;
        }

        @Override
        public Optional<T> invoke(CommandLine commandLine, Console console, Optional previousOutput) {
            return command.invoke(commandLine, console, previousOutput);
        }
    }

}
