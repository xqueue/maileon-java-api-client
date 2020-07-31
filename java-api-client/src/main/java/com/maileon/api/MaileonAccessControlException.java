package com.maileon.api;

/**
 * The Class MaileonAccessControlException.
 *
 */
public class MaileonAccessControlException extends MaileonException {

    /**
     * Instantiates a new Maileon access control exception.
     *
     * @param responseWrapper original response
     */
    public MaileonAccessControlException(ResponseWrapper responseWrapper) {
        super(responseWrapper);
    }

    /**
     * Instantiates a new Maileon access control exception.
     *
     * @param message the message
     * @param responseWrapper original response
     */
    public MaileonAccessControlException(String message, ResponseWrapper responseWrapper) {
        super(message, responseWrapper);
    }
}
