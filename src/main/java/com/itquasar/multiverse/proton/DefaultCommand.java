package com.itquasar.multiverse.proton;

import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.*;
import java.util.function.BiConsumer;

public enum DefaultCommand {
    CLEAR(
            (lr, opts) -> {
                Terminal terminal = lr.getTerminal();
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();
            },
            "clear"
    ),
    EXIT(
            (lr, opts) -> {
                if (opts.isSystemExit()) {
                    System.exit(0);
                }
            },
            "exit"
    ),
    HISTORY(
            (lr, opts) -> {
                ListIterator<History.Entry> iterator = lr.getHistory().iterator();
                while (iterator.hasNext()) {
                    History.Entry entry = iterator.next();
                    //lr.getTerminal().writer().println(entry.time() + ": " + entry.line());
                    lr.getTerminal().writer().println(entry.index() + ": " + entry.line());
                }
            },
            "history"
    );

    private final BiConsumer<LineReader, ConsoleOptions> function;
    private final Set<String> aliasesFallback;


    DefaultCommand(BiConsumer<LineReader, ConsoleOptions> function, String... aliases) {
        this(function, Collections.unmodifiableSet(new TreeSet<>(Arrays.asList(aliases))));
    }

    DefaultCommand(BiConsumer<LineReader, ConsoleOptions> function, Set<String> aliasesFallback) {
        this.aliasesFallback = aliasesFallback;
        this.function = function;
    }

    public static void invoke(String word, LineReader lineReader, ConsoleOptions options) {
        for (DefaultCommand cmd : values()) {
            Set<String> aliases = options.getDefaultCommandAliases(cmd);
            aliases = aliases.isEmpty() ? cmd.getAliasesFallback() : aliases;
            if (aliases.contains(word)) {
                cmd.function.accept(lineReader, options);
            }
        }
    }

    public static String[] aliases() {
        List<String> aliases = new LinkedList<>();
        for (DefaultCommand cmd : values()) {
            aliases.addAll(cmd.aliasesFallback);
        }
        return aliases.toArray(new String[0]);
    }

    public BiConsumer<LineReader, ConsoleOptions> getFunction() {
        return function;
    }

    public Set<String> getAliasesFallback() {
        return aliasesFallback;
    }
}
