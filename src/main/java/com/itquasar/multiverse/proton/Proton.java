package com.itquasar.multiverse.proton;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.SimpleMaskingCallback;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;

public class Proton {

    public static final Character ATOM = '⚛';
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Proton.class);

    // MUST BE THE FIRST BLOCK OF CODE IN THE MAIN CLASS!!!!
    static {
        try {
            LogManager.getLogManager().reset();
            SLF4JBridgeHandler.install();

            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ContextInitializer ci = new ContextInitializer(loggerContext);
            URL url = ci.findURLOfDefaultConfigurationFile(true);
            loggerContext.reset();
            ci.configureByResource(url);
        } catch (JoranException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .name("Proton")
                .system(true)
                //.signalHandler(Terminal.SignalHandler.SIG_IGN)
                //.jna(false) // FIXME make configurable
                //.jansi(false) // FIXME make configurable
                //.exec(false) // FIXME make configurable
                .build();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter(DefaultCommand.aliases()))
                .variable(LineReader.HISTORY_FILE, "/tmp/" + terminal.getName().replace(" ", "_") + ".history")
                .build();

        Console console = new Console(lineReader, new SimpleMaskingCallback(ATOM));

        while (true) {
            ParsedLine parsedLine = console.readParsedLine();
            if (parsedLine.word().equals("log")) {
                List<String> words = parsedLine.words();
                String msg = StringUtils.join(new LinkedList<>(words).subList(0, words.size() - 1), " ");
                System.out.println("LOG: " + msg);
                LOGGER.info(msg);
            }
            if (parsedLine.word().equals("su")) {
                String password = console.readPassword();
                System.out.println("PASSWORD: " + password);
            }
        }

    }

}
