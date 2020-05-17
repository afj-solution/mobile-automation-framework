package com.afj.solution.test.mobile.core.service.elements;

import com.afj.solution.test.mobile.core.enums.ScrollTo;
import org.openqa.selenium.By;

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
