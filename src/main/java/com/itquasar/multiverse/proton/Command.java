package com.itquasar.multiverse.proton;

import com.google.common.base.CaseFormat;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.List;
import java.util.Optional;

public interface Command<T> {

    XLogger LOGGER = XLoggerFactory.getXLogger(Command.class);

    String EMPTY_STRING = "";

    default String getName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.getClass().getSimpleName());
    }

    Optional<T> invoke(List<String> parsedLine, Console console, Optional previousOutput);

    default String getDescription() {
        return EMPTY_STRING;
    }

    default String getShortHelp() {
        return EMPTY_STRING;
    }

    // Markdown Support? File support?
    default String getFullHelp() {
        return EMPTY_STRING;
    }

}
