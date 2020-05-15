package com.afj.solution.mobile.core.config;

import com.afj.solution.mobile.core.annotation.Config;

/**
 * @author Tomash Gombosh
 */
@Config("iOS")
class IosConfig extends OsConfig {
    IosConfig(final String propertiesFile) {
        super(propertiesFile);
    }
}
