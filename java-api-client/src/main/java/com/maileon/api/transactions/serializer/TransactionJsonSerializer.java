package com.maileon.api.transactions.serializer;

import com.maileon.api.transactions.Attachment;
import com.maileon.api.transactions.Transaction;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
import java.util.List;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TransactionJsonSerializer implements MessageBodyReader<Transaction>, MessageBodyWriter<Transaction> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    @Override
    public Transaction readFrom(Class<Transaction> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Transaction.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public void writeTo(Transaction t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        IOUtils.write(serialize(t).toJSONString(), entityStream, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public static JSONObject serialize(Transaction transaction) {
        JSONObject result = new JSONObject();
        if (transaction.getType() > 0) {
            result.put("type", transaction.getType());
        }
        if (transaction.getTypeName() != null) {
            result.put("typeName", transaction.getTypeName());
        }

        if (transaction.getContact() != null) {
            JSONObject contact = ContactReferenceJsonSerializer.serialize(transaction.getContact());
            result.put("contact", contact);
        }
        if (transaction.getImportReference() != null) {
            JSONObject contact = ImportContactReferenceJsonSerializer.serialize(transaction.getImportReference());
            JSONObject importContact = new JSONObject();
            importContact.put("contact", contact);
            result.put("import", importContact);
        }
        result.put("content", transaction.getContent());
        if (transaction.getAttachments() != null && !transaction.getAttachments().isEmpty()) {
            result.put("attachments", serialize(transaction.getAttachments()));
        }
        return result;
    }

    private static JSONArray serialize(List<Attachment> attachments) {
        JSONArray result = new JSONArray();
        for (Attachment attachment : attachments) {
            result.add(serialize(attachment));
        }
        return result;
    }

    private static JSONObject serialize(Attachment attachment) {
        JSONObject result = new JSONObject();
        result.put("filename", attachment.getFilename());
        result.put("mimetype", attachment.getMimetype());
        result.put("data", attachment.getData());
        return result;
    }

    @Override
    public long getSize(Transaction t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
