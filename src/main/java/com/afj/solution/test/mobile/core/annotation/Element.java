package com.afj.solution.test.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * This annotation can be applied to App Elements for the ignoring the PMD violation
 * <p>
 * Usage:
 * <pre>
 *     {@code @Element}private final AppElement element
 * </pre>
 *
 * @author Tomash Gombosh
 * @since 1.0.0
 */
@Documented
@Retention(SOURCE)
public @interface Element {
}
