package com.maileon.api.contactfilters.serializer;


import com.maileon.api.XmlUtils;
import com.maileon.api.contactfilters.Contactfilter;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


/**
 *    De-/serializes a single contact filter
 *
 */
public class ContactfilterXmlSerializer implements MessageBodyReader<Contactfilter>, MessageBodyWriter<Contactfilter> {

    public static final String XML_ID = "id";
    public static final String XML_NAME = "name";
    public static final String XML_AUTHOR = "author";
    public static final String XML_STATE = "state";
    public static final String XML_CREATED = "created";
    public static final String XML_COUNT_CONTACTS = "count_contacts";
    public static final String XML_COUNT_RULES = "count_rules";
    public static final String XML_CONTACT_FILTER = "contactfilter";
    public static final String XML_CONTACT_FILTERS = "contactfilters";

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == Contactfilter.class;
    }

    @Override
    public Contactfilter readFrom(Class<Contactfilter> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        try {
            Element filter = XmlUtils.parseXmlDocument(in);
            return deserializeSingleElement(filter);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == Contactfilter.class;
    }

    @Override
    public void writeTo(Contactfilter t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException, WebApplicationException {
        if (t == null) {
            throw new WebApplicationException(new Exception("Unable to read passed contact filter while serializing: " + t));
        }

        Element root;
        try {
            root = serialize(t);
            XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
            writer.write(root);
            writer.close();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }


    }

    /**
     * Deserialize a single filter
     *
     * @param filter
     * @return
     * @throws Exception
     */
     public static Contactfilter deserializeSingleElement(Element filter) throws Exception {

         if (XML_CONTACT_FILTER.equals(filter.getName())) {
             Contactfilter cf = new Contactfilter();

             // ID
             Element id = filter.element(XML_ID);
             if (id != null) {
                 try {
                     cf.setId(Long.parseLong(id.getText()));
                } catch (Exception e) {
                    throw new NumberFormatException("Not a valid ID: " + id.getText());
                }
             }

             // Name
             Element name = filter.element(XML_NAME);
             if (name != null) {
                 cf.setName(name.getText());
             }

             // Author
             Element author = filter.element(XML_AUTHOR);
             if (author != null) {
                 cf.setAuthor(author.getText());
             }

             // State
             Element state = filter.element(XML_STATE);
             if (state != null) {
                 cf.setStatus(state.getText());
             }

             // Created
             Element created = filter.element(XML_CREATED);
             if (created != null) {
                 try {
                     cf.setCreated(created.getText());
                } catch (Exception e) {
                    throw new NumberFormatException("Not a valid date: " + created);
                }
             }

             // Count Contacts
             Element count_contacts = filter.element(XML_COUNT_CONTACTS);
             if (count_contacts != null) {
                 try {
                     cf.setCountContacts(Integer.parseInt(count_contacts.getText()));
                } catch (Exception e) {
                    throw new NumberFormatException("Not a int number for counted contacts: " + count_contacts.getText());
                }
             }

             // Count Rules
             Element count_rules = filter.element(XML_COUNT_RULES);
             if (count_rules != null) {
                 try {
                     cf.setCountRules(Integer.parseInt(count_rules.getText()));
                } catch (Exception e) {
                    throw new NumberFormatException("Not a int number for counted rules: " + count_rules.getText());
                }
             }

             return cf;

         } else throw new Exception(String.format("Root element of a single contact filter must be named '%s' but is named: '%s'", XML_CONTACT_FILTERS, XML_CONTACT_FILTER, filter.getName()));
    }

    /**
     * Serialize a single filter
     *
     * @param filter
     * @return
     * @throws Exception
     */
    public static Element serialize(Contactfilter filter) throws Exception {
        Element root = DocumentHelper.createElement(XML_CONTACT_FILTER);

        appendNullableChildElement(root, XML_ID, filter.getId());
        appendNullableChildElement(root, XML_NAME, filter.getName());
        appendNullableChildElement(root, XML_AUTHOR, filter.getAuthor());
        appendNullableChildElement(root, XML_STATE, filter.getStatus());
        appendNullableChildElement(root, XML_CREATED, filter.getCreated());
        appendNullableChildElement(root, XML_COUNT_CONTACTS, filter.getCountContacts());
        appendNullableChildElement(root, XML_COUNT_RULES, filter.getCountRules());

        return root;
    }

    protected static Element appendNullableChildElement(Element parent, String childName, Object value) {
        Element child = DocumentHelper.createElement(childName);
        if (null == value) {
            child.add(DocumentHelper.createAttribute(child, "nil", "true"));
        } else {
            child.setText(value.toString());
        }
        parent.add(child);
        return child;
    }

    @Override
    public long getSize(Contactfilter t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }


}
