package com.itquasar.multiverse.proton.util;

import com.itquasar.multiverse.proton.Console;
import lombok.Getter;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class PromptVariables extends AttributedStyle {

    @Getter
    private final Console console;

    public PromptVariables(Console console) {
        this.console = console;
    }

    public LocalDateTime getDatetime() {
        return LocalDateTime.now();
    }

    public Console getConsole() {
        return console;
    }

    /**
     * @return Current Working Directory (user.dir property)
     */
    public Path getCwd() {
        return Paths.get(System.getProperty("user.dir"));
    }

    public String getUser() {
        return System.getProperty("user.name");
    }

    public String bg(int color, String text) {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.background(color))
                .append(text)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    public String fg(int color, String text) {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(color))
                .append(text)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    public String bgfg(int colorBg, int colorFg, String text) {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.background(colorBg).foreground(colorFg))
                .append(text)
                .style(AttributedStyle.DEFAULT)
                .toAnsi();
    }

    Object propertyMissing(Object name) {
        System.out.println("propertyMissing ==============================================");
        return null;
    }

}
