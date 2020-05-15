package com.afj.solution.mobile.core.config;

import com.afj.solution.mobile.core.annotation.Config;

/**
 * @author Tomash Gombosh
 */
@Config("Android")
class AndroidConfig extends OsConfig {
    AndroidConfig(final String propertiesFile) {
        super(propertiesFile);
    }
}
