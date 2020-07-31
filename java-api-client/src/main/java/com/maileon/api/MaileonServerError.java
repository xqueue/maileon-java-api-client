package com.maileon.api;

/**
 * The Class MaileonServerError.
 *
 */
public class MaileonServerError extends MaileonException {

    /**
     * Instantiates a new Maileon server error.
     *
     * @param responseWrapper original response
     */
    public MaileonServerError(ResponseWrapper responseWrapper) {
        super(responseWrapper);
    }
}
