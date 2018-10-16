package com.itquasar.multiverse.proton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class PrettyPrinterManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrettyPrinterManager.class);

    private final ServiceLoader<PrettyPrinter> loader = ServiceLoader.load(PrettyPrinter.class);

    private final String packageToLoad;

    private final PrettyPrinter rootPrettyPrinter = new PrettyPrinter<Object>() {
    };

    private Map<Class, PrettyPrinter> prettyPrinterMap;

    public PrettyPrinterManager() {
        this("");
    }

    public PrettyPrinterManager(String packageToLoad) {
        this.packageToLoad = packageToLoad;
        this.getPrettyPrinters(true);
    }

    public void prettyPrint(int level, String spacer, PrintWriter printWriter, Object object) {
        this.rootPrettyPrinter.prettyPrint(level, spacer, printWriter, object, this);
    }

    public Map<Class, PrettyPrinter> getPrettyPrinters() {
        return getPrettyPrinters(false);
    }

    public Map<Class, PrettyPrinter> getPrettyPrinters(boolean refresh) {
        if (refresh) {
            if (!packageToLoad.isEmpty()) {
                LOGGER.warn("Loading pretty printers from package {}", packageToLoad);
            }
            Map<Class, PrettyPrinter> prettyPrinterMap = new HashMap<>();
            Iterator<PrettyPrinter> iterator = loader.iterator();
            while (iterator.hasNext()) {
                PrettyPrinter prettyPrinter = iterator.next();
                if (packageToLoad.isEmpty() || prettyPrinter.getClass().getPackage().getName().equals(packageToLoad)) {
                    LOGGER.debug(
                            "Loading pretty printer for {} with priority {} [{}]",
                            prettyPrinter.getSupportedType(), prettyPrinter.getPriority(), prettyPrinter.getClass().getCanonicalName()
                    );
                    PrettyPrinter registeredPrettyPrinter = prettyPrinterMap.get(prettyPrinter.getSupportedType());
                    if (registeredPrettyPrinter == null) {
                        LOGGER.info("Registering pretty printer for {} [{}]", prettyPrinter.getSupportedType(), prettyPrinter.getClass().getCanonicalName());
                        prettyPrinterMap.put(prettyPrinter.getSupportedType(), prettyPrinter);
                    } else if (registeredPrettyPrinter.getPriority() < prettyPrinter.getPriority()) {
                        LOGGER.info(
                                "Replacing pretty printer for {} [{} --> {}]",
                                prettyPrinter.getSupportedType(), registeredPrettyPrinter.getClass().getCanonicalName(), prettyPrinter.getClass().getCanonicalName()
                        );
                        prettyPrinterMap.put(prettyPrinter.getSupportedType(), prettyPrinter);
                    } else {
                        LOGGER.info(
                                "Skipping registering pretty printer for {} [{}]: low priority ({})",
                                prettyPrinter.getSupportedType(), prettyPrinter.getClass().getCanonicalName(), prettyPrinter.getPriority()
                        );
                    }
                } else {
                    LOGGER.warn("Skipping pretty print for {} [{}]", prettyPrinter.getSupportedType(), prettyPrinter.getClass().getCanonicalName());
                }
            }
            this.prettyPrinterMap = prettyPrinterMap;
        }
        return prettyPrinterMap;
    }

}
