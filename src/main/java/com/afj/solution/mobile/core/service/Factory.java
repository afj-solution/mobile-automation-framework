package com.afj.solution.mobile.core.service;

/**
 * @author Tomash Gombosh
 * @since 1.0.0
 */
public interface Factory<T, R extends Factory<T, R>> {
    T create();
}
