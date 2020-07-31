package com.maileon.api;

import com.maileon.api.utils.GzipReaderWriterInterceptor;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.UriBuilder;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>AbstractMaileonService</code> provides an abstract interface for all its subservices.
 *
 */
public abstract class AbstractMaileonService {

    private static final Logger logger = Logger.getLogger("Maileon");

    private static final int LOGGING_MAX_ENTITY_SIZE = 131072;

    /**
     * The Constant MAILEON_XML_TYPE.
     */
    public static final MediaType MAILEON_XML_TYPE = new MediaType("application", "vnd.maileon.api+xml", "utf-8");

    /**
     * The configuration.
     */
    private MaileonConfiguration config;

    /**
     * The debug.
     */
    private boolean debug = false;

    /**
     * Accept gzip-encoding in response.
     */
    private boolean compressionEnabled = false;

    /**
     * gzip compression for request entity. Works only if compressionEnabled=true
     */
    private boolean requestCompressionEnabled = false;

    /**
     * The service.
     */
    private final String service;

    /**
     * Instantiates a new abstract Maileon service.
     *
     * @param config The Maileon API-Configuration
     * @param service The concrete subservice
     */
    public AbstractMaileonService(MaileonConfiguration config, String service) {
        this.config = config;
        this.service = service;

        this.debug = config.isDebug();
    }

    /**
     * Checks if is for debugging purposes.
     *
     * @return <code>true</code>, if is debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug.
     *
     * @param debug the new debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        logger.setLevel(debug ? Level.INFO : Level.OFF);
    }

    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    public final void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public boolean isRequestCompressionEnabled() {
        return requestCompressionEnabled;
    }

    public final void setRequestCompressionEnabled(boolean requestCompressionEnabled) {
        this.requestCompressionEnabled = requestCompressionEnabled;
    }

    //GET
    /**
     * Represents GET request.
     *
     * @param path The path to the corresponding resources
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper get(String path) throws MaileonException {
        return get(path, null, MAILEON_XML_TYPE);
    }

    /**
     * Represents GET request with parameters.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the GET request
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper get(String path, QueryParameters parameters) throws MaileonException {
        return get(path, parameters, MAILEON_XML_TYPE);
    }

    /**
     * Represents a GET request with parameters and media type.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the GET request
     * @param mediaType The required media type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper get(String path, QueryParameters parameters, MediaType mediaType) throws MaileonException {
        Response cr = getBuilder(path, parameters).accept(mediaType).get();
        ResponseWrapper resp = new ResponseWrapper(cr);

        analyzeStatusCode(resp);
        return resp;
    }

    //POST
    /**
     * Represents a POST request.
     *
     * @param path The path to the corresponding resources
     * @param entity The entity to post as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper post(String path, Object entity) throws MaileonException {
        return post(path, null, MAILEON_XML_TYPE, entity);
    }

    /**
     * Represents a POST request with parameters.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the POST request
     * @param entity The entity to post as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper post(String path, QueryParameters parameters, Object entity) throws MaileonException {
        return post(path, parameters, MAILEON_XML_TYPE, entity);
    }

    /**
     * Represents a POST request with parameters and media type.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the POST request
     * @param mediaType The required media type
     * @param entity The entity to post as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper post(String path, QueryParameters parameters, MediaType mediaType, Object entity) throws MaileonException {
        // Entity must not be null
        if (entity == null) {
            entity = "";
        }
        Response cr = getBuilder(path, parameters).header("Accept", mediaType.toString()).post(Entity.entity(entity, mediaType));

        ResponseWrapper resp = new ResponseWrapper(cr);
        analyzeStatusCode(resp);
        return resp;
    }

    /**
     * Represents a PUT request.
     *
     * @param path The path to the corresponding resources
     * @param entity The entity to put as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper put(String path, Object entity) throws MaileonException {
        return put(path, null, MAILEON_XML_TYPE, entity);
    }

    /**
     * Represents a PUT request with parameters.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the PUT request
     * @param entity The entity to put as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper put(String path, QueryParameters parameters, Object entity) throws MaileonException {
        return put(path, parameters, MAILEON_XML_TYPE, entity);
    }

    /**
     * Represents a PUT request with parameters and media type.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the PUT request
     * @param mediaType The required media type
     * @param entity The entity to put as an object type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper put(String path, QueryParameters parameters, MediaType mediaType, Object entity) throws MaileonException {
        // Entity must not be null
        if (entity == null) {
            entity = "";
        }
        Response cr = getBuilder(path, parameters).put(Entity.entity(entity, mediaType));
        ResponseWrapper resp = new ResponseWrapper(cr);

        analyzeStatusCode(resp);
        return resp;
    }

    /**
     * Represents a DELETE request.
     *
     * @param path The path to the corresponding resources
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper delete(String path) throws MaileonException {
        return delete(path, null, MAILEON_XML_TYPE);
    }

    /**
     * Represents a DELETE request with parameters.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the DELETE request
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper delete(String path, QueryParameters parameters) throws MaileonException {
        return delete(path, parameters, MAILEON_XML_TYPE);
    }

    /**
     * Represents a PUT request with parameters and media type.
     *
     * @param path The path to the corresponding resources
     * @param parameters The parameters of the DELETE request
     * @param mediaType The required media type
     * @return The response as a {@link ResponseWrapper}
     * @throws MaileonException the Maileon exception
     */
    protected ResponseWrapper delete(String path, QueryParameters parameters, MediaType mediaType) throws MaileonException {
        Response cr = getBuilder(path, parameters).accept(mediaType).delete();
        ResponseWrapper resp = new ResponseWrapper(cr);
        analyzeStatusCode(resp);
        return resp;
    }
    private Client restclient;

