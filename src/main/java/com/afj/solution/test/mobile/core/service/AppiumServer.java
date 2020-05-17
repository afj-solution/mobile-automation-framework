package com.afj.solution.test.mobile.core.service;

import java.io.IOException;
import java.net.ServerSocket;

import com.afj.solution.test.mobile.core.config.ApplicationConfig;
import com.afj.solution.test.mobile.core.constants.Utils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class AppiumServer implements AppiumService {
    private final AppiumDriverLocalService service;
    private final ApplicationConfig applicationConfig;

    public AppiumServer(final ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.service = buildService();
    }

    @Override
    public void startServer() {
        if (checkIfServerIsRunning(Integer.parseInt(applicationConfig.getAppiumPort()))) {
            service.stop();
        }
        service.start();
        log.info(String.format("Start appium service on the %s", applicationConfig.getAppiumUrl()));
    }

    @Override
    public void stopServer() {
        service.stop();
        Utils.sleep(1000);
        log.info(String.format("Stop appium service on the %s", applicationConfig.getAppiumUrl()));
    }

    @Override
    public void forceStop() {
        final String command = Utils.getOs().contains("Win") ? "taskkill /F /IM node.exe" : "killall node";
        stopServer(command);
        log.info(String.format("Stop appium service on the %s", applicationConfig.getAppiumUrl()));
    }

    @Override
    public boolean checkIfServerIsRunning(final int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            log.info("Port in use now");
            isServerRunning = true;
        }
        return isServerRunning;
    }

    private AppiumDriverLocalService buildService() {
        final AppiumServiceBuilder builder = new AppiumServiceBuilder();
        final DesiredCapabilities cap = new DesiredCapabilities();
        builder.withIPAddress(applicationConfig.getAppiumIp());
        builder.usingPort(Integer.parseInt(applicationConfig.getAppiumPort()));
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        return AppiumDriverLocalService.buildService(builder);
    }

    private void stopServer(final String command) {
        final Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            log.info("Exception log", e.fillInStackTrace());
        }
    }
}
