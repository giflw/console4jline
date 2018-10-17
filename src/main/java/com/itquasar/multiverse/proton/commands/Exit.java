package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import org.jline.reader.UserInterruptException;
import picocli.CommandLine;

public class Exit implements Command<Void> {

    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
        if (console.getOptions().isSystemExit()) {
            System.exit(0);
        }
        throw new UserInterruptException("Exiting console");
    }
}
