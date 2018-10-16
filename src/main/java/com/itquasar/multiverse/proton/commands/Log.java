package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.apache.commons.lang3.StringUtils;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Log implements Command<String> {

    private static final Logger LOG_LOGGER = LoggerFactory.getLogger("console.log");

    @Override
    public Optional<String> invoke(List<String> parsedLine, Console console, Optional previousOutput) {
        List<String> words = new LinkedList<>(parsedLine.subList(1, parsedLine.size()));

        String levelStr = "INFO";
        // support for levels (--warning, --error, --trace --debug, --info)
        if (!words.isEmpty() && words.get(0).startsWith("--")) {
            levelStr = words.remove(0).replaceAll("-", "").toUpperCase();
        }
        String msg = previousOutput.isPresent()
                ? previousOutput.get().toString()
                : StringUtils.join(words, " ");

        String aux = levelStr;
        Level level = Arrays.stream(Level.values()).filter(
                lvl -> lvl.name().startsWith(aux)
        ).findFirst().orElse(Level.INFO);

        LOGGER.debug("Level: {}, Message: {}", level, msg);

        LOG_LOGGER.log(level, msg);
        return Optional.empty();
    }

}
