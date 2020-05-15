package com.afj.solution.mobile.core.service.elements;

import com.afj.solution.mobile.core.AppElement;
import org.openqa.selenium.By;

/**
 * @author Tonash Gombosh
 */
public interface WaitService {

    void waitToBeVisible(By element);

    void waitToBeVisible(AppElement element);

    void waitToBeInvisible(By element, int timeout);

    void waitToBeInvisible(By element);

    void waitToBeInvisible(AppElement element, int timeout);

    void waitToBeInvisible(AppElement element);

    void waitToBeClickable(By element, int timeout);

    void waitToBeClickable(By element);

    void waitToBeClickable(AppElement element, int timeout);

    void waitToBeClickable(AppElement element);

    void waitToBePresent(By element, int timeout);

    void waitToBePresent(By element);

    void waitToBePresent(AppElement element, int timeout);

    void waitToBePresent(AppElement element);

    void waitForAttributeValue(By element, String attributeName, String expectedValue);

    void waitForAttributeValue(AppElement element, String attributeName, String expectedValue);

}