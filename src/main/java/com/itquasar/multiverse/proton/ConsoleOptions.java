package com.itquasar.multiverse.proton;

import java.util.*;

import static java.util.Collections.unmodifiableSet;


public class ConsoleOptions {


    private final Map<DefaultCommand, Set<String>> aliases = new HashMap<>();

    private final boolean systemExit;

    public ConsoleOptions() {
        this(true, Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET);
    }

    public ConsoleOptions(boolean systemExit, Set<String> clearCmds, Set<String> exitCmds, Set<String> historyCmds) {
        this.systemExit = systemExit;
        this.aliases.put(DefaultCommand.CLEAR, readOnlySet(clearCmds));
        this.aliases.put(DefaultCommand.EXIT, readOnlySet(exitCmds));
        this.aliases.put(DefaultCommand.HISTORY, readOnlySet(historyCmds));
    }

    private static <T> Set<T> notNullOrEmpty(Set<T> set) {
        return set == null ? Collections.EMPTY_SET : set;
    }

    private static <T> Set<T> readOnlySet(Set<T> set) {
        return unmodifiableSet(notNullOrEmpty(set));
    }


    // FIXME assure not null
    public Set<String> getDefaultCommandAliases(DefaultCommand commands) {
        return this.aliases.get(commands);
    }

    public boolean isSystemExit() {
        return systemExit;
    }


}
