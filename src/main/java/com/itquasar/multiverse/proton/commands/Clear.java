package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.Optional;

public class Clear implements Command<Void> {

    @Override
    public Optional<Void> invoke(ParsedLine parsedLine, Console console) {
        Terminal terminal = console.getLineReader().getTerminal();
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        return Optional.empty();
    }
}
