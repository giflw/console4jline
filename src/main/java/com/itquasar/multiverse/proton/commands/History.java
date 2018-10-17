package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.History.Entry;
import org.jline.reader.LineReader;
import picocli.CommandLine;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class History implements Command<List<Entry>> {

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional<?> previousOutput) {
        LineReader lineReader = console.getLineReader();

        List<Entry> history = new LinkedList<>();

        ListIterator<Entry> iterator = lineReader.getHistory().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            history.add(entry);
        }
        return Optional.of(history);
    }
}
