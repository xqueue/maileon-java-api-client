package com.maileon.api;

import java.net.Proxy;

/**
 * The Class MaileonConfiguration.
 *
 */
public class MaileonConfiguration {

    /**
     * The base URI. *
     */
    private final String baseUri;

    /**
     * The API key. *
     */
    private final String apiKey;

    /**
     * The proxy *
     */
    private final MaileonProxy proxy;

    /**
     * Should debug information be displayed? *
     */
    private boolean debug = false;

    /**
     * Should SSL errors be ignored? Works ONLY with debug=true as it is intended only for quick "local" testing, not for production. *
     */
    private boolean ignoreSslErrors = false;

    /**
     * Instantiates a new Maileon configuration.
     *
     * @param baseUri the base URI
     * @param apiKey the API key
     */
    public MaileonConfiguration(String baseUri, String apiKey) {
        this(baseUri, apiKey, Proxy.NO_PROXY);
    }

    /**
     * Instantiates a new Maileon configuration.
     *
     * @param baseUri the base URI
     * @param apiKey the API key
     * @param proxy proxy definition
     */
    public MaileonConfiguration(String baseUri, String apiKey, Proxy proxy) {
        this.baseUri = baseUri;
        this.apiKey = apiKey;

        if (proxy instanceof MaileonProxy) {
            this.proxy = (MaileonProxy) proxy;
        } else {
            if (proxy != Proxy.NO_PROXY) {
                this.proxy = new MaileonProxy(proxy.type(), proxy.address());
            } else {
                this.proxy = null;
            }
        }
    }

    /**
     * Gets the base URI.
     *
     * @return the base URI
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * Gets the API key.
     *
     * @return the API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Gets the proxy.
     *
     * @return the proxy
     */
    public MaileonProxy getProxy() {
        return proxy;
    }

    /**
     * Describes if debug mode with extended output is enabled
     *
     * @return true, if debug mode is enabled
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set if debug mode with extended output is enabled
     *
     * @param debug True, if debug mode should be enabled
     * @return this
     */
    public MaileonConfiguration setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * Describes if SSL errors be ignored? Works ONLY with debug=true as it is intended only for quick "local" testing, not for production.
     *
     * @return true, if debug mode AND ignoreSslErrors are enabled
     */
    public boolean isIgnoreSslErrors() {
        return debug && ignoreSslErrors;
    }

    /**
     * If set to debug mode and this setting is true, SSL errors will be ignored
     *
     * @param ignoreSslErrors true if ssl errors should be ignored
     * @return this
     */
    public MaileonConfiguration setIgnoreSslErrors(boolean ignoreSslErrors) {
        this.ignoreSslErrors = ignoreSslErrors;
        return this;
    }
}
