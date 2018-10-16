package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Env implements Command<Object> {

    // FIXME add error support
    // FIXME add chain support
    // FIXME add set with chain support
    @Override
    public Optional<Object> invoke(ParsedLine parsedLine, Console console) {
        LOGGER.trace("env parsed line: {}", parsedLine.words());
        List<String> words = parsedLine.words();
        if (words.size() > 1) {
            switch (words.get(1).toLowerCase()) {
                case "set":
                    return Optional.ofNullable(
                            set(
                                    words.size() > 2 ? words.get(2) : "",
                                    words.size() > 3 ? words.get(3) : "null",
                                    console
                            )
                    );
                case "get":
                    return Optional.ofNullable(
                            get(
                                    words.subList(2, words.size()),
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
            System.out.println("=================");
            System.out.println("=================");
            System.out.println(value);
            System.out.println("=================");
            System.out.println("=================");
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

    private String set(String key, String value, Console console) {
        if (!key.isEmpty()) {
            if (value.equals("null")) {
                console.getEnv().remove(key);
            } else {
                console.getEnv().put(key, value);
            }
        }
        return "";
    }
}
