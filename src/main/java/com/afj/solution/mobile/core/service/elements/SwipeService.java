package com.afj.solution.mobile.core.service.elements;

import org.openqa.selenium.Point;

/**
 * @author Tomash Gombosh
 */
public interface SwipeService {

    void swipeLeftToRight();

    void swipeRightToLeft();

    void swipeVerticallyByPercent(int startPercent, int endPercent);

    void swipeHorizontallyByPercent(int startPercent, int endPercent);

    void swipeByPercent(int startXPercent, int endXPercent, int startYPercent, int endYPercent);

    void swipeToTop(Point start);

    void swipe(Point start, Point end);
}
