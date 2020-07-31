package com.maileon.api.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipReaderWriterInterceptor implements ReaderInterceptor, WriterInterceptor {

    private final boolean requestCompressionEnabled;

    public GzipReaderWriterInterceptor(boolean requestCompressionEnabled) {
        this.requestCompressionEnabled = requestCompressionEnabled;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        boolean compressed = false;

        List<String> contentEncodingHeaders = context.getHeaders().get("Content-Encoding");
        if (contentEncodingHeaders != null) {
            for (String encoding : contentEncodingHeaders) {
                if (encoding.contains("gzip")) {
                    compressed = true;
                    break;
                }
            }
        }

        if (compressed) {
            InputStream originalInputStream = context.getInputStream();
            context.setInputStream(new GZIPInputStream(originalInputStream));
        }
        return context.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        if (requestCompressionEnabled) {
            context.getHeaders().putSingle("Content-Encoding", "gzip");

            final OutputStream outputStream = context.getOutputStream();
            context.setOutputStream(new GZIPOutputStream(outputStream));
        }
        context.proceed();
    }

}
