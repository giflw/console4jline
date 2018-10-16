package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.util.Optional;

public class Log implements Command<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);

    @Override
    public Optional<String> invoke(ParsedLine parsedLine, Console console) {
        // FIXME support for levels (--warning, --error, --trace --debug, --info)
        String msg = null;
        String levelStr = "".replaceAll("-", "").toUpperCase();
        Level level = Level.INFO;
        for (Level slf4jlevel : Level.values()) {
            if (level.name().startsWith(levelStr)) {
                level = slf4jlevel;
            }
        }
        LOGGER.log(level, msg);
        return Optional.empty();
    }

}
