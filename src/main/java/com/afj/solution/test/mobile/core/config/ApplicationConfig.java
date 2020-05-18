package com.afj.solution.test.mobile.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import com.afj.solution.test.mobile.core.annotation.Config;
import com.afj.solution.test.mobile.core.enums.DeviceOs;
import com.afj.solution.test.mobile.core.enums.DeviceType;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author Tomash Gombosh
 */
@Getter
@Log4j
@Config("Application")
public class ApplicationConfig {
    private String baseUser;
    private String baseUserPassword;
    private String environment;
    private String appiumIp;
    private String appiumPort;
    private String deviceName;
    private String udid;
    private DeviceType deviceType;
    private String appPath;
    private String appPathOld;
    private String packageName;
    private String platformVersion;
    private String startActivity;
    private int timeout;
    private final DeviceOs platformName;
    private final String properties;

    protected ApplicationConfig(final DeviceOs platform,
                                final String appProperties,
                                final String androidProperties,
                                final String iosProperties) {
        requireNonNull(platform, "Platform can not be null");
        requireNonNull(appProperties, "Application properties file can not be null");
        requireNonNull(androidProperties, "Android properties properties file can not be null");
        requireNonNull(iosProperties, "iOS properties file can not be null");

        this.platformName = platform;
        this.properties = appProperties;
        initGlobalProperties();
        if (platformName.equals(DeviceOs.ANDROID)) {
            initAndroid(new AndroidConfig(androidProperties));
        } else {
            initIos(new IosConfig(iosProperties));
        }
    }

    public String getAppiumUrl() {
        return "http://" + getAppiumIp() + ":" + getAppiumPort() + "/wd/hub";
    }

    private void initGlobalProperties() {
        try {
            final InputStream fileInput = nonNull(getClass().getClassLoader().getResourceAsStream(properties))
                ? getClass().getClassLoader().getResourceAsStream(properties)
                : new FileInputStream(properties);

            final Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            this.deviceType = !properties.getProperty("device.type").equals("")
                    ? DeviceType.valueOf(properties.getProperty("device.type").toUpperCase()) : DeviceType.REAL;
            this.appiumIp = !properties.getProperty("appium.ip").equals("") ? properties.getProperty("appium.ip") : "0.0.0.0";
            this.appiumPort = !properties.getProperty("appium.port").equals("") ? properties.getProperty("appium.port") : "4723";
            this.timeout = !properties.getProperty("timeout").equals("") ? Integer.parseInt(properties.getProperty("timeout")) : 30;
            this.environment = !properties.getProperty("environment").equals("")
                    ? properties.getProperty("environment")
                    : "https://www/nimda/";
        } catch (IOException e) {
            log.info(String.format("Exception %s", e.getMessage()));
        }
    }

    private ApplicationConfig initAndroid(final AndroidConfig androidConfig) {
        this.baseUser = androidConfig.getUserName();
        this.baseUserPassword = androidConfig.getPassword();
        this.deviceName = androidConfig.getDeviceName();
        this.appPath = androidConfig.getAppPath();
        this.appPathOld = androidConfig.getAppPathOld();
        this.packageName = androidConfig.getAppPackage();
        this.platformVersion = androidConfig.getPlatformVersion();
        this.startActivity = androidConfig.getStartActivity();
        return this;
    }

    private ApplicationConfig initIos(final IosConfig iosConfig) {
        this.baseUser = iosConfig.getUserName();
        this.baseUserPassword = iosConfig.getPassword();
        this.deviceName = iosConfig.getDeviceName();
        this.udid = iosConfig.getDeviceUdid();
        this.appPath = iosConfig.getAppPath();
        this.appPathOld = iosConfig.getAppPathOld();
        this.platformVersion = iosConfig.getPlatformVersion();
        return this;
    }
}
