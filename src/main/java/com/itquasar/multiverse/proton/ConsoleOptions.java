package com.itquasar.multiverse.proton;

import java.util.Collections;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;


public class ConsoleOptions {


    private final boolean systemExit;

    public ConsoleOptions() {
        this(true);
    }

    public ConsoleOptions(boolean systemExit) {
        this.systemExit = systemExit;
    }

    private static <T> Set<T> notNullOrEmpty(Set<T> set) {
        return set == null ? Collections.EMPTY_SET : set;
    }

    private static <T> Set<T> readOnlySet(Set<T> set) {
        return unmodifiableSet(notNullOrEmpty(set));
    }


    public boolean isSystemExit() {
        return systemExit;
    }


}
