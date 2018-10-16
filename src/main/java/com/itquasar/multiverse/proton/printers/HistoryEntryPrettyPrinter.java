package com.itquasar.multiverse.proton.printers;

import com.itquasar.multiverse.proton.PrettyPrinter;
import com.itquasar.multiverse.proton.PrettyPrinterManager;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.History.Entry;

import java.io.PrintWriter;

public class HistoryEntryPrettyPrinter extends PrettyPrinter<Entry> {

    @Override
    public void prettyPrint(int level, String spacer, PrintWriter printWriter, Entry object, PrettyPrinterManager manager) {
        printWriter.println(
                StringUtils.leftPad(
                        String.valueOf(object.index()), 4
                ) + ": [" + object.time() + "] " + object.line()
        );
    }
}
