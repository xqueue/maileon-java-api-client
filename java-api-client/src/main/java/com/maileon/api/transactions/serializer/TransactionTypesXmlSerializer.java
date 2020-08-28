package com.maileon.api.transactions.serializer;

import com.maileon.api.XmlUtils;
import com.maileon.api.transactions.TransactionType;
import org.dom4j.Element;

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


public class TransactionTypesXmlSerializer implements MessageBodyReader<List<TransactionType>>, MessageBodyWriter<List<TransactionType>> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    public List<TransactionType> readFrom(Class<List<TransactionType>> type, Type genericType,
            Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException, WebApplicationException {
        try {
            Element element = XmlUtils.parseXmlDocument(entityStream);
            return deserialize(element);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<TransactionType> deserialize(final Element element) {
        if (!element.getName().equals("transaction_types")) {
            throw new IllegalArgumentException("Expected 'transaction_types' element.");
        }
        List<TransactionType> result = new ArrayList<>();
        for (Iterator<Element> iterator = element.elementIterator(); iterator.hasNext();) {
            Element transactionTypeElement = (Element) iterator.next();
            result.add(TransactionTypeXmlSerializer.deserialize(transactionTypeElement));
        }
        return result;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    @Override
    public void writeTo(List<TransactionType> t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public long getSize(List<TransactionType> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
