package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;

import java.util.Optional;

// FIXME
public class Alias implements Command<Void> {

    @Override
    public Optional<Void> invoke(ParsedLine parsedLine, Console console) {
        // alias ll = ls - lah
        //
//        Command command = new Command() {
//            @Override
//            public String getName() {
//                return null;
//            }
//
//            @Override
//            public Optional invoke(ParsedLine parsedLine, Console console) {
//                return Optional.empty();
//            }
//        }
//        console.registerCommand()
        return Optional.empty();
    }
}
