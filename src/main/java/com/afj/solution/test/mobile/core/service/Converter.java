package com.afj.solution.test.mobile.core.service;

/**
 * @author Tomash Gombosh
 */
public interface Converter<T, S> {
    T convert(S source);
}
