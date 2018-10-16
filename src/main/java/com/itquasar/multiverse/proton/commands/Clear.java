package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.List;
import java.util.Optional;

public class Clear implements Command<Void> {

    @Override
    public Optional<Void> invoke(List<String> parsedLine, Console console, Optional previousOutput) {
        Terminal terminal = console.getLineReader().getTerminal();
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        return Optional.empty();
    }
}
