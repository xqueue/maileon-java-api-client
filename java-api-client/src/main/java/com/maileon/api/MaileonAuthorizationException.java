package com.maileon.api;

/**
 * The Class MaileonAuthorizationException.
 *
 */
public class MaileonAuthorizationException extends MaileonException {

    /**
     * Instantiates a new Maileon authorization exception.
     *
     * @param responseWrapper original response
     */
    public MaileonAuthorizationException(ResponseWrapper responseWrapper) {
        super(responseWrapper);
    }

    /**
     * Instantiates a new Maileon authorization exception.
     *
     * @param message the message
     * @param responseWrapper original response
     */
    public MaileonAuthorizationException(String message, ResponseWrapper responseWrapper) {
        super(message, responseWrapper);
    }
}
