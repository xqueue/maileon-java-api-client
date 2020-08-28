package com.maileon.api.transactions.serializer;

import com.maileon.api.transactions.Transaction;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsJsonSerializer implements MessageBodyWriter<List<Transaction>> {


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Transaction.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public void writeTo(List<Transaction> transactions, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        IOUtils.write(serialize(transactions).toJSONString(), entityStream, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public static JSONArray serialize(List<Transaction> transactions) {
        JSONArray result = new JSONArray();
        for (Transaction transaction : transactions) {
            result.add(TransactionJsonSerializer.serialize(transaction));
        }
        return result;
    }

    @Override
    public long getSize(List<Transaction> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
