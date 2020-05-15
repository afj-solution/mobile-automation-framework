package com.afj.solution.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * This annotation can be applied to class to indicate that class,
 * is configuration same OS
 * Put the what is configuration that class in the <b>value</b> field.
 * <p>
 * Usage:
 * <pre>
 *     {@code @Config}(value="Android config")
 * </pre>
 *
 * @author Tomash Gombosh
 * @since 1.0.0
 */
@Documented
@Retention(SOURCE)
public @interface Config {
    String value();
}
