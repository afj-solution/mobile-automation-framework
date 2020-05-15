package com.afj.solution.mobile.core.exceptions;

import java.io.Serializable;

import com.afj.solution.mobile.core.AppElement;
import lombok.extern.log4j.Log4j;

/**
 * @author Tomash Gombosh
 */
@Log4j
public class NoValueInInputException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6220774912107392764L;

    public NoValueInInputException() {
        super();
    }

    public NoValueInInputException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(String.format("No value in the input %s", message), cause);
    }

    public NoValueInInputException(final String message) {
        super(message);
        log.error(String.format("No value in the input %s", message));
    }

    public NoValueInInputException(final Throwable cause) {
        super(cause);
        log.error(String.format("No value in the input %s", cause.getMessage()), cause);
    }

    public NoValueInInputException(final String message, final AppElement element) {
        super(message);
        log.error(String.format("No value in the input %s", element.getName()));
    }
}
