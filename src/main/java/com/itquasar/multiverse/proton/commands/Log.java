package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@CommandLine.Command
public class Log implements Command<String> {

    private static final Logger LOG_LOGGER = LoggerFactory.getLogger("com.itquasar.multiverse.proton.console.log");

    @CommandLine.Option(names = "-l", defaultValue = "info")
    private String levelOpt;

    @CommandLine.Parameters(defaultValue = "")
    private List<String> words = new LinkedList<>();

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional<?> previousOutput) {
        // support for levels (-l warning, error, trace debug, info)
        String msg = previousOutput.isPresent()
                ? previousOutput.get().toString()
                : StringUtils.join(words, " ");

        Level level = Arrays.stream(Level.values()).filter(
                lvl -> lvl.name().startsWith(levelOpt.toUpperCase())
        ).findFirst().orElse(Level.INFO);

        LOGGER.debug("Level: {}, Message: {}", level, msg);

        LOG_LOGGER.log(level, msg);
        return Optional.empty();
    }

}
