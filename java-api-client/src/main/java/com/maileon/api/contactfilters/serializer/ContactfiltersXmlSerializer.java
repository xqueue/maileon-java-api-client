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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *    De-/serializes contact filters
 *
 */
public class ContactfiltersXmlSerializer implements MessageBodyReader<List<Contactfilter>>, MessageBodyWriter<List<Contactfilter>> {

    public static final String XML_CONTACT_FILTER = "contactfilter";
    public static final String XML_CONTACT_FILTERS = "contactfilters";

    @Override
    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return genericType == Contactfilter.class;
    }

    @Override
    public List<Contactfilter> readFrom(Class<List<Contactfilter>> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            Element filters = XmlUtils.parseXmlDocument(entityStream);
            return deserialize(filters);
        } catch (Throwable e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return genericType == Contactfilter.class;
    }

    @Override
    public void writeTo(List<Contactfilter> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        if (t == null) {
            throw new WebApplicationException();
        }

        Element root;
        try {
                    root = serialize(t);
            XMLWriter writer = new XMLWriter(entityStream, OutputFormat.createPrettyPrint());
            writer.write(root);
            writer.close();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }


    }

    /**
     * Deserialize a list of filters
     *
     * @param filters
     * @return
     * @throws Exception
     */
    public static List<Contactfilter> deserialize(Element filters) throws Exception {
        List<Contactfilter> result = new ArrayList<>();
        if (XML_CONTACT_FILTERS.equals(filters.getName())) {
            // Set of elements/filters
            @SuppressWarnings("unchecked")
            Iterator<Element> iter = filters.elementIterator(XML_CONTACT_FILTER);
            while (iter.hasNext()) {
                Element filter = iter.next();
                result.add(ContactfilterXmlSerializer.deserializeSingleElement(filter));
            }
        } else if (XML_CONTACT_FILTER.equals(filters.getName())) {
            // Single element/filter
            result.add(ContactfilterXmlSerializer.deserializeSingleElement(filters));
        } else throw new Exception(String.format("Root element must be either named '%s' or '%s' but is named: '%s'", XML_CONTACT_FILTERS, XML_CONTACT_FILTER, filters.getName()));

        return result;
    }

    /**
     * Serialize a list of filters
     *
     * @param filters
     * @return
     * @throws Exception
     */
    public static Element serialize(List<Contactfilter> filters) throws Exception {
        Element root = DocumentHelper.createElement(XML_CONTACT_FILTERS);

        for (Contactfilter filter:filters) {
            root.add(ContactfilterXmlSerializer.serialize(filter));
        }

        return root;
    }

    @Override
    public long getSize(List<Contactfilter> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
