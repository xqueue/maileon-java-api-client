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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * De-/serializes mailings
 *
 */
public class MailingsXmlSerializer implements MessageBodyReader<List<Mailing>>, MessageBodyWriter<List<Mailing>> {

    public static final String XML_MAILINGS = "mailings";

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == List.class;
    }

    @Override
    public List<Mailing> readFrom(Class<List<Mailing>> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        try {
            Element filters = XmlUtils.parseXmlDocument(in);
            return deserialize(filters);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type1 == List.class;
    }

    @Override
    public void writeTo(List<Mailing> t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException, WebApplicationException {
        if (t == null) {
            throw new WebApplicationException();
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
     * Deserialize a list of mailings
     *
     * @param elements
     * @return
     * @throws Exception
     */
    public static List<Mailing> deserialize(Element elements) throws Exception {
        List<Mailing> result = new ArrayList<>();
        if (XML_MAILINGS.equals(elements.getName())) {
            // Set of elements/filters
            @SuppressWarnings("unchecked")
            Iterator<Element> iter = elements.elementIterator(MailingXmlSerializer.XML_MAILING);
            while (iter.hasNext()) {
                Element element = iter.next();
                result.add(MailingXmlSerializer.deserializeSingleElement(element));
            }
        } else if (MailingXmlSerializer.XML_MAILING.equals(elements.getName())) {
            // Single element/filter
            result.add(MailingXmlSerializer.deserializeSingleElement(elements));
        } else {
            throw new Exception(String.format("Root element must be either named '%s' or '%s' but is named: '%s'", XML_MAILINGS, MailingXmlSerializer.XML_MAILING, elements.getName()));
        }

        return result;
    }

    /**
     * Serialize a list of mailings
     *
     * @param mailings
     * @return
     * @throws Exception
     */
    public static Element serialize(List<Mailing> mailings) throws Exception {
        Element root = DocumentHelper.createElement(XML_MAILINGS);

        for (Mailing mailing : mailings) {
            root.add(MailingXmlSerializer.serialize(mailing));
        }

        return root;
    }

    @Override
    public long getSize(List<Mailing> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
