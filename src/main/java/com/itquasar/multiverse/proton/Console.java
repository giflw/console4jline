package com.itquasar.multiverse.proton;

import com.itquasar.multiverse.proton.util.PromptVariables;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
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

    private final ConfigObject config;

    private int line = 0;

    public Console(URL configUrl, CommandManager commandManager, PrettyPrinterManager prettyPrinterManager, LineReader lineReader, MaskingCallback maskingCallback) {
        this(configUrl, commandManager, prettyPrinterManager, lineReader, maskingCallback, new ConsoleOptions());
    }

    public Console(URL configUrl, CommandManager commandManager, PrettyPrinterManager prettyPrinterManager, LineReader lineReader, MaskingCallback maskingCallback, ConsoleOptions consoleOptions) {
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

        // MUST BE LAST, TO HAVE VARIABLES ACCESSED
        ConfigSlurper configSlurper = new ConfigSlurper();
        {
            Console console = this;
            configSlurper.setBinding(new HashMap() {{
                put("_", new PromptVariables(console));
            }});
        }
        this.config = configSlurper.parse(configUrl);
    }

    public ConfigObject getConfig() {
        return config;
    }

    public ConsoleOptions getOptions() {
        return consoleOptions;
    }

    public LineReader getLineReader() {
        return this.lineReader;
    }

    public Terminal getTerminal() {
        return this.lineReader.getTerminal();
    }

    public Optional<Command> getCommand(String name) {
        CommandLine commandLine = commandManager.getCommandLine().getSubcommands().get(name);
        return commandLine != null
                ? Optional.of((Command) commandLine.getCommand())
                : Optional.empty();
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
            ConfigObject prompt = (ConfigObject) config.get("prompt");
            line = this.lineReader.readLine(
                    prompt.get("left").toString(),
                    prompt.get("right").toString(),
                    this.maskingCallback,
                    buffer()
            );
        } catch (UserInterruptException e) {
            LOGGER.debug("User interruption! Exiting!");
        } catch (EndOfFileException e) {
            LOGGER.debug("Nothing more to read! Exiting!");
            // Force system exit
            getCommand("exit").ifPresent(it -> it.invoke(null, this, InterCommunication.ok()));
        }
        return line;
    }

    private String buffer() {
        return null;
    }

    private InterCommunication execute(final ParsedLine parsedLine) {
        if (!this.passwordInput.get()) {
            this.lineReader.getHistory().add(parsedLine.line());

            List<String> words = parsedLine.words();

            List<Integer> pipes = new LinkedList<>();
            pipes.add(-1);

            for (int i = 0; i < words.size(); i++) {
                if ("|".equals(words.get(i))) {
                    pipes.add(i);
                }
            }

            List<String[]> commands = new LinkedList<>();

            for (int i = 0; i < pipes.size(); i++) {
                int fromIndex = pipes.get(i) + 1;
                int toIndex = i >= pipes.size() - 1 ? words.size() : pipes.get(i + 1);
                List<String> subList = words.subList(fromIndex, toIndex);
                LOGGER.debug("Spliting command line from {} to {}: {}", fromIndex, toIndex, subList);
                commands.add(subList.toArray(new String[0]));
            }

            LOGGER.trace("Commands: {}", commands);

            InterCommunication previousOutput = InterCommunication.ok();
            for (String[] commandLine : commands) {
                CommandLine.ParseResult parseResult = commandManager.getCommandLine().parseArgs(commandLine);
                if (parseResult.hasSubcommand()) {
                    CommandLine subCommandLine = parseResult.asCommandLineList().get(1);
                    Command command = subCommandLine.getCommand();
                    previousOutput = command.invoke(subCommandLine, this, previousOutput);
                }
            }

            return previousOutput;
        }
        return InterCommunication.ok();
    }

    @Override
    public void run() {
        boolean running = true;
        PrintWriter writer = lineReader.getTerminal().writer();
        while (running) {
            try {
                ParsedLine parsedLine = this.readParsedLine();
                if (!parsedLine.word().trim().isEmpty()) {
                    // TODO refactor
                    this.execute(parsedLine).visit(
                            it ->
                                    ((InterCommunication) it).getResult().ifPresent(
                                            obj ->
                                                    prettyPrinterManager.prettyPrint(0, PrettyPrinter.DEFAULT_SPACER, writer, obj)
                                    )
                    );
                }
            } catch (UserInterruptException ex) {
                running = false;
            }
        }
    }
}
