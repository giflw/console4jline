package com.itquasar.multiverse.proton;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

/**
 * @param <T> Type supported for String conversion
 */
public abstract class PrettyPrinter<T> {

    public static final XLogger LOGGER = XLoggerFactory.getXLogger(PrettyPrinter.class);

    /**
     * 4 space characters
     */
    public static final String DEFAULT_SPACER = "  ";

    public static final Byte DEFAULT_PRIORITY = Byte.MIN_VALUE;

    private final Byte priority;

    private final String spacer;

    private final Class<T> supportedType;

    public PrettyPrinter() {
        this(DEFAULT_PRIORITY, DEFAULT_SPACER);
    }

    public PrettyPrinter(Byte priority, String spacer) {
        this.priority = priority;
        this.spacer = spacer;
        this.supportedType = (
                (Class<T>) (
                        (ParameterizedType) getClass().getGenericSuperclass()
                ).getActualTypeArguments()[0]
        );
    }

    public Class<T> getSupportedType() {
        return supportedType;
    }

    /**
     * @return A {@Byte} indicating the priority of this printer.
     * In case that exists two prettyPrinter mapped to same type, the one which have the greater priority will be used.
     * Default value i {@Byte#MIN_VALUE}
     */
    public final byte getPriority() {
        return priority;
    }

    /**
     * @param printWriter
     * @param object
     * @param manager
     * @return true if it was printed to given print writer
     */
    public final void prettyPrint(PrintWriter printWriter, T object, PrettyPrinterManager manager) {
        this.prettyPrint(0, printWriter, object, manager);
    }

    public final void prettyPrint(int level, PrintWriter printWriter, T object, PrettyPrinterManager manager) {
        this.prettyPrint(level, DEFAULT_SPACER, printWriter, object, manager);
    }


    /**
     * @param printWriter {@link PrintWriter} to print object to
     * @param object      Object to be printed.
     */
    public void prettyPrint(int level, String spacer, PrintWriter printWriter, T object, PrettyPrinterManager manager) {
        LOGGER.entry(level, spacer, printWriter, object != null ? object.getClass() : "<<NULL>>", manager);
        PrettyPrinter prettyPrinter = null;
        Class clazz = null;

        if (object != null) {
            Class[] classes = Stream.of(
                    new Class[]{object.getClass()},
                    object.getClass().getInterfaces(),
                    object.getClass().getClasses(),
                    object.getClass().getDeclaredClasses()
            ).flatMap(Stream::of)
                    .toArray(Class[]::new);
            LOGGER.trace("Hierarquical classes found: {}", classes);
            for (Class classe : classes) {
                LOGGER.trace("Searching pretty printer for {}", classe);
                prettyPrinter = manager.getPrettyPrinters().get(classe);
                if (prettyPrinter != null) {
                    clazz = classe;
                    break;
                }
            }
        }

        if (prettyPrinter != null) {
            LOGGER.trace("{}: {} [{}]", object.getClass(), prettyPrinter.getClass(), prettyPrinter.getSupportedType());
            prettyPrinter.prettyPrint(level, spacer, printWriter, object, manager);
        } else {
            String toString = object == null ? "<<NULL>>" : object.toString();
            LOGGER.trace("{}: Falling back to toString()", clazz);
            toString = StringUtils.leftPad("", spacer.length() * level, spacer) + toString;
            printWriter.println(toString);
        }
        LOGGER.exit();
    }

}
