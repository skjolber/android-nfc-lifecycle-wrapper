package com.github.skjolber.nfc.lifecycle.utility;

public class NfcSwitch {

    public void setMode(Mode mode) {

    }

    public enum Mode {
        CAPTURE_AND_FORWARDING,
        NOT_CAPTURING,
        CAPTURE_AND_NOT_FORWARDING
    }

    public void setForward(boolean forward) {

    }
}
