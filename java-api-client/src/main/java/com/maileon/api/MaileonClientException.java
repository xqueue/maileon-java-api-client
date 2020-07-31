package com.maileon.api;

/**
 * The Class MaileonClientException.
 *
 */
public class MaileonClientException extends MaileonException {

    /**
     * Instantiates a new Maileon client exception.
     *
     * @param message the message
     * @param t the t
     */
    public MaileonClientException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Instantiates a new Maileon client exception.
     *
     * @param message the message
     */
    public MaileonClientException(String message) {
        super(message);
    }
}
