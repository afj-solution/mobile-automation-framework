package com.afj.solution.test.mobile.core.config;

import lombok.extern.log4j.Log4j;

import com.afj.solution.test.mobile.core.annotation.Config;

/**
 * @author Tomash Gombosh
 */
@Config("Android")
@Log4j
class AndroidConfig extends OsConfig {
    AndroidConfig(final String propertiesFile) {
        super(propertiesFile);
    }
}
