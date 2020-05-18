package com.afj.solution.test.mobile.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.log4j.Log4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.afj.solution.test.mobile.core.BaseTest;
import com.afj.solution.test.mobile.core.enums.DeviceOs;
import com.afj.solution.test.mobile.core.enums.DeviceType;
import com.afj.solution.test.mobile.core.exception.NoValueFromConfigException;

/**
 * @author Tomash Gombosh
 */
@DisplayName("Config package tests")
@Log4j
public class ConfigTest extends BaseTest {
    private final String androidTestFilePropertiesPath = String.format("%s%s", RESOURCE_DIR, "androidTestProperties.properties");
    private final String iosTestFilePropertiesPath = String.format("%s%s", RESOURCE_DIR, "iosTestProperties.properties");
    private final String appTestFilePropertiesPath = String.format("%s%s", RESOURCE_DIR, "appTestProperties.properties");
    private final String defaultTestPropertiesPath = String.format("%s%s", RESOURCE_DIR, "defaultTest.properties");
    private final Properties androidTestFileProperties = new Properties();
    private final Properties iosTestFileProperties = new Properties();
    private final Properties applicationTestFileProperties = new Properties();
    private final Properties defaultTestFileProperties = new Properties();

    @BeforeEach
    public void setupData() throws IOException {
        final InputStream androidTestFile = new FileInputStream(androidTestFilePropertiesPath);
        final InputStream iosTestFile = new FileInputStream(iosTestFilePropertiesPath);
        final InputStream applicationTestFile = new FileInputStream(appTestFilePropertiesPath);
        final InputStream defaultTestFile = new FileInputStream(defaultTestPropertiesPath);

        androidTestFileProperties.load(androidTestFile);
        iosTestFileProperties.load(iosTestFile);
        applicationTestFileProperties.load(applicationTestFile);
        defaultTestFileProperties.load(defaultTestFile);

        androidTestFile.close();
        iosTestFile.close();
        applicationTestFile.close();
        defaultTestFile.close();
    }

    @Test
    public void iosPropertiesCheck() {
        final ApplicationConfig iosConfig = new ApplicationConfig(DeviceOs.IOS,
            appTestFilePropertiesPath,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(iosConfig.getPlatformName()).isEqualTo(DeviceOs.IOS);
            softAssertions.assertThat(iosConfig.getPlatformVersion())
                .isEqualTo(iosTestFileProperties.getProperty("platform.version"));
            softAssertions.assertThat(iosConfig.getDeviceName())
                .isEqualTo(iosTestFileProperties.getProperty("device.name"));
            softAssertions.assertThat(iosConfig.getUdid())
                .isEqualTo(iosTestFileProperties.getProperty("device.udid"));
            softAssertions.assertThat(iosConfig.getAppPath())
                .isEqualTo(iosTestFileProperties.getProperty("app.path"));
            softAssertions.assertThat(iosConfig.getAppPathOld())
                .isEqualTo(iosTestFileProperties.getProperty("app.path.old"));
            softAssertions.assertThat(iosConfig.getBaseUser())
                .isEqualTo(iosTestFileProperties.getProperty("base.user.username"));
            softAssertions.assertThat(iosConfig.getBaseUserPassword())
                .isEqualTo(iosTestFileProperties.getProperty("base.user.password"));
        });
    }

    @Test
    public void androidPropertiesCheck() {
        final ApplicationConfig androidProperties = new ApplicationConfig(DeviceOs.ANDROID,
            appTestFilePropertiesPath,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(androidProperties.getPlatformName())
                .isEqualTo(DeviceOs.ANDROID);
            softAssertions.assertThat(androidProperties.getPlatformVersion())
                .isEqualTo(androidTestFileProperties.getProperty("platform.version"));
            softAssertions.assertThat(androidProperties.getDeviceName())
                .isEqualTo(androidTestFileProperties.getProperty("device.name"));
            softAssertions.assertThat(androidProperties.getUdid())
                .isEqualTo(androidTestFileProperties.getProperty("device.udid"));
            softAssertions.assertThat(androidProperties.getAppPath())
                .isEqualTo(androidTestFileProperties.getProperty("app.path"));
            softAssertions.assertThat(androidProperties.getAppPathOld())
                .isEqualTo(androidTestFileProperties.getProperty("app.path.old"));
            softAssertions.assertThat(androidProperties.getBaseUser())
                .isEqualTo(androidTestFileProperties.getProperty("base.user.username"));
            softAssertions.assertThat(androidProperties.getBaseUserPassword())
                .isEqualTo(androidTestFileProperties.getProperty("base.user.password"));
        });
    }

    @Test
    public void basePropertiesCheck() {
        final ApplicationConfig applicationConfig = new ApplicationConfig(DeviceOs.ANDROID,
            appTestFilePropertiesPath,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(applicationConfig.getDeviceType())
                .isEqualTo(DeviceType.valueOf(applicationTestFileProperties.getProperty("device.type").toUpperCase()));
            softAssertions.assertThat(applicationConfig.getTimeout())
                .isEqualTo(Integer.parseInt(applicationTestFileProperties.getProperty("timeout")));
            softAssertions.assertThat(applicationConfig.getAppiumIp())
                .isEqualTo(applicationTestFileProperties.getProperty("appium.ip"));
            softAssertions.assertThat(applicationConfig.getAppiumPort())
                .isEqualTo(applicationTestFileProperties.getProperty("appium.port"));
        });
    }

    @Test
    public void checkNullProperties() {
        final String nullTestFilePropertiesPath = null;
        assertThatThrownBy(() -> new ApplicationConfig(DeviceOs.ANDROID,
            nullTestFilePropertiesPath,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Application properties file can not be null");

    }

    @Test
    public void checkOsExceptionProperties() {
        final String exception = "exception.properties";
        assertThatThrownBy(() -> new ApplicationConfig(DeviceOs.valueOf("ADDDRIOD"),
            exception,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void checkValueExceptionProperties() {
        final String nullTestFilePropertiesPath = String.format("%s%s", RESOURCE_DIR, "exception.properties");
        assertThatThrownBy(() -> new ApplicationConfig(DeviceOs.ANDROID,
            appTestFilePropertiesPath,
            nullTestFilePropertiesPath,
            iosTestFilePropertiesPath))
            .isInstanceOf(NoValueFromConfigException.class);
    }

    @Test
    public void defaultPropertiesCheck() {
        final ApplicationConfig applicationConfig = new ApplicationConfig(DeviceOs.ANDROID,
            defaultTestPropertiesPath,
            androidTestFilePropertiesPath,
            iosTestFilePropertiesPath);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(applicationConfig.getDeviceType())
                .isEqualTo(DeviceType.REAL);
            softAssertions.assertThat(defaultTestFileProperties.getProperty("device.type"))
                .isEqualTo("");
            softAssertions.assertThat(applicationConfig.getTimeout())
                .isEqualTo(30);
            softAssertions.assertThat(defaultTestFileProperties.getProperty("timeout"))
                .isEqualTo("");
            softAssertions.assertThat(applicationConfig.getAppiumIp())
                .isEqualTo("0.0.0.0");
            softAssertions.assertThat(defaultTestFileProperties.getProperty("appium.ip"))
                .isEqualTo("");
            softAssertions.assertThat(applicationConfig.getAppiumPort())
                .isEqualTo("4723");
            softAssertions.assertThat(defaultTestFileProperties.getProperty("appium.port"))
                .isEqualTo("");
        });
    }
}
