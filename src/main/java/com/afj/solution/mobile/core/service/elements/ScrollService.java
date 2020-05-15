package com.afj.solution.mobile.core.service.elements;

import com.afj.solution.mobile.core.enums.ScrollTo;
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
