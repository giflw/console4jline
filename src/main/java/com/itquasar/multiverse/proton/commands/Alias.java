package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import picocli.CommandLine;

// FIXME
public class Alias implements Command<Void> {

    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
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
        return InterCommunication.ok();
    }
}
