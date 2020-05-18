package com.afj.solution.test.mobile.core.enums;

/**
 * @author Tomash Gombosh
 */
public enum DeviceOs {
    ANDROID("Android"), IOS("iOS");

    private final String deviceOs;

    DeviceOs(final String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getDeviceOs() {
        return deviceOs;
    }
}
