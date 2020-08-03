package com.maileon.api.contacts;

import com.maileon.api.*;
import com.maileon.api.utils.PageUtils;
import java.text.SimpleDateFormat;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ws.rs.WebApplicationException;

/**
 * The <code>MaileonContactsService</code> client sends operational requests to the <code>ContactsResource</code>.
 *
 */
public class MaileonContactsService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON CONTACTS";

    /**
     * Constructs a <code>MaileonContactsService</code>.
     *
     * @param config Maileon API-Configuration.
     */
    public MaileonContactsService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Gets the total count of contacts in the account. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-count-contacts/?lang=en">Maileon API documentation</a>.
     *
     * @return the count of contacts.
     * @throws MaileonException
     */
    public int countContacts() throws MaileonException {
        return MaileonContactsService.this.countContacts(null);
    }

    /**
     * Gets the total count of contacts in the account.See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-count-contacts/?lang=en">Maileon API documentation</a>.
     *
     * @param updatedAfter filter for updated-property
     * @return the count of contacts.
     * @throws MaileonException
     */
    public int countContacts(Date updatedAfter) throws MaileonException {
        ResponseWrapper response;
        if (updatedAfter == null) { // all contacts
            response = get("contacts/count");
        } else {
            SimpleDateFormat dateformat = new SimpleDateFormat(DateTimeConstants.SQL_DATE_TIME_FORMAT, Locale.ENGLISH);
            QueryParameters params = new QueryParameters("updated_after", dateformat.format(updatedAfter));
            response = get("contacts/count", params);
        }

        try {
            return Integer.parseInt(response.getEntityAsXml().getText());
        } catch (NumberFormatException nfe) {
            throw new MaileonClientException("unexpected response format", nfe);
        }
    }

    /**
     * Creates a single contact with a given synchronization mode.
     *
     * @param contact the {@link Contact} object to create.
     * @param syncMode the {@link SynchronizationMode} determines the case whether the contact should be update or ignored if it already exists.
     * @return id of created contact
     * @throws MaileonException the maileon exception
     */
    public long createContact(Contact contact, SynchronizationMode syncMode) throws MaileonException {
        return createContact(contact, syncMode, null, null, false, false, null);
    }

    /**
     * Creates a single contact.See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/create-contact/?lang=en">Maileon API documentation</a>.
     *
     * @param contact the {@link Contact} to create.
     * @param syncMode the {@link SynchronizationMode} determines whether the permission should be updated or ignored if they already exist.
     * @param src the source of the contact if provided.
     * @param subscriptionPage in case where this method was called by a subscription page. If provided, then it offers the possibility to track the contacts for reporting usages.
     * @param doi the doi, default <code>false</code>. It determines where a double opt-in process should be started for the created contact.
     * @param doiPlus the doi plus, default <code>false</code>. The same as above, however doi plus will ignored if the parameter doi is set to <code>false</code>.
     * @param doiMailingKey whether the doi mailing key should be used or not. It is also dependent on the doi parameter (activated or not).
     *
     * @return id of the created contact
     * @throws MaileonException the maileon exception
     */
    public long createContact(Contact contact, SynchronizationMode syncMode, String src, String subscriptionPage, boolean doi, boolean doiPlus, String doiMailingKey)
            throws MaileonException {
        return createContact(contact, syncMode, src, subscriptionPage, doi, doiPlus, doiMailingKey, false);
    }

    /**
     * Creates a single contact and allows the usage of an externalID.See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/create-contact-by-external-id/?lang=en">Maileon
     * API documentation</a>.
     *
     * @param contact the {@link Contact} to create.
     * @param syncMode the {@link SynchronizationMode} determines whether the permission should be updated or ignored if they already exist.
     * @param src the source of the contact if provided.
     * @param subscriptionPage in case where this method was called by a subscription page. If provided, then it offers the possibility to track the contacts for reporting usages.
     * @param doi the doi, default <code>false</code>. It determines where a double opt-in process should be started for the created contact.
     * @param doiPlus the doi plus, default <code>false</code>. The same as above, however doi plus will ignored if the parameter doi is set to <code>false</code>.
     * @param doiMailingKey whether the doi mailing key should be used or not. It is also dependent on the doi parameter (activated or not).
     * @param useExternalIdasPrimaryID if <code>true</code> the externalId will be used as a contact identifier, otherwise the email.
     *
     * @return id of the created contact
     * @throws MaileonException the maileon exception
     */
    public long createContact(Contact contact, SynchronizationMode syncMode, String src, String subscriptionPage,
            boolean doi, boolean doiPlus, String doiMailingKey, boolean useExternalIdasPrimaryID)
            throws MaileonException {
        if (contact == null) {
            throw new MaileonClientException("contact cannot be null");
        }
        if (contact.getEmail() == null) {
            throw new MaileonClientException("email address cannot be null");
        }
        if (useExternalIdasPrimaryID && contact.getExternalId() == null) {
            throw new MaileonClientException("external ID cannot be null when using as primary ID");
        }
        QueryParameters params = new QueryParameters();
        Permission permission = contact.getPermission();
        if (permission != null) {
            params.add("permission", permission.getCode());
        }
        if (syncMode != null) {
            params.add("sync_mode", syncMode.getCode());
        }
        params.add("src", src);
        params.add("subscription_page", subscriptionPage);
        if (doi) {
            params.add("doi", doi);
            if (doiPlus) {
                params.add("doiplus", doiPlus);
            }
            if (doiMailingKey != null) {
                params.add("doimailing", doiMailingKey);
            }
        }

        String xml = ContactAdaptor.toXml(contact, true).asXML();
        ResponseWrapper resp;
        if (useExternalIdasPrimaryID) {
            resp = post("contacts/externalid/" + encodePath(contact.getExternalId()), params, xml);
        } else {
            resp = post("contacts/" + encodePath(contact.getEmail()), params, xml);
        }

        if (resp.getStatusCode() == 201) {
            return Long.parseLong(resp.getEntityAsXml().getText());
        }
        throw new WebApplicationException(resp.getStatusCode());
    }

    /**
     * This method synchronizes (updates) a list of contacts in the account. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/synchronize-contacts/?lang=en">Maileon
     * API documentation</a>.
     *
     * @param contacts list of {@link Contact} to synchronize.
     * @param permission the assigned permission (none, single opt-in, double opt-in, ..., etc).
     * @param syncMode the {@link SynchronizationMode} determines the case whether the permission should be updated or ignored if they already exist.
     * @param useExternalId if <code>true</code> the externalId will be used as a contact identifier, otherwise the email.
     * @param ignoreInvalidContacts if <code>true</code> the invalid contacts will be ignored.
     * @param reimportUnsubscribedContacts if <code>true</code> the unsubscribed contacts will be reimported, otherwise they will be ignored.
     * @param overridePermission if <code>true</code> the permission of existing and non existing contacts will be overridden. In case of <code>false</code>, the permission will be
     * used for new contacts only and existing contacts will not be influenced.
     * @param updateOnly if <code>true</code> only existing contacts are updated and no new contacts are created.
     * @return {@link SynchronizationReport} detailed report with statistics and validation errors.
     * @throws MaileonException
     */
    public SynchronizationReport synchronizeContacts(List<Contact> contacts, Permission permission, SynchronizationMode syncMode,
            boolean useExternalId, boolean ignoreInvalidContacts, boolean reimportUnsubscribedContacts, boolean overridePermission, boolean updateOnly)
            throws MaileonException {
        return synchronizeContacts(contacts, permission, syncMode, useExternalId, ignoreInvalidContacts, reimportUnsubscribedContacts, overridePermission, updateOnly, false);
    }

    /**
     * This method synchronizes (updates) a list of contacts in the account. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/synchronize-contacts/?lang=en">Maileon
     * API documentation</a>.
     *
     * @param contacts list of {@link Contact} to synchronize.
     * @param permission the assigned permission (none, single opt-in, double opt-in, ..., etc).
     * @param syncMode the {@link SynchronizationMode} determines the case whether the permission should be updated or ignored if they already exist.
     * @param useExternalId if <code>true</code> the externalId will be used as a contact identifier, otherwise the email.
     * @param ignoreInvalidContacts if <code>true</code> the invalid contacts will be ignored.
     * @param reimportUnsubscribedContacts if <code>true</code> the unsubscribed contacts will be reimported, otherwise they will be ignored.
     * @param overridePermission if <code>true</code> the permission of existing and non existing contacts will be overridden. In case of <code>false</code>, the permission will be
     * used for new contacts only and existing contacts will not be influenced.
     * @param updateOnly if <code>true</code> only existing contacts are updated and no new contacts are created.
     * @param preferMaileonId if <code>true</code> Maileon tries identifying contacts by Maileon-ID, if available. Fallback is always the email address, combination with
     * useExternalId is forbidden.
     * @return {@link SynchronizationReport} detailed report with statistics and validation errors.
     * @throws MaileonException
     */
    public SynchronizationReport synchronizeContacts(List<Contact> contacts, Permission permission, SynchronizationMode syncMode,
            boolean useExternalId, boolean ignoreInvalidContacts, boolean reimportUnsubscribedContacts, boolean overridePermission, boolean updateOnly, boolean preferMaileonId)
            throws MaileonException {
        if (contacts == null) {
            throw new MaileonClientException("contacts cannot be null");
        }
        if (contacts.isEmpty()) {
            return new SynchronizationReport();
        }
        QueryParameters params = new QueryParameters();
        if (permission != null) {
            params.add("permission", permission.getCode());
        }
        if (syncMode != null) {
            params.add("sync_mode", syncMode.getCode());
        }
        if (preferMaileonId) {
            params.add("prefer_maileon_id", true);
        }

        params.add("ignore_invalid_contacts", ignoreInvalidContacts);
        params.add("reimport_unsubscribed_contacts", reimportUnsubscribedContacts);
        params.add("override_permission", overridePermission);
        params.add("update_only", updateOnly);

        params.add("use_external_id", useExternalId);
        String xml = ContactAdaptor.toXml(contacts).asXML();
        return SynchronizationReportAdaptor.fromXml(post("contacts", params, xml).getEntityAsXml());
    }

    /**
     * Returns a page of contacts in the accounts. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-contacts/?lang=en">Maileon API documentation</a>.
     *
     * @param standardFields standard properties of the contacts as list of {@link StandardContactField}.
     * @param customFields custom properties of the contacts.
     * @param pageIndex the page index starting from 1.
     * @param pageSize the number of contacts in the page.
     * @return a page containing the returned {@link Contact} objects.
     * @throws MaileonException the maileon exception
     */
    public Page<Contact> getContacts(List<StandardContactField> standardFields, List<String> customFields, int pageIndex, int pageSize) throws MaileonException {
        return getContacts(standardFields, customFields, pageIndex, pageSize, null);
    }

    /**
     * Returns a page of contacts in the accounts.See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-contacts/?lang=en">Maileon API documentation</a>.
     *
     * @param standardFields standard properties of the contacts as list of {@link StandardContactField}.
     * @param customFields custom properties of the contacts.
     * @param pageIndex the page index starting from 1.
     * @param pageSize the number of contacts in the page.
     * @param updatedAfter filter for updated-property
     * @return a page containing the returned {@link Contact} objects.
     * @throws MaileonException the maileon exception
     */
    public Page<Contact> getContacts(List<StandardContactField> standardFields, List<String> customFields, int pageIndex, int pageSize, Date updatedAfter) throws MaileonException {
        if (pageIndex < 1) {
            throw new MaileonBadRequestException("pageIndex must be > 0 - found: " + pageIndex);
        }
        if (pageSize < 1) {
            throw new MaileonBadRequestException("pageSize must be > 0 - found: " + pageSize);
        }
        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));
        if (updatedAfter != null) {
            SimpleDateFormat dateformat = new SimpleDateFormat(DateTimeConstants.SQL_DATE_TIME_FORMAT, Locale.ENGLISH);
            params.add("updated_after", dateformat.format(updatedAfter));
        }

        ResponseWrapper response = get("contacts", params);
        Page<Contact> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        page.setItems(ContactAdaptor.fromXml(xml.elements("contact")));
        return page;
    }

    /**
     * Returns a page of contacts in the accounts that match a particular filter. See <a href="https://dev.maileon.com/get-contacts-by-filter-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param contactFilterId the id of the required filter.
     * @param standardFields standard properties of the contacts as list of {@link StandardContactField}.
     * @param customFields custom properties of the contacts.
     * @param pageIndex the page index starting from 1.
     * @param pageSize the number of contacts in the page.
     * @return a page containing the returned {@link Contact} objects.
     * @throws MaileonException the maileon exception
     */
    public Page<Contact> getContactsByFilterId(long contactFilterId, List<StandardContactField> standardFields, List<String> customFields, int pageIndex, int pageSize)
            throws MaileonException {

        QueryParameters params = new QueryParameters("page_index", pageIndex);
        params.add("page_size", pageSize);
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));

        ResponseWrapper response = get("contacts/filter/" + contactFilterId, params);

        Page<Contact> page = PageUtils.createPage(pageIndex, pageSize, response);

        Element xml = response.getEntityAsXml();
        page.setItems(ContactAdaptor.fromXml(xml.elements("contact")));

        return page;
    }

    /**
     * Returns a single contact with a given email address.
     *
     * @param email the email of the searched contacts.
     * @param standardFields standard properties of the contact as list of {@link StandardContactField}.
     * @param customFields custom properties of the contact.
     * @return the found {@link Contact}.
     * @throws MaileonException
     */
    public Contact getContact(String email, List<StandardContactField> standardFields, List<String> customFields) throws MaileonException {
        if (email == null || !email.contains("@")) {
            throw new MaileonNotFoundException("contact with email " + email + " isn't found");
        }
        QueryParameters params = new QueryParameters();
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));
        Element xml = get("contacts/email/" + encodePath(email), params).getEntityAsXml();
        return ContactAdaptor.fromXml(xml);
    }

    /**
     * Returns a single contact using maileon contact id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-contact-by-maileon-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param id the maileon contact id.
     * @param checksum the checksum of the maileon contact id to prevent form fields manipulation.
     * @param standardFields standard properties of the contact as list of {@link StandardContactField}.
     * @param customFields custom properties of the contact.
     * @return the found {@link Contact}.
     * @throws MaileonException
     */
    public Contact getContact(long id, String checksum, List<StandardContactField> standardFields, List<String> customFields) throws MaileonException {
        if (checksum == null) {
            throw new MaileonBadRequestException("illegal checksum");
        }
        QueryParameters params = new QueryParameters("id", id);
        params.add("checksum", checksum);
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));

        Element xml = get("contacts/contact", params).getEntityAsXml();
        return ContactAdaptor.fromXml(xml);
    }

    /**
     * Returns a list of contacts using an external id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-contacts-by-external-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param externalId the externaId to use.
     * @param standardFields standard properties of the contact as list of {@link StandardContactField}.
     * @param customFields custom properties of the contact.
     * @return a list of found {@link Contact}.
     * @throws MaileonException
     */
    public List<Contact> getContactsByExternalId(String externalId, List<StandardContactField> standardFields, List<String> customFields) throws MaileonException {
        if (externalId == null || externalId.isEmpty()) {
            throw new MaileonNotFoundException("contact with externalId " + externalId + " isn't found");
        }
        QueryParameters params = new QueryParameters();
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));
        Element xml = get("contacts/externalid/" + encodePath(externalId), params).getEntityAsXml();

        return ContactAdaptor.fromXml(xml.elements());
    }

    /**
     * Updates a contact using particular settings.
     *
     * @param contact the {@link Contact} to update
     * @param checksum the corresponding checksum of the contact.
     * @param triggerDoi if <code>true</code> a doi process will be triggered.
     * @param src the source of the contact if provided.
     * @param pageKey in case where this method was called by a landing page. If provided, then it offers the possibility to track the contact for reporting usages or in related
     * doi processes.
     * @param doiMailingKey whether the doi mailing key should be used or not. whether the doi mailing key should be used or not. It is also dependent on the triggerDoi parameter
     * (activated or not).
     * @param ignoreChecksum if this flag ist set to <code>true</code>, the method will ignore the checksum.
     * @throws MaileonException
     */
    public void updateContact(Contact contact, String checksum, boolean triggerDoi, String src, String pageKey, String doiMailingKey, boolean ignoreChecksum)
            throws MaileonException {
        Long id = contact.getId();
        if (null == id) {
            throw new MaileonClientException("contact id required");
        }
        QueryParameters params = new QueryParameters("id", id);
        params.add("checksum", checksum);
        Permission permission = contact.getPermission();
        if (permission != null) {
            params.add("permission", permission.getCode());
        }
        params.add("triggerdoi", triggerDoi);
        if (src != null) {
            params.add("src", src);
        }
        if (pageKey != null) {
            params.add("page_key", pageKey);
        }
        if (doiMailingKey != null) {
            params.add("doimailing", doiMailingKey);
        }
        if (ignoreChecksum) {
            params.add("ignore_checksum", true);
        }
        put("contacts/contact", params, ContactAdaptor.toXml(contact, false).asXML());
    }

    /**
     * Updates a contact using particular settings.
     *
     * @param email the email of contact
     * @param contact the {@link Contact} to update
     * @throws MaileonException
     */
    public void updateContactByEmail(String email, Contact contact)
            throws MaileonException {
        if (null == email || !email.contains("@")) {
            throw new MaileonClientException("email is required");
        }
        QueryParameters params = new QueryParameters();
        Permission permission = contact.getPermission();
        if (permission != null) {
            params.add("permission", permission.getCode());
        }

        put("contacts/" + encodePath(email), params, ContactAdaptor.toXml(contact, false).asXML());
    }

    /**
     * Removes a contact completely given an email address. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-contacts-by-email/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param email the corresponding email address of the contact.
     * @throws MaileonException
     */
    public void deleteContactsByEmail(String email) throws MaileonException {
        if (email == null || !email.contains("@")) {
            throw new MaileonNotFoundException("contact with email " + email + " isn't found");
        }
        delete("contacts/email/" + encodePath(email));
    }

    /**
     * Removes a contact completely given an external contact id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-contacts-by-externalid/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param externalId the corresponding external contact id.
     * @throws MaileonException
     */
    public void deleteContactsByExternalId(String externalId) throws MaileonException {
        if (externalId == null || externalId.isEmpty()) {
            throw new MaileonNotFoundException("contact with externalId " + externalId + " isn't found");
        }
        delete("contacts/externalid/" + encodePath(externalId));
    }

    /**
     * Removes a contact completely given a maileon contact id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-contact-by-maileon-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param contactId the maileon contact id
     * @throws MaileonException
     */
    public void deleteContactsByMaileonId(long contactId) throws MaileonException {
        if (contactId <= 0) {
            throw new MaileonNotFoundException("contact with id " + contactId + " isn't found");
        }
        QueryParameters params = new QueryParameters("id", contactId);
        delete("contacts/contact", params);
    }

    /**
     * Standard field parameters.
     *
     * @param standardFields the standard fields
     * @return the query parameters
     */
    private static QueryParameters standardFieldParameters(List<StandardContactField> standardFields) {
        QueryParameters parameters = new QueryParameters();
        if (standardFields != null) {
            for (StandardContactField scf : standardFields) {
                if (scf != null) {
                    parameters.add("standard_field", scf.getName());
                }
            }
        }
        return parameters;
    }

    /**
     * Custom field parameters.
     *
     * @param customFields the custom fields
     * @return the query parameters
     */
    private static QueryParameters customFieldParameters(List<String> customFields) {
        QueryParameters parameters = new QueryParameters();
        if (customFields != null) {
            for (String cf : customFields) {
                if (cf != null) {
                    parameters.add("custom_field", cf);
                }
            }
        }
        return parameters;
    }

    /**
     * Returns the contacts having the provided email address. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-contacts-by-email/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param email the email of the searched contacts.
     * @param standardFields standard properties of the contact as list of {@link StandardContactField}.
     * @param customFields custom properties of the contact.
     * @return a list of the found {@link Contact}.
     * @throws MaileonException
     */
    public List<Contact> getContactsByEmail(String email, List<StandardContactField> standardFields, List<String> customFields) throws MaileonException {
        if (email == null || !email.contains("@")) {
            return new ArrayList<>();
        }
        QueryParameters params = new QueryParameters();
        params.add(standardFieldParameters(standardFields));
        params.add(customFieldParameters(customFields));
        ResponseWrapper resp = get("contacts/emails/" + encodePath(email), params);
        Element xml = resp.getEntityAsXml();
        return ContactAdaptor.fromXml(xml.elements("contact"));
    }

    /**
     * Unsubscribes the contact(s) with the given email address in the account. See
     * <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/unsubscribe-contacts-by-email/?lang=en">Maileon API documentation</a>.
     *
     * @param email of contacts to unsubsribe.
     * @throws MaileonException
     */
    public void unsubscribeContactsByEmail(String email) throws MaileonException {
        if (email == null || !email.contains("@")) {
            throw new MaileonNotFoundException("contact with email " + email + " isn't found");
        }
        delete("contacts/email/" + encodePath(email) + "/unsubscribe");
    }

    /**
     * Unsubscribes a contact using the maileon contact id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/unsubscribe-contact-by-maileon-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param contactId the given maileon contact id.
     * @throws MaileonException
     */
    public void unsubscribeContactById(int contactId) throws MaileonException {
        if (contactId <= 0) {
            throw new MaileonNotFoundException("contact with id " + contactId + " isn't found");
        }
        QueryParameters params = new QueryParameters("id", contactId);
        delete("contacts/contact/unsubscribe", params);
    }

    /**
     * Unsubscribes a contact using an external contact id. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/unsubscribe-contacts-by-external-id/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param externalId the provided external contact id.
     * @throws MaileonException
     */
    public void unsubscribeContactByExternalId(String externalId) throws MaileonException {
        if (externalId == null || externalId.isEmpty()) {
            throw new MaileonNotFoundException("contact with externalId " + externalId + " isn't found");
        }
        delete("contacts/externalid/" + externalId + "/unsubscribe");
    }
}
