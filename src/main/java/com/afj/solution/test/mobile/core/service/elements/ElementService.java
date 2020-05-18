package com.afj.solution.test.mobile.core.service.elements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.afj.solution.test.mobile.core.AppElement;

/**
 * @author Tomash Gombosh
 */
public interface ElementService {
    WebElement find(By element);

    WebElement find(AppElement element);

    List<By> findAll(By element);

    List<AppElement> findAll(AppElement element);

    boolean isElementVisible(By element);

    boolean isElementVisible(AppElement element);

    boolean isElementPresent(AppElement element, int timeout);

    boolean isElementPresent(By element, int timeout, String name);

    boolean isElementPresent(By element);

    boolean isElementPresent(AppElement element);

    boolean isElementEnabled(AppElement element);

    boolean isElementEnabled(By element);

    boolean isAttributePresent(AppElement appElement, String attribute);

    String getAttributeValue(AppElement appElement, String attributeName);

    boolean isAttributeEquals(AppElement appElement, String attributeName, String expectedValue);

    void scrollDownUntilVisible(AppElement element);

    void scrollUpUntilVisible(AppElement element);

    void scrollToElement(AppElement ele);

    void scrollFromToElement(By startElement, By endElement);

    void scrollFromToElement(AppElement startElement, AppElement endElement);
}
