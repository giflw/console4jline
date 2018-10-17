package com.itquasar.multiverse.proton;

import lombok.Data;
import uk.org.lidalia.slf4jext.Level;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Data
public class InterCommunication<T> {


    private final Optional<T> result;
    private final Status status;
    private final List<Throwable> throwables;
    private final List<LogRecord> logs;


    public InterCommunication(T result, Status status, List<Throwable> throwables, List<LogRecord> logs) {
        this(Optional.ofNullable(result), status, throwables, logs);
    }

    public InterCommunication(Optional<T> result, Status status, List<Throwable> throwables, List<LogRecord> logs) {
        this.result = result;
        this.status = status;
        this.throwables = throwables == null ? Collections.EMPTY_LIST : throwables;
        this.logs = logs == null ? Collections.EMPTY_LIST : logs;
    }

    public static <T> InterCommunication<T> ok() {
        return new InterCommunication<>((T) null, Status.OK, null, null);
    }

    public static <T> InterCommunication<T> of(T result) {
        return new InterCommunication<>(result, Status.OK, null, null);
    }

    public static <T> InterCommunication<T> of(Throwable throwable) {
        List<Throwable> throwables = new LinkedList<>();
        throwables.add(throwable);
        return new InterCommunication<>((T) null, Status.ERROR, throwables, null);
    }

    public static <T> InterCommunication<T> of(Level level, String message) {
        LogRecord logRecord = new LogRecord(level, message, null);
        List<LogRecord> logs = new LinkedList<>();
        logs.add(logRecord);
        return new InterCommunication<>((T) null, Status.OK, null, logs);
    }

    public void visit(Consumer<InterCommunication<?>> visitor){
        visitor.accept(this);
    }


    public enum Status {
        OK, ERROR;
    }

    @Data
    public static class LogRecord {
        private final Level level;
        private final Optional<String> message;
        private final Optional<Throwable> throwable;

        public LogRecord(String message) {
            this(null, message, null);
        }

        public LogRecord(Level level, String message) {
            this(level, message, null);
        }

        public LogRecord(Level level, String message, Throwable throwable) {
            this.level = level == null ? Level.INFO : level;
            this.message = Optional.ofNullable(message);
            this.throwable = Optional.ofNullable(throwable);
        }
    }

}