    /**
     * Gets the builder.
     *
     * @param path the path
     * @param parameters the parameters
     * @return the builder
     */
    private Builder getBuilder(String path, QueryParameters parameters) {
        initClient();
        WebTarget wt = restclient.target(UriBuilder.fromUri(config.getBaseUri()));

        if (!debug && compressionEnabled) {
            //http://www.codingpedia.org/ama/how-to-compress-responses-in-java-rest-api-with-gzip-and-jersey/
            //http://stackoverflow.com/questions/25542450/gzip-format-decompress-jersey
            wt.register(new GzipReaderWriterInterceptor(requestCompressionEnabled));
        }

        wt = wt.path(path);

        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                wt = wt.queryParam(parameters.getName(i), parameters.getValue(i));
            }
        }
        Builder request = wt.request();
        if (!debug && compressionEnabled) {
            //http://www.codingpedia.org/ama/how-to-compress-responses-in-java-rest-api-with-gzip-and-jersey/
            //http://stackoverflow.com/questions/25542450/gzip-format-decompress-jersey
            request.header("Accept-Encoding", "gzip");
        }

        return request;

    }

    private void initClient() {
        if (restclient == null) {
            ClientBuilder cb = ClientBuilder.newBuilder();
            if (config.getProxy() != null && config.getProxy() != Proxy.NO_PROXY) {
                // set proxy configuration
                cb.property(ClientProperties.PROXY_URI, (config.getProxy().address()).toString());
                //cb.property(ClientProperties.PROXY_USERNAME, config.getProxy().);
                cb.property(ClientProperties.PROXY_PASSWORD, config.getProxy().address().toString());
            }

            // If set up, you can ignore SSL errors here. For local testing without fiddling with certificates.
            if (config.isIgnoreSslErrors()) {
                try {
                    Logger.getLogger(service).info("Ignoring possible SSL errors due to config setting 'ignoreSslErrors'.'");

                    SSLContext sslcontext = SSLContext.getInstance("TLS");

                    sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }}, new java.security.SecureRandom());

                    cb.sslContext(sslcontext).hostnameVerifier((s1, s2) -> true);
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                    throw new MaileonClientException("Error ignoring SSL in debug mode", e);
                }

            }
            if (debug) {
                cb.register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, LOGGING_MAX_ENTITY_SIZE));
            }
            final HttpAuthenticationFeature authFeature = HttpAuthenticationFeature.basicBuilder().credentials(config.getApiKey(), "").build();
            cb.register(authFeature);
            restclient = cb.build();
        }
    }

    /**
     * The Class QueryParameters is used to represent the parameters of the client requests.
     */
    public static class QueryParameters {

        /**
         * The names.
         */
        private final List<String> names = new ArrayList<>();

        /**
         * The values.
         */
        private final List<String> values = new ArrayList<>();

        /**
         * Instantiates a new query parameters.
         */
        public QueryParameters() {

        }

        /**
         * Instantiates a new query parameters.
         *
         * @param name the name of the parameter
         * @param value the value of the parameter
         */
        public QueryParameters(String name, Object value) {
            add(name, value);
        }

        /**
         * Adds query parameters.
         *
         * @param name the name of the parameter
         * @param value the value of the parameter
         * @return the constructed query parameters
         */
        public final QueryParameters add(String name, Object value) {
            if (value != null) {
                names.add(name);
                values.add(value.toString());
            }
            return this;
        }

        /**
         * Adds the list of values for a parameter.
         *
         * @param name the name of the required parameter
         * @param values the values to add
         * @return the query parameters
         */
        public QueryParameters addList(String name, List<Object> values) {
            for (Object value : values) {
                add(name, value);
            }
            return this;
        }

        /**
         * Adds the whole query parameters.
         *
         * @param parameters the parameters to add
         * @return the constructed query parameters
         */
        public QueryParameters add(QueryParameters parameters) {
            names.addAll(parameters.names);
            values.addAll(parameters.values);
            return this;
        }

        /**
         * Returns the size of the query parameters.
         *
         * @return the size as an integer number
         */
        public int size() {
            return names.size();
        }

        /**
         * Gets the name of a parameter given its index.
         *
         * @param i the index of the parameters
         * @return the corresponding name
         */
        public String getName(int i) {
            return names.get(i);
        }

        /**
         * Gets the value of a parameter given its index.
         *
         * @param i the index of the parameters
         * @return the corresponding value
         */
        public String getValue(int i) {
            return values.get(i);
        }

    }

    /**
     * Analyze status code.
     *
     * @param resp the response
     * @throws MaileonException the Maileon exception
     */
    private static void analyzeStatusCode(ResponseWrapper resp) throws MaileonException {

        Response.StatusType status = resp.getStatus();
        Family family = status.getFamily();
        switch (family) {
            case SUCCESSFUL:
                return;
            case CLIENT_ERROR:
                // try to get the error message
                String message = parseErrorMsg(resp);
                switch (status.getStatusCode()) {
                    case 400: // bad request
                        throw (message != null) ? new MaileonBadRequestException(message, resp) : new MaileonBadRequestException(resp);
                    case 401: // unauthorized
                        throw (message != null) ? new MaileonAuthorizationException(message, resp) : new MaileonAuthorizationException(resp);
                    case 403: // forbidden
                        throw (message != null) ? new MaileonAccessControlException(message, resp) : new MaileonAccessControlException(resp);
                    case 404: // not found
                        throw (message != null) ? new MaileonNotFoundException(message, resp) : new MaileonNotFoundException(resp);
                    default:
                        throw (message != null) ? new MaileonBadRequestException(message, resp) : new MaileonException(resp);
                }
            case OTHER:
                throw new MaileonException(resp);
            case SERVER_ERROR:
                switch (status.getStatusCode()) {
                    case 500: // not available
                        throw new MaileonException(status.getStatusCode() + " - " + parseErrorMsg(resp), resp);
                    case 503: // not available
                        throw new MaileonBadRequestException(parseErrorMsg(resp), resp);
                    default:
                        throw new MaileonServerError(resp);
                }
            default:
                throw new IllegalStateException("unexpected status code family: " + family);
        }
    }

    /**
     * Parses the error message.
     *
     * @param resp the response
     * @return the string
     */
    private static String parseErrorMsg(final ResponseWrapper resp) {
        if (resp.getType() != null && resp.getType().isCompatible(MAILEON_XML_TYPE)) {
            try {
                String entity = resp.getEntityAsString();
                if (entity != null && !entity.isEmpty()) {
                    Element error = DocumentHelper.parseText(entity).getRootElement();
                    return error.elementText("message");
                }
            } catch (Throwable e) {

            }
        }
        return null;
    }

    /**
     * Encodes the path to a particular resource.
     *
     * @param path The path to encode
     * @return The encoded path
     * @throws MaileonException
     */
    protected static String encodePath(final String path) throws MaileonException {
        try {
            return new URI(null, null, path, null).toString();
        } catch (URISyntaxException ioe) {
            throw new MaileonException("path cannot be encoded: path='" + path + "'", ioe);
        }
    }
}
