package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@CommandLine.Command(name = "echo")
public class Echo implements Command<String> {

    @CommandLine.Parameters(defaultValue = "")
    private List<String> args = new LinkedList<>();

    @Override
    public Optional invoke(CommandLine commandLine, Console console, Optional<?> previousOutput) {
        Optional<String> optional = Optional.of(StringUtils.join(args, " "));
        return optional;
    }
}
