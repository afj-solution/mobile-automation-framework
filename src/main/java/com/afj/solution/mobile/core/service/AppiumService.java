package com.afj.solution.mobile.core.service;

/**
 * @author Tomash Gombosh
 */
public interface AppiumService {

    void startServer();

    void stopServer();

    void forceStop();

    boolean checkIfServerIsRunning(int port);
}
