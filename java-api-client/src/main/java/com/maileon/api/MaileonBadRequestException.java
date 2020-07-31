package com.maileon.api;

/**
 * The Class MaileonBadRequestException.
 *
 */
public class MaileonBadRequestException extends MaileonException {

    /**
     * Instantiates a new Maileon bad request exception.
     *
     * @param message the message
     */
    public MaileonBadRequestException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Maileon bad request exception.
     *
     * @param responseWrapper original response
     */
    public MaileonBadRequestException(ResponseWrapper responseWrapper) {
        super(responseWrapper);
    }

    /**
     * Instantiates a new maileon bad request exception.
     *
     * @param message the message
     * @param responseWrapper original response
     */
    public MaileonBadRequestException(String message, ResponseWrapper responseWrapper) {
        super(message, responseWrapper);
    }
}
