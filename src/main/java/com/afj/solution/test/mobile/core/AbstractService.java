package com.afj.solution.test.mobile.core;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.afj.solution.test.mobile.core.enums.DeviceOs;
import com.afj.solution.test.mobile.core.enums.ScrollTo;
import com.afj.solution.test.mobile.core.exception.NoSuchAttributeException;
import com.afj.solution.test.mobile.core.service.device.DeviceService;
import com.afj.solution.test.mobile.core.service.device.NetworkService;
import com.afj.solution.test.mobile.core.service.elements.ElementService;
import com.afj.solution.test.mobile.core.service.elements.ScrollService;
import com.afj.solution.test.mobile.core.service.elements.SwipeService;
import com.afj.solution.test.mobile.core.service.elements.WaitService;
import com.google.common.base.Function;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.afj.solution.test.mobile.core.annotation.Ignore;
import com.afj.solution.test.mobile.core.config.ApplicationConfig;
import com.afj.solution.test.mobile.core.enums.DeviceType;
import com.afj.solution.test.mobile.core.exception.NoValueInInputException;
import com.afj.solution.test.mobile.core.service.elements.ActionService;

import static com.afj.solution.test.mobile.core.constants.Utils.sleep;
import static java.util.Objects.requireNonNull;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * @author Tomash Gombosh
 */
@Log4j
public abstract class AbstractService implements ActionService, ElementService, ScrollService, SwipeService, WaitService {
    @Ignore
    private final ApplicationConfig applicationConfig;
    @Ignore
    private final AppiumDriver<MobileElement> driver;
    @Ignore
    private final Dimension winSize;
    @Ignore
    private final TouchAction touchAction;
    @Ignore
    private final WebDriverWait webDriverWait;
    @Ignore
    private final Wait<WebDriver> fluentWait;

