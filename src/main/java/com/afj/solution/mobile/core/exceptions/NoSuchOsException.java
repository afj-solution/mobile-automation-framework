package com.afj.solution.mobile.core.exceptions;

import java.io.Serializable;

import lombok.extern.log4j.Log4j;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class NoSuchOsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5980197442061756936L;

    public NoSuchOsException() {
        super();
    }

    public NoSuchOsException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(String.format("No such mobile os %s", message), cause);
    }

    public NoSuchOsException(final String message) {
        super(message);
        log.error(String.format("No such mobile os %s", message));
    }

    public NoSuchOsException(final Throwable cause) {
        super(cause);
        log.error(String.format("No such mobile os %s", cause.getMessage()), cause);
    }
}
