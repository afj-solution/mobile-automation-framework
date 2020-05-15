package com.afj.solution.mobile.core.service;

/**
 * @author Tomash Gombosh
 */
public class AndroidId {
    private final String packageName;

    public AndroidId(final String packageName) {
        this.packageName = packageName;
    }

    public String getAndroidId(final String id) {
        return String.format("%s:id/%s", packageName, id);
    }
}
