package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;

import java.util.Optional;

// FIXME TEST
public class Echo implements Command<String> {

    @Override
    public Optional<String> invoke(ParsedLine parsedLine, Console console) {
        System.out.println(parsedLine.wordCursor() + " | " + parsedLine.wordIndex() + " --> " + parsedLine.word());
        System.out.println(parsedLine.wordCursor() + " | " + parsedLine.wordIndex() + " --> " + parsedLine.word());
        String str = parsedLine.line();
        return Optional.ofNullable(str);
    }
}
