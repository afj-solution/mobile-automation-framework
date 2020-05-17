package com.afj.solution.test.mobile.core.enums;

/**
 * @author Tomash Gombosh
 */
public enum DeviceType {
    SIMULATOR("Simulator"), REAL("real");

    private final String deviceType;

    DeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
