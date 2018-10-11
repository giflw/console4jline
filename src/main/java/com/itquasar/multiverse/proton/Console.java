package com.itquasar.multiverse.proton;

import org.jline.reader.*;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class Console {

    private final AtomicBoolean passwordInput = new AtomicBoolean(false);

    private final LineReader lineReader;
    private final MaskingCallback maskingCallback;

    private final ConsoleOptions consoleOptions;

    private int line = 0;

    public Console(LineReader lineReader, MaskingCallback maskingCallback) {
        this(lineReader, maskingCallback, new ConsoleOptions());
    }

    public Console(LineReader lineReader, MaskingCallback maskingCallback, ConsoleOptions consoleOptions) {
        this.lineReader = lineReader;
        this.maskingCallback = new DynamicMaskingCallback(this.passwordInput, maskingCallback);
        this.consoleOptions = consoleOptions;
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
            intercept(line);
        } catch (UserInterruptException e) {
            e.printStackTrace();
        } catch (EndOfFileException e) {
            e.printStackTrace();
            // Force system exit
            DefaultCommand.EXIT.getFunction().accept(this.lineReader, new ConsoleOptions());
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

    private String intercept(String line) {
        if (!this.passwordInput.get()) {
            this.lineReader.getHistory().add(line);
            line = line.toLowerCase().trim();
            DefaultCommand.invoke(line, this.lineReader, this.consoleOptions);
        }
        return line;
    }
}
