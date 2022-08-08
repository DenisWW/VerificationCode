package com.nineone.verificationcode.utils;

public class RxBus {
    private static RxBus rxBus;

    public RxBus() {
    }

    public static RxBus getRxBus() {
        if (rxBus == null) {
            synchronized (RxBus.class){
                if (rxBus == null) rxBus = new RxBus();
            }
        }
        return rxBus;
    }
}
