package com.afj.solution.test.mobile.core.service;

public interface Converter<T, S> {
    T convert(S source);
}
