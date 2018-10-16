package com.itquasar.multiverse.proton.printers;

import com.itquasar.multiverse.proton.PrettyPrinter;
import com.itquasar.multiverse.proton.PrettyPrinterManager;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class MapPrettyPrinter extends PrettyPrinter<Map> {

    @Override
    public void prettyPrint(int level, String spacer, PrintWriter printWriter, Map object, PrettyPrinterManager manager) {
        for (Map.Entry entry : (Set<Map.Entry>) object.entrySet()) {
            manager.prettyPrint(level + 1, spacer, printWriter, entry.getKey());
            manager.prettyPrint(level + 2, spacer, printWriter, entry.getValue());
        }
    }
}
