package com.afj.solution.test.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * This annotation can be applied to method to indicate that method or etc
 * to ignore the visibility of that from the check style
 *
 * @author Tomash Gombosh
 * @since 1.0.0
 */
@Documented
@Target({METHOD, FIELD})
@Retention(SOURCE)
public @interface Ignore {
}
