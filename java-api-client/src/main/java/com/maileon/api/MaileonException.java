package com.maileon.api;

/**
 * The Class MaileonException.
 *
 */
public class MaileonException extends RuntimeException {

    /**
     * The status code.
     */
    private int statusCode;

    /**
     * The reason phrase.
     */
    private String reasonPhrase;

    /**
     * Response Wrapper with raw response for debugging.
     */
    private ResponseWrapper responseWrapper;

    /**
     * Instantiates a new Maileon exception.
     */
    public MaileonException() {

    }

    /**
     * Instantiates a new Maileon exception.
     *
     * @param responseWrapper original response
     */
    public MaileonException(ResponseWrapper responseWrapper) {
        this.responseWrapper = responseWrapper;
        this.statusCode = responseWrapper.getStatusCode();
        this.reasonPhrase = responseWrapper.getReasonPhrase();
    }

    /**
     * Instantiates a new Maileon exception.
     *
     * @param message the message
     */
    public MaileonException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Maileon exception.
     *
     * @param message the message
     * @param responseWrapper original response
     */
    public MaileonException(String message, ResponseWrapper responseWrapper) {
        super(message);
        this.responseWrapper = responseWrapper;
        this.statusCode = responseWrapper.getStatusCode();
        this.reasonPhrase = responseWrapper.getReasonPhrase();
    }

    /**
     * Instantiates a new Maileon exception.
     *
     * @param message the message
     * @param t the t
     */
    public MaileonException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the reason phrase.
     *
     * @return the reason phrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public ResponseWrapper getResponseWrapper() {
        return responseWrapper;
    }

}
