package com.afj.solution.mobile.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.afj.solution.mobile.core.exceptions.NoValueFromConfigException;
import lombok.extern.log4j.Log4j;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class OsConfig {
    private final Properties properties;
    private final String propertiesFile;

    protected OsConfig(final String propertiesFile) {
        this.propertiesFile = propertiesFile;
        log.info(propertiesFile);
        this.properties = new Properties();
        this.loadFile();
    }

    private void loadFile() {
        final InputStream fileInput = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        try {
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            log.info(String.format("Exception log %s", e.getMessage()));
        }
    }

    String getUserName() {
        if (!properties.getProperty("base.user.username").equals("")) {
            return properties.getProperty("base.user.username");
        } else {
            throw new NoValueFromConfigException(String.format("base.user.email is required value in the %s", propertiesFile));
        }
    }

    String getPassword() {
        if (!properties.getProperty("base.user.password").equals("")) {
            return properties.getProperty("base.user.password");
        } else {
            throw new NoValueFromConfigException(String.format("base.user.password is required value in the %s", propertiesFile));
        }
    }

    String getDeviceName() {
        if (!properties.getProperty("device.name").equals("")) {
            return properties.getProperty("device.name");
        } else {
            throw new NoValueFromConfigException(String.format("device.name is required value in the %s", propertiesFile));
        }
    }

    String getDeviceUdid() {
        if (!properties.getProperty("device.udid").equals("")) {
            return properties.getProperty("device.udid");
        } else {
            throw new NoValueFromConfigException(String.format("device.udid is required value in the %s", propertiesFile));
        }
    }

    String getAppPath() {
        if (!properties.getProperty("app.path").equals("")) {
            return properties.getProperty("app.path");
        } else {
            throw new NoValueFromConfigException(String.format("app.path is required value in the %s", propertiesFile));
        }
    }

    String getAppPathOld() {
        if (!properties.getProperty("app.path.old").equals("")) {
            return properties.getProperty("app.path.old");
        } else {
            throw new NoValueFromConfigException(String.format("app.path is required value in the %s", propertiesFile));
        }
    }

    String getPlatformVersion() {
        if (!properties.getProperty("platform.version").equals("")) {
            return properties.getProperty("platform.version");
        } else {
            throw new NoValueFromConfigException(String.format("platform.version is required value in the %s", propertiesFile));
        }
    }

    String getStartActivity() {
        if (!properties.getProperty("start.activity").equals("")) {
            return properties.getProperty("start.activity");
        } else {
            throw new NoValueFromConfigException(String.format("start.activity is required value in the %s", propertiesFile));
        }
    }

    String getAppPackage() {
        if (!properties.getProperty("app.package").equals("")) {
            return properties.getProperty("app.package");
        } else {
            throw new NoValueFromConfigException(String.format("app.package is required value in the %s", propertiesFile));
        }
    }
}
