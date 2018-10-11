package com.itquasar.multiverse.proton;

import org.jline.reader.ParsedLine;

import java.util.Optional;

public interface Command<T> {

    String EMPTY_STRING = "";

    String getName();

    Optional<T> invoke(ParsedLine parsedLine, Console console);

    default Optional<T> invoke(String line, Console console)  {
        return invoke(console.getLineReader().getParser().parse(line, 0), console);
    }

    default String getDescription() {
        return EMPTY_STRING;
    }

    default String getShortHelp() {
        return EMPTY_STRING;
    }

    // Markdown Support? File support?
    default String getFullHelp() {
        return EMPTY_STRING;
    }

}
