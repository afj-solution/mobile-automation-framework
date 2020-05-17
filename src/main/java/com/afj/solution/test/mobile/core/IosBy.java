package com.afj.solution.test.mobile.core;

import java.io.Serializable;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

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

    public static IosBy AccessibilityId(final String accessibilityId) {
        return (IosBy) MobileBy.AccessibilityId(accessibilityId);
    }

    public static IosBy iOSNsPredicateString(final String iOSNsPredicateString) {
        return (IosBy) MobileBy.iOSNsPredicateString(iOSNsPredicateString);
    }

    public static IosBy iOSClassChain(final String iOSClassChain) {
        return (IosBy) MobileBy.iOSClassChain(iOSClassChain);
    }

}
