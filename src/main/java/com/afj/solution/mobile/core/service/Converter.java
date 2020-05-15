package com.afj.solution.mobile.core.service;

public interface Converter<T, S> {
    T convert(S source);
}
