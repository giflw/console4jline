package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import picocli.CommandLine;

import java.util.Optional;

// FIXME
public class Alias implements Command<Void> {

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional<?> previousOutput) {
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
