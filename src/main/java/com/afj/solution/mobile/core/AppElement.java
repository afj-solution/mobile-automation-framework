package com.afj.solution.mobile.core;

import java.util.function.Consumer;

import com.afj.solution.mobile.core.enums.DeviceOs;
import com.afj.solution.mobile.core.enums.ScrollTo;
import com.afj.solution.mobile.core.exceptions.NoSuchOsException;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileSelector;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openqa.selenium.By;

import static java.util.Objects.requireNonNull;

/**
 * @author Tomash Gombosh
 */
@Data
@AllArgsConstructor
public class AppElement {
    private String name;
    private AndroidBy androidLocator;
    private IosBy iosLocator;
    private ScrollTo scrollTo;
    private boolean required;

    public AppElement(final Consumer<AppElement> builder) {
        requireNonNull(builder).accept(this);
    }

    public AppElement(final String name, final AndroidBy androidLocator, final ScrollTo scrollTo, final boolean required) {
        this(name, androidLocator, null, scrollTo, required);
    }

    public AppElement(final String name, final IosBy iosBy, final ScrollTo scrollTo, final boolean required) {
        this(name, null, iosBy, scrollTo, required);
    }

    public AppElement(final String name,  final AndroidBy androidLocator, final IosBy iosBy, final ScrollTo scrollTo) {
        this(name, androidLocator, iosBy, scrollTo, false);
    }

    public AppElement(final String name,  final AndroidBy androidLocator, final IosBy iosBy) {
        this(name, androidLocator, iosBy, ScrollTo.NO, false);
    }

    /**
     * Gets the appropriate locator.
     *
     * @param deviceType the type of device, ANDROID or IOS.
     * @return the appropriate By locator of this element.
     */
    public By get(final DeviceOs deviceType) {
        By loc;
        if (deviceType.equals(DeviceOs.ANDROID)) {
            loc = this.androidLocator;
        } else if (deviceType.equals(DeviceOs.IOS)) {
            loc = this.iosLocator;
        } else {
            throw new NoSuchOsException("OS of the device should be Android or iOS, you use another one");
        }
        return loc;
    }

    public static class IosBy extends MobileBy {
        protected IosBy(final MobileSelector selector, final String locatorString) {
            super(selector, locatorString);
        }
    }

    public static class AndroidBy extends MobileBy {
        protected AndroidBy(MobileSelector selector, String locatorString) {
            super(selector, locatorString);
        }
    }

}
