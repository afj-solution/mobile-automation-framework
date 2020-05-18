package com.afj.solution.test.mobile.core;

import java.io.File;

import org.assertj.core.api.WithAssertions;

public class BaseTest implements WithAssertions {
    protected final static String RESOURCE_DIR = System.getProperty("user.dir") + File.separator
        + "src" + File.separator  + "test"+ File.separator  + "resources"
        + File.separator;
}
