package com.itquasar.multiverse.proton;

import org.jline.reader.*;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Console implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

    private final AtomicBoolean passwordInput = new AtomicBoolean(false);

    private final LineReader lineReader;
    private final MaskingCallback maskingCallback;

    private final ConsoleOptions consoleOptions;

    private final CommandManager commandManager;

    private final PrettyPrinterManager prettyPrinterManager;

    private final Map<String, Object> env = new HashMap();

    private int line = 0;

    public Console(CommandManager commandManager, PrettyPrinterManager prettyPrinterManager, LineReader lineReader, MaskingCallback maskingCallback) {
        this(commandManager, prettyPrinterManager, lineReader, maskingCallback, new ConsoleOptions());
    }

    public Console(CommandManager commandManager, PrettyPrinterManager prettyPrinterManager, LineReader lineReader, MaskingCallback maskingCallback, ConsoleOptions consoleOptions) {
        this.lineReader = lineReader;
        this.maskingCallback = new DynamicMaskingCallback(this.passwordInput, maskingCallback);
        this.consoleOptions = consoleOptions;
        this.commandManager = commandManager;
        this.prettyPrinterManager = prettyPrinterManager;

        HashMap<Object, Object> system = new HashMap<>();
        this.env.put("system", system);

        system.put("env", new HashMap<>(System.getenv()));

        HashMap<String, Object> properties = new HashMap<>();
        for (String key : System.getProperties().stringPropertyNames()) {
            properties.put(key, System.getProperty(key));
        }
        system.put("properties", properties);
    }


    public ConsoleOptions getOptions() {
        return consoleOptions;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commandManager.getCommands());
    }

    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(commandManager.getCommands().get(name));
    }

    public Map<String, Object> getEnv() {
        return env;
    }

    public String readPassword() {
        this.passwordInput.set(true);
        String read = read();
        this.passwordInput.set(false);
        return read;
    }

    public String readLine() {
        return read();
    }

    public ParsedLine readParsedLine() {
        return this.lineReader.getParser().parse(read(), 0);
    }

    private String read() {
        if (!this.passwordInput.get()) {
            this.line++;
        }
        String line = null;
        try {
            line = this.lineReader.readLine(prompt(), rigthPrompt(), this.maskingCallback, buffer());
        } catch (UserInterruptException e) {
            LOGGER.debug("User interruption! Exiting!");
        } catch (EndOfFileException e) {
            LOGGER.debug("Nothing more to read! Exiting!");
            // Force system exit
            getCommand("exit").ifPresent(it -> it.invoke((String) null, this));
        }
        return line;
    }

    private String rigthPrompt() {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.background(AttributedStyle.CYAN).foreground(AttributedStyle.BLACK))
                .append("[" + LocalDateTime.now() + "]")
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    private String prompt() {
        return "[" + (this.line) + "] " + this.lineReader.getTerminal().getName() + " $ ";
    }

    private String buffer() {
        return null;
    }

    private Optional execute(final ParsedLine parsedLine) {
        if (!this.passwordInput.get()) {
            this.lineReader.getHistory().add(parsedLine.line());
            Optional<Command> command = getCommand(parsedLine.word());
            if (!command.isPresent()) {
                this.getLineReader().getTerminal().writer().println("Command " + parsedLine.word() + " not found!");
            }
            return command.flatMap(
                    it -> it.invoke(parsedLine, this)
            );
        }
        return Optional.empty();
    }

    @Override
    public void run() {
        boolean running = true;
        PrintWriter writer = lineReader.getTerminal().writer();
        while (running) {
            try {
                ParsedLine parsedLine = this.readParsedLine();
                if (!parsedLine.word().trim().isEmpty()) {
                    this.execute(parsedLine).ifPresent(
                            it -> prettyPrinterManager.prettyPrint(0, PrettyPrinter.DEFAULT_SPACER, writer, it)
                    );
                }
            } catch (UserInterruptException ex) {
                running = false;
            }
        }
    }
}
