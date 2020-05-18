package com.afj.solution.test.mobile.core;

import java.io.Serializable;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

/**
 * @author Tomash Gombosh
 */
public abstract class IosBy extends By implements Serializable {

    private static final long serialVersionUID = -2991949292460596394L;

    public static IosBy xpath(final String xpath) {
        return (IosBy) By.xpath(xpath);
    }

    public static IosBy id(final String id) {
        return (IosBy) By.id(id);
    }

    public static IosBy name(final String name) {
        return (IosBy) By.name(name);
    }

    public static IosBy accessibilityId(final String accessibilityId) {
        return (IosBy) MobileBy.AccessibilityId(accessibilityId);
    }

    public static IosBy iosPredicateString(final String iosPredicateString) {
        return (IosBy) MobileBy.iOSNsPredicateString(iosPredicateString);
    }

    public static IosBy iosClassChain(final String iosClassChain) {
        return (IosBy) MobileBy.iOSClassChain(iosClassChain);
    }

}
