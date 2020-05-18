package com.afj.solution.test.mobile.core.exception;

import java.io.Serializable;

import lombok.extern.log4j.Log4j;

import com.afj.solution.test.mobile.core.AppElement;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class NoSuchAttributeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -8742900403658909408L;

    public NoSuchAttributeException() {
        super();
    }

    public NoSuchAttributeException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(String.format("No such attribute %s", message), cause);
    }

    public NoSuchAttributeException(final String message) {
        super(message);
        log.error(String.format("No such attribute %s", message));
    }

    public NoSuchAttributeException(final String message, final AppElement element) {
        super(message);
        log.error(String.format("No such attribute %s in element %s", message, element.getName()));
    }

    public NoSuchAttributeException(final Throwable cause) {
        super(cause);
        log.error(String.format("No such attribute %s", cause.getMessage()), cause);
    }
}
