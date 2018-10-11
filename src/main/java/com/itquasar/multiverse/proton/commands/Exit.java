package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;

import java.util.Optional;

public class Exit implements Command<Void> {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public Optional<Void> invoke(ParsedLine parsedLine, Console console) {
        if (console.getOptions().isSystemExit()) {
            System.exit(0);
        }
        throw new UserInterruptException("Exiting console");
    }
}
