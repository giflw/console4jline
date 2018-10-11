package com.itquasar.multiverse.proton;

import org.jline.reader.MaskingCallback;

import java.util.concurrent.atomic.AtomicBoolean;

public class DynamicMaskingCallback implements MaskingCallback {

    private final AtomicBoolean maskSwitch;
    private final MaskingCallback maskingCallback;

    public DynamicMaskingCallback(AtomicBoolean maskSwitch, MaskingCallback maskingCallback) {
        this.maskingCallback = maskingCallback;
        this.maskSwitch = maskSwitch;
    }

    @Override
    public String display(String line) {
        return maskSwitch.get() ? maskingCallback.display(line) : line;
    }

    @Override
    public String history(String line) {
        return maskingCallback.history(line);
    }
}
