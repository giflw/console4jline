package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@CommandLine.Command
public class Log implements Command<String> {

    // FIXME move to console

    private static final Logger LOG_LOGGER = LoggerFactory.getLogger("com.itquasar.multiverse.proton.console.log");

    @CommandLine.Option(names = "-l", defaultValue = "info")
    private String levelOpt;

    @CommandLine.Parameters(defaultValue = "")
    private List<String> words = new LinkedList<>();

    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
        // support for levels (-l warning, error, trace debug, info)
        String msg = previousOutput.getResult().isPresent()
                ? previousOutput.getResult().get().toString()
                : StringUtils.join(words, " ");

        Level level = Arrays.stream(Level.values()).filter(
                lvl -> lvl.name().startsWith(levelOpt.toUpperCase())
        ).findFirst().orElse(Level.INFO);

        LOGGER.debug("Level: {}, Message: {}", level, msg);


        LOG_LOGGER.error("FIXME FIXME FIXME FIXME");
        return InterCommunication.of(level, msg);
    }

}
