package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Env implements Command<Object> {

    // FIXME add error support
    @Override
    public Optional<Object> invoke(List<String> parsedLine, Console console, Optional previousOutput) {
        LOGGER.trace("env parsed line: {}", parsedLine);
        if (parsedLine.size() > 1) {
            switch (parsedLine.get(1).toLowerCase()) {
                case "set":
                    return Optional.ofNullable(
                            set(
                                    parsedLine.size() > 2 ? parsedLine.get(2) : "",
                                    parsedLine.size() > 3 ? parsedLine.get(3) : "null",
                                    console,
                                    previousOutput
                            )
                    );
                case "get":
                    return Optional.ofNullable(
                            get(
                                    parsedLine.subList(2, parsedLine.size()),
                                    console
                            )
                    );
            }
        }
        return Optional.of(console.getEnv());
    }

    private Object get(List<String> subList, Console console) {
        LOGGER.debug("env get {}", subList);
        Object value = null;
        Map<String, Object> env = console.getEnv();
        int level = 0;
        for (String key : subList) {
            LOGGER.trace("Searching {} in env level {}", key, level++);
            value = env.get(key);
            if (value == null) {
                break;
            }
            if (value instanceof Map) {
                env = (Map) value;
            }
        }
        LOGGER.trace("Found {} in env for key {}", value, subList);
        return value;
    }

    private String set(String key, String value, Console console, Optional previousOutput) {
        if (!key.isEmpty()) {
            if (value.equals("null") || (value.equals("--") && !previousOutput.isPresent())) {
                console.getEnv().remove(key);
            } else if (value.equals("--")) {
                console.getEnv().put(key, previousOutput.get());
            } else {
                console.getEnv().put(key, value);
            }
        }
        return "";
    }
}
