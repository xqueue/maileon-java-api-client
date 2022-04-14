package com.maileon.api.contactfilters;

import com.maileon.api.AbstractMaileonService;
import com.maileon.api.MaileonClientException;
import com.maileon.api.MaileonConfiguration;
import com.maileon.api.MaileonException;
import com.maileon.api.Page;
import com.maileon.api.ResponseWrapper;
import com.maileon.api.contactfilters.serializer.ContactfilterXmlSerializer;
import com.maileon.api.contactfilters.serializer.ContactfiltersXmlSerializer;
import com.maileon.api.utils.PageUtils;
import javax.ws.rs.core.MediaType;
import org.dom4j.Element;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The <code>MaileonContactfiltersService</code> client sends operational requests to the <code>MaileonContactfiltersResource</code> service.
 *
 */
public class MaileonContactfiltersService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON CONTACTFILTERS";

    /**
     * Instantiates a new Maileon ping service.
     *
     * @param config The Maileon API-Configuration
     */
    public MaileonContactfiltersService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

      /**
     * Returns the total count of contact filters in the account. See
     * <a href="https://maileon.com/support/get-contactfilters-count/">Maileon API documentation</a>.
     *
     * @throws MaileonException
     */
    public long getContactfiltersCount() throws MaileonException {
        ResponseWrapper response = get("contactfilters/count");
        try {
            return Long.parseLong(response.getEntityAsXml().getText());
        } catch (MaileonClientException | NumberFormatException e) {
            throw new MaileonException("Unable to parse contact filter count as integer: " + response.getEntityAsXml().getText(), e);
        }
    }

    /**
     * Retuns a page of contact filters using the provided pagination settings. See
     * <a href="https://maileon.com/support/get-contactfilters/">Maileon API documentation</a>.
     *
     * @param pageSize required number of elements in the result page [1..1000].
     * @param pageIndex starts with 1.
     * @return a page containing the returned {@link Contactfilter} objects.
     * @throws MaileonException
     */
    public Page<Contactfilter> getContactfilters(int pageSize, int pageIndex) throws MaileonException {
        QueryParameters qp = new QueryParameters("pageSize", pageSize);
        qp.add("pageIndex", pageIndex);

        ResponseWrapper response = get("contactfilters", qp);

        try {
            Page<Contactfilter> page = PageUtils.createPage(pageIndex, pageSize, response);

            Element xml = response.getEntityAsXml();
            page.setItems(ContactfiltersXmlSerializer.deserialize(xml));

            return page;
        } catch (Exception e) {
            throw new MaileonException("Problem while executing GET on all contact filters", e);
        }
    }

    /**
     * Gets a contacts-filter according to a particular id. See
     * <a href="https://maileon.com/support/get-contactfilter/">Maileon API documentation</a>.
     *
     * @param contactFilterId id of the requested filter
     * @return the {@link Contactfilter} instance
     * @throws MaileonException
     */
    public Contactfilter getContactfilter(long contactFilterId) throws MaileonException {
        ResponseWrapper response = get("contactfilters/contactfilter/" + contactFilterId);

        try {
            return ContactfilterXmlSerializer.deserializeSingleElement(response.getEntityAsXml());
        } catch (Exception e) {
            throw new MaileonException("Problem while executing GET on single contact filter", e);
        }
    }

    /**
     * Updates a particular contacts filter with new settings. See
     * <a href="https://maileon.com/support/update-contact-filter/">Maileon API documentation</a>.
     *
     * @param contactFilterId id of the required filter
     * @param newFilterData the new data with which the contact filter will be updated. Currently only the name of the underlying filter will be
     * updated
     * @throws MaileonException
     */
    public void updateContactfilter(long contactFilterId, Contactfilter newFilterData) throws MaileonException {
        try {
            post("contactfilters/contactfilter/" + contactFilterId, null, ContactfilterXmlSerializer.serialize(newFilterData).asXML());
        } catch (MaileonException e) {
            throw e;
        } catch (Exception e) {
            throw new MaileonException("Problem while executing POST", e);
        }
    }
    /**
     * Deletes a particular contacts filter. See
     * <a href="https://maileon.com/support/delete-contactsfilter/">Maileon API documentation</a>.
     *
     * @param contactFilterId id of the filter
     * @throws MaileonException
     */
    public void deleteContactfilter(long contactFilterId) throws MaileonException {
        try {
            delete("contactfilters/contactfilter/" + contactFilterId);
        } catch (MaileonException e) {
            throw e;
        } catch (Exception e) {
            throw new MaileonException("Problem while executing POST", e);
        }
    }
    
    /**
     * Update contacts of a particular contacts filter. See
     * <a href="https://maileon.com/support/refresh-contactfilter-contacts/">Maileon API documentation</a>.
     *
     * @param contactFilterId id of the required contacts filter
     * @param time timestamp in milliseconds since January 1, 1970, 00:00:00 GMT.
     *  The filter is going to be updated if the last actualization has been done before the given time
     * @param asynchronously if true, then update will be queued and the call returns immediately
     * @throws MaileonException
     */
    public void refreshContactfilterContacts(long contactFilterId, long time, boolean asynchronously) throws MaileonException {
        try {
            QueryParameters qp = new QueryParameters("time", time);
            qp.add("async", asynchronously);

            get("contactfilters/contactfilter/" + contactFilterId + "/refresh", qp);
        } catch (MaileonException e) {
            throw e;
        } catch (Exception e) {
            throw new MaileonException("Problem while executing refreshing filter " + contactFilterId, e);
        }
    }

    /**
     * Update contacts of a particular contacts filter synchronously. See
     * <a href="https://maileon.com/support/refresh-contactfilter-contacts/">Maileon API documentation</a>.
     *
     * @param contactFilterId id of the required contacts filter
     * @param time timestamp in milliseconds since January 1, 1970, 00:00:00 GMT.
     *  The filter is going to be updated if the last actualization has been done before the given time
     * @throws MaileonException
     * @see #refreshContactfilterContacts(long, long, boolean)
     */
    public void refreshContactfilterContacts(long contactFilterId, long time) throws MaileonException {
        refreshContactfilterContacts(contactFilterId, time, false);
    }
    
    

    /**
     * Creates a simple contact filter.
     *
     * @param newFilter
     *  the data for the filter as a JSON string
     * @param createTargetGroup
     *  if true, also a target group will be created and the ID will be returned
     * @return ContactfilterTargetgroupTuple
     *    the result object of the API call, containing the IDs of the contact filter and targetgroup (if created as well, -1 else)
     */
    public ContactfilterTargetgroupTuple createContactfilter(String newFilter, boolean createTargetGroup) throws MaileonException {
        QueryParameters qp = new QueryParameters("createTargetGroup", createTargetGroup?"true":"false");
           
        ResponseWrapper response = post("contactfilters/v2", qp, MediaType.APPLICATION_JSON_TYPE, newFilter);
        
        try {
            JSONObject result = (JSONObject) new JSONParser().parse(response.getEntityAsString());
            
            return new ContactfilterTargetgroupTuple((long)result.get("contact_filter_id"), (long)result.get("target_group_id"));
        } catch (ParseException e) {
            throw new MaileonException("Unable to parse server response: " + e.getMessage(), e);
        }
    }

}
