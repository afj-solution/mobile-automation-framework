package com.afj.solution.test.mobile.core;

import java.io.Serializable;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

/**
 * @author Tomash Gombosh
 */
public abstract class AndroidBy extends By implements Serializable {
    private static final long serialVersionUID = 8379932225592242556L;

    public static AndroidBy xpath(final String xpath) {
        return (AndroidBy) By.xpath(xpath);
    }

    public static AndroidBy id(final String id) {
        return (AndroidBy) By.id(id);
    }

    public static AndroidBy name(final String name) {
        return (AndroidBy) By.name(name);
    }

    public static AndroidBy accessibilityId(final String accessibilityId) {
        return (AndroidBy) MobileBy.AccessibilityId(accessibilityId);
    }

    public static AndroidBy androidUiAutomator(final String androidUiAutomator) {
        return (AndroidBy) MobileBy.AndroidUIAutomator(androidUiAutomator);
    }
}
