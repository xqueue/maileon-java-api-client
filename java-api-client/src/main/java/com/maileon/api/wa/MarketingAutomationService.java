package com.maileon.api.wa;

import com.maileon.api.AbstractMaileonService;
import com.maileon.api.MaileonClientException;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

/**
 *
 * @author Anouar Haha
 */
public class MarketingAutomationService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON MARKETING AUTOMATION SERVICE";

    public MarketingAutomationService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Start marketing automation program for the given contacts.
     *
     * @param processId id of process
     * @param emails emails of contacts
     * @throws MaileonException
     */
    public void startProcesses(long processId, List<String> emails) throws MaileonException {
        if (processId <= 0L) {
            throw new MaileonClientException("process doesn't exist");
        }
        if (emails == null) {
            throw new MaileonClientException("emails cannot be null");
        }
        if (emails.isEmpty()) {
            throw new MaileonClientException("emails cannot be empty");
        }
        JSONObject json = new JSONObject();
        json.put("emails", emails);
        post("marketing-automation/" + processId, null, MediaType.APPLICATION_JSON_TYPE, json.toJSONString());
    }
}
