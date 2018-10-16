package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class Echo implements Command<String> {

    @Override
    public Optional<String> invoke(List<String> parsedLine, Console console, Optional previousOutput) {
        return Optional.ofNullable(
                previousOutput.orElse(
                        StringUtils.join(parsedLine.subList(1, parsedLine.size()), " ")
                ).toString()
        );
    }
}
