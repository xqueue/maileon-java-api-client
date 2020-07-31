package com.maileon.api.contacts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.maileon.api.XmlUtils;
import com.maileon.api.utils.BooleanHelper;
import java.util.Map;

/**
 * The Class ContactAdaptor.
 *
 */
class ContactAdaptor {

    public static List<Contact> fromXml(List<Element> l) {
        List<Contact> contacts = new ArrayList<>();
        for (Element e : l) {
            contacts.add(fromXml(e));
        }
        return contacts;
    }

    static Contact fromXml(Element e) {
        return parseContactProperties(e);
    }

    static Element toXml(List<Contact> contacts) {
        Element contactsE = DocumentHelper.createElement("contacts");
        for (Contact contact : contacts) {
            contactsE.add(toXml(contact, true));
        }
        return contactsE;
    }

    /**
     * To xml.
     *
     * @param contact the contact
     * @param withId if true, then id contact will be included in xml
     * @return the element
     */
    static Element toXml(Contact contact, boolean withId) {
        Element contactE = DocumentHelper.createElement("contact");
        // email
        if (contact.getEmail() != null) {
            Element emailE = DocumentHelper.createElement("email");
            emailE.setText(contact.getEmail());
            contactE.add(emailE);
        }
        if (contact.getExternalId() != null) {
            Element externalIdE = DocumentHelper.createElement("external_id");
            externalIdE.setText(contact.getExternalId());
            contactE.add(externalIdE);
        }
        if (withId && contact.getId() != null && contact.getId() > 0L) { // update contact by id returns error if contactId is available in xml
            Element idE = DocumentHelper.createElement("id");
            idE.setText(Long.toString(contact.getId()));
            contactE.add(idE);
        }
        // standard fields
        Element sfE = DocumentHelper.createElement("standard_fields");
        for (Map.Entry<StandardContactField, String> scf : contact.getStandardFields().entrySet()) {
            sfE.add(new Field(scf.getKey().getName(), scf.getValue()).asElement());
        }
        contactE.add(sfE);
        // custom fields
        Element cfE = DocumentHelper.createElement("custom_fields");
        for (Map.Entry<String, String> cf : contact.getCustomFields().entrySet()) {
            cfE.add(new Field(cf.getKey(), cf.getValue()).asElement());
        }
        contactE.add(cfE);
        return contactE;
    }

    /**
     * Parses the fields.
     *
     * @param el the el
     * @return the list
     */
    private static List<Field> parseFields(List<Element> el) {
        List<Field> l = new ArrayList<>(el.size());
        for (Element e : el) {
            l.add(new Field(e));
        }
        return l;
    }

    private static Contact parseContactProperties(Element e) {
        return parseContactProperties(new Contact(), e);
    }

    private static Contact parseContactProperties(Contact contact, Element e) {
        String idStr = e.elementText("id");
        if (idStr != null && !idStr.isEmpty()) {
            contact.setId(Long.parseLong(idStr));
        }
        String permissionStr = e.elementText("permission");
        if (permissionStr != null) {
            contact.setPermission(Permission.get(Integer.parseInt(permissionStr)));
        }
        contact.setEmail(e.elementText("email"));
        contact.setExternalId(e.elementText("external_id"));

        String updatedS = e.elementText("updated");
        if (updatedS != null && !updatedS.isEmpty()) {
            Timestamp updatedTS = Timestamp.valueOf(updatedS);
            contact.setUpdated(updatedTS);
        }

        String createdS = e.elementText("created");
        if (createdS != null && !createdS.isEmpty()) {
            Timestamp createdTS = Timestamp.valueOf(createdS);
            contact.setCreated(createdTS);
        }

        String anonymous = e.attributeValue("anonymous");
        if (anonymous != null) {
            contact.setAnonymous(BooleanHelper.parseBoolean(anonymous));
        }
        Element standardFields = e.element("standard_fields");
        if (standardFields != null) {
            List<Field> l = parseFields(standardFields.elements("field"));
            for (Field f : l) {
                contact.getStandardFields().put(StandardContactField.parse(f.getName()), f.getValue());
            }
        }
        Element customFields = e.element("custom_fields");
        if (customFields != null) {
            List<Field> l = parseFields(customFields.elements("field"));
            for (Field f : l) {
                contact.getCustomFields().put(f.getName(), f.getValue());
            }
        }
        return contact;
    }

    /**
     * The Class Field.
     */
    private static class Field {

        /**
         * The name.
         */
        private final String name;

        /**
         * The value.
         */
        private final String value;

        /**
         * Instantiates a new field.
         *
         * @param name the name
         * @param value the value
         */
        public Field(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Instantiates a new field.
         *
         * @param e the e
         */
        public Field(Element e) {
            name = e.elementText("name");
            if (!XmlUtils.isNil(e.element("value"))) {
                value = e.elementText("value");
            } else {
                value = null;
            }
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * As element.
         *
         * @return the element
         */
        public Element asElement() {
            Element e = DocumentHelper.createElement("field");
            Element nameE = DocumentHelper.createElement("name");
            e.add(nameE);
            nameE.setText(name);
            Element valueE = DocumentHelper.createElement("value");
            e.add(valueE);
            if (value == null) {
                valueE.add(DocumentHelper.createAttribute(valueE, "nil", "true"));
            } else {
                valueE.setText(value);
            }
            return e;
        }
    }
}
