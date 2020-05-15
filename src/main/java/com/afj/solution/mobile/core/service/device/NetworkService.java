package com.afj.solution.mobile.core.service.device;

/**
 * @author Tomash Gombosh
 */
public interface NetworkService {
    boolean ANDROID = false;

    void turnOffAllNetworkConnections();

    void turnOnAllNetworkConnections();

    void turnOnWifiOnly();

    void turnOnBluetoothOnly();

    void turnOffBluetooth();

    void turnOnCellularOnly();

    void turnOnAirplaneMode();
}
