package com.afj.solution.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * This annotation can be applied to method(test) to indicate that method(test),
 * linked and(or) blocked(in past) by issue.
 * Put issue number from bug tracking system to <b>value</b> field.
 * If test blocked by issue use additional @Disabled annotation.
 * <p>
 * Usage:
 * <pre>
 *     {@code @Issue}(value="IS-123")
 * </pre>
 *
 * @author Tomash Gombosh
 * @since 1.0.0
 */
@Documented
@Target({METHOD, FIELD})
@Retention(SOURCE)
public @interface Issue {
    String value();
}

