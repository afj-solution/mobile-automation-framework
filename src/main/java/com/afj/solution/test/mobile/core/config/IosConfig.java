package com.afj.solution.test.mobile.core.config;

import lombok.extern.log4j.Log4j;

import com.afj.solution.test.mobile.core.annotation.Config;

/**
 * @author Tomash Gombosh
 */
@Config("iOS")
@Log4j
class IosConfig extends OsConfig {
    IosConfig(final String propertiesFile) {
        super(propertiesFile);
    }
}
