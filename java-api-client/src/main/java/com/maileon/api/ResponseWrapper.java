package com.maileon.api;

import org.apache.commons.io.IOUtils;
import org.dom4j.Element;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The Class ResponseWrapper.
 *
 */
public class ResponseWrapper {

    /**
     * The status.
     */
    private Response.StatusType status;

    /**
     * The status code.
     */
    private int statusCode;

    /**
     * The reason phrase.
     */
    private String reasonPhrase;

    /**
     * The headers.
     */
    private MultivaluedMap<String, String> headers;

    /**
     * The type.
     */
    private MediaType type;

    /**
     * The entity.
     */
    private byte[] entity;

    /**
     * Instantiates a new responseAsObject wrapper.
     *
     * @param response the responseAsObject
     * @throws MaileonClientException the Maileon client exception
     */
    public ResponseWrapper(Response response) throws MaileonClientException {
        response.bufferEntity();
        status = response.getStatusInfo();
        statusCode = response.getStatus();
        reasonPhrase = status.getReasonPhrase();
        headers = response.getStringHeaders();
        type = response.getMediaType();
        if (response.hasEntity()) {
            try {
                try (InputStream eis = response.readEntity(InputStream.class)) {
                    entity = IOUtils.toByteArray(eis);
                }
            } catch (IOException e) {
                throw new MaileonClientException("failed to parse response entity", e);
            }
        }
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Response.StatusType getStatus() {
        return status;
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the reason phrase.
     *
     * @return the reason phrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public MediaType getType() {
        return type;
    }

    /**
     * Checks for entity.
     *
     * @return true, if successful
     */
    public boolean hasEntity() {
        return entity != null;
    }

    /**
     * Checks if the server responded with a 2xx status code
     *
     * @return true, if successful
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode <= 299;
    }

    /**
     * Gets the entity.
     *
     * @return the entity
     */
    public byte[] getEntity() {
        return entity;
    }

    /**
     * Gets the entity as string.
     *
     * @return the entity as string
     */
    public String getEntityAsString() {
        if (entity == null) {
            return null;
        }
        return new String(entity, StandardCharsets.UTF_8);
    }

    /**
     * Gets the entity as XML.
     *
     * @return the entity as XML
     * @throws MaileonClientException the Maileon client exception
     */
    public Element getEntityAsXml() throws MaileonClientException {
        if (entity == null) {
            return null;
        }
        return XmlUtils.parseXml(getEntityAsString());
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" + "statusCode=" + statusCode + ", reasonPhrase=" + reasonPhrase + '}';
    }

}
