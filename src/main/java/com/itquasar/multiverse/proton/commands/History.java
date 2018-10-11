package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.History.Entry;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class History implements Command<List<Entry>> {

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public Optional<List<Entry>> invoke(ParsedLine parsedLine, Console console) {
        LineReader lineReader = console.getLineReader();
        Terminal terminal = lineReader.getTerminal();

        List<Entry> history = new LinkedList<>();

        ListIterator<Entry> iterator = lineReader.getHistory().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            history.add(entry);
        }
        return Optional.of(history);
    }
}
