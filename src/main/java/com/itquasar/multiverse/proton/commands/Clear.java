package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import picocli.CommandLine;

public class Clear implements Command<Void> {

    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
        Terminal terminal = console.getLineReader().getTerminal();
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        return InterCommunication.ok();
    }
}
