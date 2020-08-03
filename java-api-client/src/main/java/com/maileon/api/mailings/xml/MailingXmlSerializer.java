package com.maileon.api.mailings.xml;

import com.maileon.api.XmlUtils;
import com.maileon.api.mailings.Mailing;
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
import java.util.List;

/**
 * De-/serializes a single mailing
 *
 */
public class MailingXmlSerializer implements MessageBodyReader<Mailing>, MessageBodyWriter<Mailing> {

    public static final String XML_ID = "id";
    public static final String XML_MAILING = "mailing";
    public static final String XML_MAILING_FIELD = "field";
    public static final String XML_MAILING_FIELDS = "fields";

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == Mailing.class;
    }

    @Override
    public Mailing readFrom(Class<Mailing> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        try {
            Element element = XmlUtils.parseXmlDocument(in);
            return deserializeSingleElement(element);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == Mailing.class;
    }

    @Override
    public void writeTo(Mailing t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException, WebApplicationException {
        if (t == null) {
            throw new WebApplicationException(new Exception("Unable to read passed mailing while serializing (mailing is null)"));
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
     * Deserialize a mailing
     *
     * @param element
     * @return
     * @throws Exception
     */
    public static Mailing deserializeSingleElement(Element element) throws Exception {

        if (XML_MAILING.equals(element.getName())) {

            Mailing mailing;

            // ID
            Element id = element.element(XML_ID);
            if (id != null) {
                try {
                    mailing = new Mailing(Long.parseLong(id.getText()));
                } catch (Exception e) {
                    throw new NumberFormatException("Not a valid ID: " + id.getText());
                }
            } else {
                return new Mailing(-1);
            }

            Element fieldElement = element.element(XML_MAILING_FIELDS);
            if (fieldElement != null) {
                List<Element> fields = fieldElement.elements(XML_MAILING_FIELD);
                for (Element field : fields) {
                    Element name = field.element("name");
                    Element value = field.element("value");
                    mailing.getFields().put(name.getText(), value.getText());
                }
            }

            return mailing;

        } else {
            throw new Exception(String.format("Root element of a single mailing must be named '%s' but is named: '%s'", XML_MAILING, element.getName()));
        }
    }

    /**
     * Serialize a single filter
     *
     * @param element
     * @return
     * @throws Exception
     */
    public static Element serialize(Mailing element) throws Exception {
        Element root = DocumentHelper.createElement(XML_MAILING);

        appendNullableChildElement(root, XML_ID, element.getId());

        if (element.getFields() != null) {
            Element fieldsElement = DocumentHelper.createElement(XML_MAILING_FIELDS);
            root.add(fieldsElement);
            for (String field : element.getFields().keySet()) {
                addFieldElement(fieldsElement, field, element.getFields().get(field));
            }
        }

        return root;
    }

    protected static Element addFieldElement(Element parent, String name, String value) {
        Element field = DocumentHelper.createElement("field");
        parent.add(field);
        Element nameE = DocumentHelper.createElement("name");
        field.add(nameE);
        nameE.setText(name);
        appendNullableChildElement(field, "value", value);
        return field;
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
    public long getSize(Mailing t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
