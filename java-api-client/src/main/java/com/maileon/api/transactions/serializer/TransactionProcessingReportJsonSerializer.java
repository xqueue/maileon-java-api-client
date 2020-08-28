package com.maileon.api.transactions.serializer;

import com.maileon.api.transactions.ContactReference;
import com.maileon.api.transactions.TransactionProcessingReport;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TransactionProcessingReportJsonSerializer implements MessageBodyReader<TransactionProcessingReport> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == TransactionProcessingReport.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public TransactionProcessingReport readFrom(Class<TransactionProcessingReport> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
            return deserialize(object);
        } catch (ParseException e) {
            throw new WebApplicationException(e);
        }
    }

    public static TransactionProcessingReport deserialize(JSONObject reportObject) {
        TransactionProcessingReport result = new TransactionProcessingReport();
        ContactReference contact = new ContactReferenceJsonSerializer().deserialize((JSONObject) reportObject.get("contact"));
        result.setContact(contact);
        result.setQueued((Boolean) reportObject.get("queued"));
        result.setMessage((String) reportObject.get("message"));
        result.setTransactionId((String) reportObject.get("transaction_id"));
        return result;
    }

}
