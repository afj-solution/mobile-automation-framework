package com.afj.solution.test.mobile.core.service.device;

/**
 * @author Tomash Gombosh
 */
public interface DeviceService {
    void closeAndLaunchApp();

    void resetApp();

    void uninstallAndReinstallApp();

    void removeApp();

    void terminateAppAndLaunch();

    void goToBackGround();

    void goToBackGround(int second);

    void activeApp();

    void closeApp();
}