    protected AbstractService(final AppiumDriver<MobileElement> driver, final ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.driver = driver;
        this.winSize = this.driver.manage().window().getSize();
        this.touchAction = applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)
                ? new AndroidTouchAction(driver)
                : new IOSTouchAction(driver);
        this.webDriverWait = new WebDriverWait(driver, applicationConfig.getTimeout());
        this.fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(applicationConfig.getTimeout()))
                .pollingEvery(Duration.ofMillis(500))
                .ignoreAll(Arrays.asList(ElementNotVisibleException.class,
                        NoSuchElementException.class,
                        StaleElementReferenceException.class,
                        WebDriverException.class));
    }

    public abstract boolean isDisplayed();

    @Override
    public String getText(final AppElement appElement) {
        return this.find(appElement).getText();
    }

    @Override
    public String getInputValue(final AppElement appElement) {
        try {
            return getInputValue(this.find(appElement));
        } catch (NoSuchElementException e) {
            throw new NoValueInInputException("No value or element", appElement);
        }
    }

    @Override
    public String getInputValue(final WebElement webElement) {
        String value;
        if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
            value = webElement.getText();
        } else {
            value = webElement.getAttribute("value");
        }
        return value;
    }

    @Override
    public void tap(final AppElement appElement, final boolean isLog) {
        if (isLog) {
            log.info("Tapping element '" + appElement.getName());
        }
        this.find(appElement).click();
    }

    @Override
    public void tap(final AppElement appElement) {
        this.tap(appElement, true);
    }

    @Override
    public void tapPercent(final int widthPercent, final int heightPercent) {
        final int widthX = (winSize.width * widthPercent) / 100;
        final int heightY = (winSize.height * heightPercent) / 100;
        this.tapAt(widthX, heightY);
    }

    @Override
    public void tapAt(final int widthX, final int heightY) {
        touchAction.tap(PointOption.point(widthX, heightY));
        this.driver.performTouchAction(touchAction);
        log.info(String.format("Tap at the coordinate %d %d", widthX, heightY));
    }

    @Override
    public void longTap(final AppElement element) {
        touchAction.longPress(ElementOption.element(this.find(element))).release().perform();
    }

    @Override
    public void enterText(final AppElement appElement, final String text) {
        this.enterText(appElement, text, false);
    }

    @Override
    public void enterText(final AppElement appElement, final String text, final boolean clear) {
        log.info("Entering text '" + text + "' to element '" + appElement.getName());
        if (clear) {
            this.tap(appElement, false);
            this.find(appElement).clear();
        }
        this.find(appElement).sendKeys(text);
    }

    @Override
    public WebElement find(final By element) {
        return this.driver.findElement(element);
    }

    @Override
    public WebElement find(final AppElement element) {
        if (!element.getScrollTo().equals(ScrollTo.NO)) {
            try {
                this.scrollToElement(element);
            } catch (NoSuchElementException e) {
                log.info("*** Could not scroll to element: " + element.getName());
                log.info(e);
            }
        }
        return this.find(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public List findAll(final By element) {
        return driver.findElements(element);
    }

    @Override
    public List<AppElement> findAll(final AppElement element) {
        return this.findAll(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public boolean isElementVisible(final By element) {
        try {
            this.waitToBeVisible(element);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementVisible(final AppElement element) {
        return this.isElementVisible(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public boolean isElementPresent(final AppElement element, final int timeout) {
        return isElementPresent(element.get(applicationConfig.getPlatformName()), timeout, element.getName());
    }

    @Override
    public boolean isElementPresent(final By element, final int timeout, final String name) {
        boolean elementFound;
        try {
            this.waitToBePresent(element, timeout);
            log.info(String.format("Element %s present after timeout %s", name, timeout));
            elementFound = true;
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Element not present after timeout " + timeout);
            elementFound = false;
        }
        return elementFound;
    }

    @Override
    public boolean isElementPresent(final By element) {
        boolean elementFound;
        try {
            this.find(element);
            log.info("Element present");
            elementFound = true;
        } catch (NoSuchElementException e) {
            log.info("Element not present");
            elementFound = false;
        }
        return elementFound;
    }

    @Override
    public boolean isElementPresent(final AppElement element) {
        return this.isElementPresent(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public boolean isElementEnabled(final AppElement element) {
        return this.isElementEnabled(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public boolean isElementEnabled(final By element) {
        boolean elementEnabled;
        try {
            this.waitForAttributeValue(element, "enabled", "true");
            log.info(String.format("Element %s is enabled", element.toString()));
            elementEnabled = true;
        } catch (TimeoutException e) {
            log.info(String.format("Element %s is not enabled", element.toString()));
            elementEnabled = false;
        }
        return elementEnabled;
    }

    @Override
    public boolean isAttributePresent(final AppElement appElement, final String attribute) {
        boolean result;
        try {
            find(appElement).getAttribute(attribute);
            result = true;
        } catch (UnsupportedCommandException e) {
            log.info("Element doesnt have such attribute");
            result = false;
        }
        return result;
    }

    @Override
    public String getAttributeValue(final AppElement appElement, final String attributeName) {
        if (isAttributePresent(appElement, attributeName)) {
            return find(appElement).getAttribute(attributeName);
        } else {
            throw new NoSuchAttributeException(attributeName, appElement);
        }
    }

    @Override
    public boolean isAttributeEquals(final AppElement appElement, final String attributeName, final String expectedValue) {
        return expectedValue.equals(getAttributeValue(appElement, attributeName));
    }

    @Override
    public void scrollDownUntilVisible(final AppElement element) {
        if (!this.isElementVisible(element)) {
            log.info("Scrolling DOWN until " + element.getName() + " is visible...");
            this.scrollDownUntilVisible(element.get(applicationConfig.getPlatformName()));
        }
    }

    @Override
    public void scrollUpUntilVisible(final AppElement element) {
        if (!this.isElementVisible(element)) {
            log.info("Scrolling UP until " + element.getName() + " is visible...");
            this.scrollUpUntilVisible(element.get(applicationConfig.getPlatformName()));
        }
    }

    @Override
    public void scrollToElement(final AppElement ele) {
        if (ele.getScrollTo().equals(ScrollTo.UP)) {
            this.scrollUpUntilVisible(ele);
        } else if (ele.getScrollTo().equals(ScrollTo.DOWN)) {
            this.scrollDownUntilVisible(ele);
        }
    }

    @Override
    public void scrollFromToElement(final By startElement, final By endElement) {
        sleep(500);
        log.info("Scrolling from one element to another");
        touchAction.press(ElementOption.element(find(startElement)))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(5)))
                .moveTo(ElementOption.element(find(endElement)))
                .release()
                .perform();
        sleep(1000);
    }

    @Override
    public void scrollFromToElement(final AppElement startElement, final AppElement endElement) {
        this.scrollFromToElement(startElement.get(applicationConfig.getPlatformName()), endElement.get(applicationConfig.getPlatformName()));
    }

    @Override
    public void scrollUpUntilVisible(final By element) {
        this.scrollUntilVisible(ScrollTo.UP, element);
    }

    @Override
    public void scrollDownUntilVisible(final By element) {
        this.scrollUntilVisible(ScrollTo.DOWN, element);
    }

    @Override
    public void scrollUntilVisible(final ScrollTo scrollDirection, final By element) {
        fluentWait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(final WebDriver input) {
                if (scrollDirection.equals(ScrollTo.UP)) {
                    scrollUp();
                } else {
                    scrollDown();
                }
                return input.findElement(element);
            }
        });
    }

    @Override
    public void scrollUp() {
        log.info("Scrolling up");
        this.swipeVerticallyByPercent(10, 60);
    }

    @Override
    public void scrollDown() {
        log.info("Scrolling down");
        this.swipeVerticallyByPercent(60, 10);
    }

    @Override
    public void swipeLeftToRight() {
        log.info("Swipe left to right");
        this.swipeHorizontallyByPercent(10, 90);
    }

    @Override
    public void swipeRightToLeft() {
        log.info("Swipe right to left");
        this.swipeHorizontallyByPercent(90, 10);
    }

    @Override
    public void swipeVerticallyByPercent(final int startPercent, final int endPercent) {
        final int startY = (winSize.height * startPercent) / 100;
        final int endY = (winSize.height * endPercent) / 100;
        final int stepX = (winSize.width * 50) / 100;

        final Point startPoint = new Point(stepX, startY);
        final Point endPoint = new Point(stepX, endY);
        this.swipe(startPoint, endPoint);
    }

    @Override
    public void swipeHorizontallyByPercent(final int startPercent, final int endPercent) {
        final int startX = (winSize.width * startPercent) / 100;
        final int endX = (winSize.width * endPercent) / 100;
        final int stepY = (winSize.height * 50) / 100;

        final Point startPoint = new Point(startX, stepY);
        final Point endPoint = new Point(endX, stepY);
        this.swipe(startPoint, endPoint);
    }

    @Override
    public void swipeByPercent(final int startXPercent,
                               final int endXPercent,
                               final int startYPercent,
                               final int endYPercent) {
        final int startX = (winSize.width * startXPercent) / 100;
        final int endX = (winSize.width * endXPercent) / 100;
        final int startY = (winSize.height * startYPercent) / 100;
        final int endY = (winSize.height * endYPercent) / 100;

        final Point startPoint = new Point(startX, startY);
        final Point endPoint = new Point(endX, endY);
        this.swipe(startPoint, endPoint);
    }

    @Override
    public void swipeToTop(final Point start) {
        final Point endPoint = new Point(start.x, 0);
        swipe(start, endPoint);
    }

    @Override
    public void swipe(final Point start, final Point end) {
        touchAction.longPress(PointOption.point(start))
                .moveTo(PointOption.point(end))
                .release()
                .perform();
    }

    @Override
    public void waitToBeVisible(final By element) {
        fluentWait.until(visibilityOfElementLocated(element));
    }

    @Override
    public void waitToBeVisible(final AppElement element) {
        this.waitToBeVisible(element.get(applicationConfig.getPlatformName()));
    }

    @Override
    public void waitToBeInvisible(final By element, final int timeout) {
        final WebDriverWait webDriverWait = new WebDriverWait(driver, timeout);
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    @Override
    public void waitToBeInvisible(final By element) {
        this.waitToBeInvisible(element, applicationConfig.getTimeout());
    }

    @Override
    public void waitToBeInvisible(final AppElement element, final int timeout) {
        log.info(String.format("Wait to be invisible %s by timeout %s", element.getName(), timeout));
        this.waitToBeInvisible(element.get(applicationConfig.getPlatformName()), timeout);
    }

    @Override
    public void waitToBeInvisible(final AppElement element) {
        this.waitToBeInvisible(element.get(applicationConfig.getPlatformName()), applicationConfig.getTimeout());
    }

    @Override
    public void waitToBeClickable(final By element, final int timeout) {
        final WebDriverWait webDriverWait = new WebDriverWait(driver, timeout);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Override
    public void waitToBeClickable(final By element) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Override
    public void waitToBeClickable(final AppElement element, final int timeout) {
        log.info(String.format("Wait to be clickable %s by timeout %s", element.getName(), timeout));
        this.waitToBeClickable(element.get(applicationConfig.getPlatformName()), timeout);
    }

    @Override
    public void waitToBeClickable(final AppElement element) {
        this.waitToBeClickable(element.get(applicationConfig.getPlatformName()), applicationConfig.getTimeout());
    }

    @Override
    public void waitToBePresent(final By element, final int timeout) {
        webDriverWait.withTimeout(Duration.ofMillis(timeout))
                .until(ExpectedConditions.presenceOfElementLocated(element));
    }

    @Override
    public void waitToBePresent(final By element) {
        this.waitToBePresent(element, applicationConfig.getTimeout());
    }

    @Override
    public void waitToBePresent(final AppElement element, final int timeout) {
        log.info(String.format("Wait to be present %s by timeout %s", element.getName(), timeout));
        this.waitToBePresent(element.get(applicationConfig.getPlatformName()), timeout);
    }

    @Override
    public void waitToBePresent(final AppElement element) {
        this.waitToBePresent(element.get(applicationConfig.getPlatformName()), applicationConfig.getTimeout() * 1000);
    }

    @Override
    public void waitForAttributeValue(final By element, final String attributeName, final String expectedValue) {
        fluentWait.until(ExpectedConditions.attributeToBe(element, attributeName, expectedValue));
    }

    @Override
    public void waitForAttributeValue(final AppElement element, final String attributeName, final String expectedValue) {
        log.info(String.format("Wait to be attribute %s to be %s on element %s", attributeName, expectedValue, element.getName()));
        waitForAttributeValue(element.get(applicationConfig.getPlatformName()), attributeName, expectedValue);
    }

    class Device implements DeviceService {
        private final AppiumDriver<MobileElement> driver = AbstractService.this.driver;
        private final ApplicationConfig applicationConfig = AbstractService.this.applicationConfig;

        @Override
        public void closeAndLaunchApp() {
            this.closeApp();
            log.info("Re-opening application...");
            this.driver.launchApp();
        }

        @Override
        public void resetApp() {
            log.info("Reset  application...");
            this.driver.resetApp();
        }

        @Override
        public void uninstallAndReinstallApp() {
            this.removeApp();
            log.info("Re-installing application...");
            this.driver.installApp(this.applicationConfig.getAppPath());
            this.driver.launchApp();
        }

        @Override
        public void removeApp() {
            log.info("Uninstalling application...");
            if (this.applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                if (driver.isAppInstalled(this.applicationConfig.getPackageName())) {
                    driver.removeApp(this.applicationConfig.getPackageName());
                }
            } else {
                if (driver.isAppInstalled(this.applicationConfig.getPackageName())) {
                    driver.removeApp(this.applicationConfig.getPackageName());
                }
            }
        }

        @Override
        public void terminateAppAndLaunch() {
            log.info("Terminate app...");
            this.driver.terminateApp(this.applicationConfig.getPackageName());
            this.activeApp();
        }

        @Override
        public void goToBackGround() {
            log.info("Go to background to 10 seconds...");
            this.goToBackGroundWithSeconds(10);
        }

        @Override
        public void goToBackGround(int second) {
            log.info(String.format("Go to background to %d seconds...", second));
            this.goToBackGroundWithSeconds(second);
        }

        @Override
        public void activeApp() {
            log.info("Active app...");
            this.driver.activateApp(this.applicationConfig.getPackageName());
            sleep(5000);
        }

        @Override
        public void closeApp() {
            log.info("Closing application...");
            this.driver.closeApp();
        }

        void goToBackGroundWithSeconds(final int seconds) {
            this.driver.runAppInBackground(Duration.ofSeconds(seconds));
        }
    }

    class Network implements NetworkService {
        private AndroidDriver<MobileElement> driverAndroid;
        private IOSDriver<MobileElement> driverIos;
        private final ApplicationConfig applicationConfig = AbstractService.this.applicationConfig;
        private final Dimension winSize = AbstractService.this.winSize;
        public Network() {
            if (applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                this.driverIos = (IOSDriver<MobileElement>) driver;
            } else {
                this.driverAndroid = (AndroidDriver<MobileElement>) driver;
            }
        }
        @Override
        public void turnOffAllNetworkConnections() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            final ConnectionState none = new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK);
            if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                driverAndroid.setConnection(none);
            } else if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(true, false, false);
            }
        }

        @Override
        public void turnOnAllNetworkConnections() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            final ConnectionState wifi = new ConnectionState(ConnectionState.WIFI_MASK);
            final ConnectionState data = new ConnectionState(ConnectionState.DATA_MASK);
            if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                turnOffAllNetworkConnections();
                driverAndroid.setConnection(wifi);
                driverAndroid.setConnection(data);
            } else if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(false, true, true);

            }
        }

        @Override
        public void turnOnWifiOnly() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            final ConnectionState wifi = new ConnectionState(ConnectionState.WIFI_MASK);
            if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                turnOffAllNetworkConnections();
                driverAndroid.setConnection(wifi);
            } else if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(true, true, false);
            }
        }

        @Override
        public void turnOnBluetoothOnly() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(true, false, true);
            }
        }

        @Override
        public void turnOffBluetooth() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(false, true, false);
            }
        }

        @Override
        public void turnOnCellularOnly() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            final ConnectionState data = new ConnectionState(ConnectionState.DATA_MASK);
            if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                turnOffAllNetworkConnections();
                driverAndroid.setConnection(data);
            } else if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(false, false, false);
            }
        }

        @Override
        public void turnOnAirplaneMode() {
            log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
            final ConnectionState airPlane = new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK);
            if (applicationConfig.getPlatformName().equals(DeviceOs.ANDROID)) {
                driverAndroid.setConnection(airPlane);
            } else if (applicationConfig.getDeviceType().equals(DeviceType.SIMULATOR)
                    && applicationConfig.getPlatformName().equals(DeviceOs.IOS)) {
                log.info("Tests are running on iOS simulator, network not available");
            } else {
                this.iosConnection(true, false, false);
            }
        }

        private void iosConnection(final boolean airplane, final boolean wifi, final boolean bluetooth) {
            log.info("airplane: " + airplane + " wifi: " + wifi + " bluetooth: " + bluetooth);
            final int height = this.winSize.height;
            final int width = this.winSize.width;
            final int step = (int) (height * 0.6);

            final String airplaneButtonName = applicationConfig.getPlatformVersion().contains("11")
                    ? "airplane-mode-button"
                    : "Airplane Mode";
            final String wifiButtonName = applicationConfig.getPlatformVersion().contains("11")
                    ? "wifi-button"
                    : "Wi-Fi";
            final String bluetoothButtonName = "bluetooth-button";


            for (int i = 0; i < 5; i++) {
                try {
                    driverIos.findElement(By.xpath("(//XCUIElementTypeAlert//XCUIElementTypeButton)[last()]")).click();
                    log.info("Clicked on alert button");
                } catch (NoSuchElementException e) {
                    log.info(String.format("Alert not found %s", e.getMessage()));
                }
                final int counter = i + 1;
                log.info("Trying to open control center: " + counter + " try.");
                AbstractService.this.swipeByPercent(20, height - 5, width / 2, -step);
                try {
                    driverIos.findElement(By.name(airplaneButtonName));
                    break;
                } catch (NoSuchElementException e) {
                    log.info(String.format("Alert not found %s", e.getMessage()));
                }
            }

            try {
                final MobileElement continueButton = driverIos.findElement(By.name("Continue"));
                new WebDriverWait(driverIos, 5).until(ExpectedConditions.elementToBeClickable(continueButton));
                continueButton.click();
            } catch (NoSuchElementException e) {
                log.info(String.format("Alert not found %s", e.getMessage()));
            }

            try {
                final MobileElement airplaneIcon = driverIos.findElement(By.name(airplaneButtonName));

                final String value = airplaneIcon.getAttribute("value");
                log.info("Airplane icon state value " + value);

                final boolean airplaneIconState = parseBooleanValue(value);
                log.info("Airplane icon state " + airplaneIconState);

                if (airplaneIconState != airplane) {
                    airplaneIcon.click();
                    log.info("Clicked on Airplane icon");
                }
            } catch (NoSuchElementException e) {
                log.info(String.format("Alert not found %s", e.getMessage()));
            }

            try {
                sleep(2_000);
                final MobileElement wifiIcon = driverIos.findElement(By.name(wifiButtonName));

                final String value = wifiIcon.getAttribute("value");
                log.info("Wifi icon state value " + value);

                final boolean wifiIconState = parseBooleanValue(value);
                log.info("Wifi icon state " + wifiIconState);

                if (wifiIconState != wifi) {
                    wifiIcon.click();
                    log.info("Clicked on WiFi icon");
                }

            } catch (NoSuchElementException e) {
                log.info(String.format("Alert not found %s", e.getMessage()));
            }
            try {
                sleep(2000);
                final MobileElement bluetoothIcon = driverIos.findElement(By.name(bluetoothButtonName));

                final String value = bluetoothIcon.getAttribute("value");
                log.info("Bluetooth icon state value " + value);

                final boolean bluetoothIconState = parseBooleanValue(value);
                log.info("Bluetooth icon state" + bluetoothIconState);

                if (bluetoothIconState != bluetooth) {
                    bluetoothIcon.click();
                    log.info("Clicked on Bluetooth icon");
                }
            } catch (NoSuchElementException e) {
                log.info(String.format("Alert not found %s", e.getMessage()));
            }

            AbstractService.this.swipeVerticallyByPercent(width / 2, 0);
            sleep(2_000);
        }

        private boolean parseBooleanValue(final String value) {
            requireNonNull(value, "Value can not be null");
            return "1".equals(value);
        }
    }
}
