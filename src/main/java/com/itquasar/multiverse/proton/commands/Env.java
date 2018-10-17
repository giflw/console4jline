package com.itquasar.multiverse.proton.commands;

import com.itquasar.multiverse.proton.Command;
import com.itquasar.multiverse.proton.Console;
import com.itquasar.multiverse.proton.InterCommunication;
import picocli.CommandLine;

import java.util.List;
import java.util.Map;

@CommandLine.Command
// FIXME not working
public class Env implements Command<Object> {

    @CommandLine.Parameters(index = "0")
    private Action action = Action.none;

    @CommandLine.Parameters(defaultValue = "")
    private List<String> keyPath;

    @CommandLine.Option(names = {"-v"}, defaultValue = "null")
    private Object value;

    private static Object get(List<String> subList, Console console) {
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

    // FIXME add error handling
    private static String set(List<String> keys, Object value, Console console, InterCommunication previousOutput) {
        Map<String, Object> parent = console.getEnv();
        int depth = 0;
        while (depth < keys.size() - 1) {
            parent = (Map) get(keys.subList(0, depth), console);
        }

        String key = keys.get(keys.size() - 1);
        if (!key.isEmpty()) {
            if (value.equals("null") || (value.equals("--") && !previousOutput.getResult().isPresent())) {
                parent.remove(key);
            } else if (value.equals("--")) {
                parent.put(key, previousOutput.getResult().get());
            } else {
                parent.put(key, value);
            }
        }
        return "";
    }

    // FIXME add error support
    @Override
    public InterCommunication invoke(CommandLine commandLine, Console console, InterCommunication<?> previousOutput) {
        LOGGER.trace("env {}: {} -> {}", action, keyPath, value);
        InterCommunication result = null;
        switch (action) {
//            case set:
//                result = Optional.ofNullable(
//                        set(
//                                keyPath, value, console, previousOutput
//                        )
//                );
//                break;
//            case get:
//                result = Optional.ofNullable(
//                        get(keyPath, console)
//                );
//                break;
            default:
                action = Action.none;
                result = InterCommunication.ok();//of(console.getEnv());
        }
        keyPath.clear();

        return result;
    }

    public enum Action {
        get, set, none
    }

}
