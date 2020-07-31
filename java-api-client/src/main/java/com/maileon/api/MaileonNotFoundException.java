package com.maileon.api;

/**
 * The Class MaileonNotFoundException.
 *
 */
public class MaileonNotFoundException extends MaileonException {

    private static final long serialVersionUID = -2716645571382241113L;

    /**
     * Instantiates a new Maileon not found exception.
     *
     * @param message the message
     */
    public MaileonNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Maileon not found exception.
     *
     * @param responseWrapper original response
     */
    public MaileonNotFoundException(ResponseWrapper responseWrapper) {
        super(responseWrapper);
    }

    /**
     * Instantiates a new Maileon not found exception.
     *
     * @param message the message
     * @param responseWrapper original response
     */
    public MaileonNotFoundException(String message, ResponseWrapper responseWrapper) {
        super(message, responseWrapper);
    }
}
