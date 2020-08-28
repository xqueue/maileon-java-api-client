package com.maileon.api.transactions.serializer;

import com.maileon.api.XmlUtils;
import com.maileon.api.transactions.Attribute;
import com.maileon.api.transactions.AttributeType;
import com.maileon.api.transactions.TransactionType;
import com.maileon.api.utils.BooleanHelper;
import com.maileon.api.utils.MimeTypes;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Provider
@Consumes(MimeTypes.MAILEON_API_XML)
@Produces(MimeTypes.MAILEON_API_XML)
public class TransactionTypeXmlSerializer implements MessageBodyReader<TransactionType>, MessageBodyWriter<TransactionType> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == TransactionType.class && mediaType.getSubtype().endsWith("xml");
    }

    @Override
    public TransactionType readFrom(Class<TransactionType> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            Element element = XmlUtils.parseXmlDocument(entityStream);
            return deserialize(element);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

    public static TransactionType deserialize(Element element) {
        TransactionType result = new TransactionType();
        if (!element.getName().equals("transaction_type")) {
            throw new IllegalArgumentException("Expected 'transaction_type' element.");
        }
        result.setId(maybeParseLong(element.elementText("id")));
        String elementText = element.elementText("created");
        if (elementText != null && !elementText.isEmpty()) {
            result.setCreated(new Date(Long.parseLong(elementText)));
        }
        result.setName(element.elementText("name"));
        result.setDescription(element.elementText("description"));
        result.setArchivingDuration(maybeParseInteger(element.elementText("archivingDuration")));
        result.setStoreOnly("true".equals(element.elementText("storeOnly")));
        result.setAttributes(deserializeAttributes(element.element("attributes")));

        return result;
    }

    private static Long maybeParseLong(String s) {
        if (s == null) {
            return null;
        }
        return Long.parseLong(s);
    }

    private static Integer maybeParseInteger(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return Integer.parseInt(s);
    }

    @SuppressWarnings("unchecked")
    private static List<Attribute> deserializeAttributes(Element element) {
        List<Attribute> result = new ArrayList<>();
        for (Iterator<Element> iterator = element.elementIterator(); iterator.hasNext();) {
            Element attributeElement = (Element) iterator.next();
            if (!attributeElement.getName().equals("attribute")) {
                throw new IllegalArgumentException("Expected 'attribute' element.");
            }
            Attribute attribute = new Attribute();
            attribute.setId(maybeParseLong(attributeElement.elementText("id")));
            attribute.setName(attributeElement.elementText("name"));
            attribute.setDescription(attributeElement.elementText("description"));
            attribute.setType(AttributeType.valueOf(attributeElement.elementText("type").toUpperCase()));
            attribute.setRequired(BooleanHelper.parseBoolean(attributeElement.elementText("required")));
            result.add(attribute);
        }
        return result;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == TransactionType.class && mediaType.getSubtype().endsWith("xml");
    }

    @Override
    public void writeTo(TransactionType t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        IOUtils.write(serialize(t).asXML(), entityStream, StandardCharsets.UTF_8);
    }

    public static Element serialize(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        // perform preliminary check. The full requirements can be found in the documentation.
        if (type.getName() == null || type.getName().trim().length() < 3) {
            throw new IllegalArgumentException("the name of type is too short");
        }

        Element result = DocumentHelper.createElement("transaction_type");
        XmlUtils.addChildElement(result, "name", type.getName());
        XmlUtils.addChildElement(result, "description", type.getDescription());
        if (type.getArchivingDuration() != null) {
            XmlUtils.addChildElement(result, "archivingDuration", type.getArchivingDuration().toString());
        }
        if (type.isStoreOnly()) {
            XmlUtils.addChildElement(result, "storeOnly", "true");
        }
        Element attributesElement = XmlUtils.addChildElement(result, "attributes");
        if (type.getAttributes() != null) {
            for (Attribute attribute : type.getAttributes()) {
                validateAttribute(attribute);
                Element attributeElement = XmlUtils.addChildElement(attributesElement, "attribute");
                XmlUtils.addChildElement(attributeElement, "name", attribute.getName());
                XmlUtils.addChildElement(attributeElement, "description", attribute.getDescription());
                XmlUtils.addChildElement(attributeElement, "type", attribute.getType().toString());
                XmlUtils.addChildElement(attributeElement, "required", Boolean.toString(attribute.isRequired()));
            }
        }
        return result;
    }

    /**
     * This function performs only preliminary validation of attribute's name. The maximal length of attribute's name isn't checked for example.
     *
     * The full requirements can be read in the documentation.
     */
    private static void validateAttribute(Attribute attribute) throws IllegalArgumentException {
        if (attribute == null) {
            throw new IllegalArgumentException("attribute is null");
        }
        if (attribute.getName() == null) {
            throw new IllegalArgumentException("attribute's name is null");
        }
        String name = attribute.getName().trim();
        if (name.length() < 3) {
            throw new IllegalArgumentException("attribute's name is too short");
        }
        if (attribute.getType() == null) {
            throw new IllegalArgumentException("attribute's type isn't set");
        }
    }

    @Override
    public long getSize(TransactionType t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
