package com.afj.solution.mobile.core.exceptions;

import java.io.Serializable;

import lombok.extern.log4j.Log4j;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class NoValueFromConfigException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1329573108063956749L;

    public NoValueFromConfigException() {
        super();
    }

    public NoValueFromConfigException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(String.format("No such mobile os %s", message), cause);
    }

    public NoValueFromConfigException(final String message) {
        super(message);
        log.error(String.format("No such mobile os %s", message));
    }

    public NoValueFromConfigException(final Throwable cause) {
        super(cause);
        log.error(String.format("No such mobile os %s", cause.getMessage()), cause);
    }
}
