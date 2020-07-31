package com.maileon.api.contacts;

import com.maileon.api.*;

import java.util.List;

/**
 * The <code>MaileonContactsService</code> client sends operational requests to the <code>ContactsResource</code>.
 *
 */
public class MaileonContactFieldsService extends AbstractMaileonService {

    public static final String SERVICE = "MAILEON CONTACT FIELDS";

    /**
     * Constructs a <code>MaileonContactFieldsService</code>.
     *
     * @param config Maileon API-Configuration.
     */
    public MaileonContactFieldsService(MaileonConfiguration config) {
        super(config, SERVICE);
    }

    /**
     * Removes the values of a specified standard contact property for all contacts. See
     * <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-standard-field-values/?lang=en">Maileon API documentation</a>.
     *
     * @param field the required standard field.
     * @throws MaileonException
     */
    public void deleteStandardFieldValues(StandardContactField field) throws MaileonException {
        delete("contacts/fields/standard/" + encodePath(field.getName()) + "/values");
    }

    /**
     * Removes the values of a specified custom contact property for all contacts. See
     * <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-custom-field-values/?lang=en">Maileon API documentation</a>.
     *
     * @param field the required custom field
     * @throws MaileonException
     */
    public void deleteCustomFieldValues(String field) throws MaileonException {
        delete("contacts/fields/custom/" + encodePath(field) + "/values");
    }

    /**
     * Deletes the custom contact field with the provided name. Note that all the values of the field get auotmatically deleted by this call.
     *
     * <p>
     * <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/delete-custom-field/?lang=en" target="_blank">Maileon API documentation</a></p>
     *
     * @param field name of contact field
     * @throws MaileonException
     */
    public void deleteCustomField(String field) throws MaileonException {
        if (field == null || field.isEmpty()) {
            throw new MaileonNotFoundException("custom field " + field + " isn't found");
        }
        delete("contacts/fields/custom/" + encodePath(field));
    }

    /**
     * Gets all cutom fields defined in the account. See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/get-custom-fields/?lang=en">Maileon API documentation</a>.
     *
     * @return list of {@link CustomContactFieldDefinition}.
     * @throws MaileonException
     */
    public List<CustomContactFieldDefinition> getCustomFields() throws MaileonException {
        ResponseWrapper resp = get("contacts/fields/custom");
        return CustomContactFieldDefinitionAdaptor.fromXml(resp.getEntityAsXml());
    }

    /**
     * Creates a custom contact field with the provided name and data type.See <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/create-custom-field/?lang=en">Maileon API
     * documentation</a>.
     *
     * @param name name of contact field
     * @param type string / integer / float / date / boolean
     * @throws MaileonException
     */
    public void createCustomField(String name, String type) throws MaileonException {
        post("contacts/fields/custom/" + encodePath(name), new QueryParameters("type", type), null);
    }

    /**
     * Renames a custom contact field.The data type and the recorded values for the contacts remain unchanged. See
     * <a href="https://dev.maileon.com/api/rest-api-1-0/contacts/rename-custom-field/?lang=en">Maileon API documentation</a>.
     *
     * @param oldName old name of contact field.
     * @param newName new name of contact field.
     * @throws MaileonException
     */
    public void renameCustomField(String oldName, String newName) throws MaileonException {
        put("contacts/fields/custom/" + encodePath(oldName) + "/" + encodePath(newName), null);
    }
}
