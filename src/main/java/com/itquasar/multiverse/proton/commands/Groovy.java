package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Groovy implements Command {

    private GroovyShell groovyShell;

    @CommandLine.Unmatched
    private List<String> words = new LinkedList<>();

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional previousOutput) {
        if (groovyShell == null) {
            groovyShell = new GroovyShell();
        }
        groovyShell.setVariable("console", console);
        groovyShell.setVariable("previousOutput", previousOutput);

        Object evaluate = groovyShell.evaluate(
                StringUtils.join(words, "")
        );
        return Optional.ofNullable(evaluate);
    }
}
