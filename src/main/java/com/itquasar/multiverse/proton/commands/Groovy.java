package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.LinkedList;
import java.util.List;

public class Groovy implements Command {

    private GroovyShell groovyShell;

    @CommandLine.Unmatched
    private List<String> words = new LinkedList<>();

    @Override
    // FIXME error handling
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication previousOutput) {
        if (groovyShell == null) {
            groovyShell = new GroovyShell();
        }
        groovyShell.setVariable("console", console);
        groovyShell.setVariable("previousOutput", previousOutput);

        Object evaluate = groovyShell.evaluate(
                StringUtils.join(words, "")
        );
        return InterCommunication.of(evaluate);
    }
}
