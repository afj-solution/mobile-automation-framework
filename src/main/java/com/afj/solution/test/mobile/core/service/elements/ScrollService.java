package com.afj.solution.test.mobile.core.service.elements;

import org.openqa.selenium.By;

import com.afj.solution.test.mobile.core.enums.ScrollTo;

/**
 * @author Tomash Gombosh
 */
public interface ScrollService {

    void scrollUpUntilVisible(By element);

    void scrollDownUntilVisible(By element);

    void scrollUntilVisible(ScrollTo scrollDirection, By element);

    void scrollUp();

    void scrollDown();
}
