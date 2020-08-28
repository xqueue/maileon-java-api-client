package com.maileon.api.transactions.serializer;

import com.maileon.api.transactions.TransactionProcessingReport;
import org.json.simple.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TransactionProcessingReportsJsonSerializer implements MessageBodyReader<List<TransactionProcessingReport>> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return List.class.isAssignableFrom(type)
                && mediaType.getSubtype().endsWith("json");
    }

    @Override
    public List<TransactionProcessingReport> readFrom(Class<List<TransactionProcessingReport>> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JSONArray reports = (JSONArray) new JSONParser().parse(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
            return deserialize(reports);
        } catch (ParseException e) {
            throw new WebApplicationException(e);
        }
    }

    public static List<TransactionProcessingReport> deserialize(String jsonString) {
        try {
            JSONObject o= (JSONObject) new JSONParser().parse(jsonString);
            return deserialize((JSONArray) o.get("reports"));
        } catch (ParseException e) {
            throw new WebApplicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<TransactionProcessingReport> deserialize(JSONArray reports) {
        List<TransactionProcessingReport> result = new ArrayList<>();
        for (JSONObject report: (Iterable<JSONObject>) reports) {
            result.add(TransactionProcessingReportJsonSerializer.deserialize(report));
        }
        return result;
    }
}
