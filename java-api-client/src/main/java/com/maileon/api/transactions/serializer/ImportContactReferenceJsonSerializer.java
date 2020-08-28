package com.maileon.api.transactions.serializer;

import com.maileon.api.MaileonClientException;
import com.maileon.api.transactions.ImportContactReference;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ImportContactReferenceJsonSerializer implements MessageBodyReader<ImportContactReference>, MessageBodyWriter<ImportContactReference> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ImportContactReference.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public ImportContactReference readFrom(Class<ImportContactReference> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
            return deserialize(object);
        } catch (ParseException e) {
            throw new WebApplicationException(e);
        }
    }

    public static ImportContactReference deserialize(JSONObject contactObject) {
        ImportContactReference result = new ImportContactReference();
        result.setId((Long) contactObject.get("id"));
        result.setEmail((String) contactObject.get("email"));
        result.setExternalId((String) contactObject.get("external_id"));
        result.setPermission((Integer) contactObject.get("permission"));
        result.setErrorField((String) contactObject.get("error_field"));
        return result;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ImportContactReference.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public void writeTo(ImportContactReference t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        IOUtils.write(serialize(t).toJSONString(), entityStream, StandardCharsets.UTF_8);
    }

    public static JSONObject serialize(ImportContactReference contact) {
        validateContact(contact);
        JSONObject result = new JSONObject();
        maybePut("id", contact.getId(), result);
        maybePut("email", contact.getEmail(), result);
        maybePut("external_id", contact.getExternalId(), result);
        maybePut("permission", contact.getPermission(), result);
        return result;
    }

    private static void validateContact(ImportContactReference contact) {
        if (contact.getId() == null && contact.getEmail() == null && contact.getExternalId() == null) {
            throw new MaileonClientException("When using the import statement you must provide one of the following parameters: id, email, external_id.");
        }

        if (contact.getPermission() == null) {
            throw new MaileonClientException("When using the import statement you must provide a permission.");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void maybePut(String key, T value, JSONObject object) {
        if (value != null) {
            object.put(key, value);
        }
    }

    @Override
    public long getSize(ImportContactReference t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
