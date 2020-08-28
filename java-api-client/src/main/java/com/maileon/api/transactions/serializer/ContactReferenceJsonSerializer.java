package com.maileon.api.transactions.serializer;

import com.maileon.api.transactions.ContactReference;
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
public class ContactReferenceJsonSerializer implements MessageBodyReader<ContactReference>, MessageBodyWriter<ContactReference> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ContactReference.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public ContactReference readFrom(Class<ContactReference> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
            return deserialize(object);
        } catch (ParseException e) {
            throw new WebApplicationException(e);
        }
    }

    public ContactReference deserialize(JSONObject contactObject) {
        ContactReference result = new ContactReference();
        result.setId((Long) contactObject.get("id"));
        result.setEmail((String) contactObject.get("email"));
        result.setExternalId((String) contactObject.get("external_id"));
        return result;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ContactReference.class && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public void writeTo(ContactReference t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        IOUtils.write(serialize(t).toJSONString(), entityStream, StandardCharsets.UTF_8);
    }

    public static JSONObject serialize(ContactReference contact) {
        if (contact == null) {
            return null;
        }
        JSONObject result = new JSONObject();
        maybePut("id", contact.getId(), result);
        maybePut("email", contact.getEmail(), result);
        maybePut("external_id", contact.getExternalId(), result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> void maybePut(String key, T value, JSONObject object) {
        if (value != null) {
            object.put(key, value);
        }
    }

    @Override
    public long getSize(ContactReference t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
