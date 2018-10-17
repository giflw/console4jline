package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import picocli.CommandLine;

import java.util.Optional;

public class Clear implements Command<Void> {

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional<?> previousOutput) {
        Terminal terminal = console.getLineReader().getTerminal();
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        return Optional.empty();
    }
}
