package com.maileon.api.ping;

import com.maileon.api.AbstractMaileonService;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import com.maileon.api.ResponseWrapper;

/**
 * The <code>MaileonPingService</code> client sends operational requests to the <code>PingResource</code> service.
 *
 */
public class MaileonPingService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON PING";

    /**
     * Instantiates a new Maileon ping service.
     *
     * @param config The Maileon API-Configuration
     */
    public MaileonPingService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Checks the authorization of the API-Key for retrieve actions.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/ping/get-test/?lang=en">Documentation website</a></p>
     *
     * @return Wrapper {@link ResponseWrapper} for the corresponding response
     * @throws MaileonException If the request completed unsuccessfully (the API-key does not hold the right to retrieve data or another problem occurred) an
     * {@link MaileonException} will be thrown
     */
    public ResponseWrapper checkRetrieve() throws MaileonException {
        return get("ping");
    }

    /**
     * Checks the authorization of the API-Key for delete actions.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/ping/delete-test/?lang=en">Documentation website</a></p>
     *
     * @return Wrapper {@link ResponseWrapper} for the corresponding response
     * @throws MaileonException If the request completed unsuccessfully (the API-key does not hold the right to retrieve data or another problem occurred) an
     * {@link MaileonException} will be thrown
     */
    public ResponseWrapper checkDelete() throws MaileonException {
        return delete("ping");
    }

    /**
     * Check the authorization of the API-Key for create actions.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/ping/put-test/?lang=en">Documentation website</a></p>
     *
     * @return Wrapper {@link ResponseWrapper} for the corresponding response
     * @throws MaileonException If the request completed unsuccessfully (the API-key does not hold the right to retrieve data or another problem occurred) an
     * {@link MaileonException} will be thrown
     */
    public ResponseWrapper checkCreate() throws MaileonException {
        return put("ping", null);
    }

    /**
     * Check the authorization of the API-Key for update actions.
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/ping/post-test/?lang=en">Documentation website</a></p>
     *
     * @return Wrapper {@link ResponseWrapper} for the corresponding response
     * @throws MaileonException If the request completed unsuccessfully (the API-key does not hold the right to retrieve data or another problem occurred) an
     * {@link MaileonException} will be thrown
     */
    public ResponseWrapper checkUpdate() throws MaileonException {
        return post("ping", null);
    }

}
