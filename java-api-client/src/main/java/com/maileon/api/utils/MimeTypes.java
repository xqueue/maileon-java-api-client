package com.maileon.api.utils;

import javax.ws.rs.core.MediaType;

public final class MimeTypes {

    public static final String MAILEON_API_XML = "application/vnd.maileon.api+xml";

    public static final MediaType MEDIA_TYPE_MAILEON_API_XML = new MediaType("application", "vnd.maileon.api+xml");

    public static final String MAILEON_API_JSON = "application/vnd.maileon.api+json";

    public static final MediaType MEDIA_TYPE_MAILEON_API_JSON = new MediaType("application", "vnd.maileon.api+json");

    private MimeTypes() {
    }
}
