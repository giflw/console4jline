package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.LinkedList;
import java.util.List;

@CommandLine.Command(name = "echo")
public class Echo implements Command<String> {

    @CommandLine.Parameters(defaultValue = "")
    private List<String> args = new LinkedList<>();

    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
        InterCommunication<String> optional = InterCommunication.of(StringUtils.join(args, " "));
        return optional;
    }
}
