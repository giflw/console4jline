package com.itquasar.multiverse.proton.printers;

import com.itquasar.multiverse.proton.PrettyPrinter;
import com.itquasar.multiverse.proton.PrettyPrinterManager;

import java.io.PrintWriter;
import java.util.List;

public class ListPrettyPrinter extends PrettyPrinter<List> {

    @Override
    public void prettyPrint(int level, String spacer, PrintWriter printWriter, List object, PrettyPrinterManager manager) {
        for (Object item : object) {
            manager.prettyPrint(level + 1, spacer, printWriter, item);
        }
    }
}
