package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.UserInterruptException;

import java.util.List;
import java.util.Optional;

public class Exit implements Command<Void> {

    @Override
    public Optional<Void> invoke(List<String> parsedLine, Console console, Optional previousOutput) {
        if (console.getOptions().isSystemExit()) {
            System.exit(0);
        }
        throw new UserInterruptException("Exiting console");
    }
}
