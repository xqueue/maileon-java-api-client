package com.maileon.api.transactions;

import java.io.Serializable;

public class Attachment implements Serializable {

    private final String filename;
    private final String mimetype;
    private final String data;

    /**
     * Create a new attachment.
     *
     * @param filename the file name of the attachment
     * @param mimetype the mime type of the attachment
     * @param data the file contents of the attachment, encoded as Base64
     */
    protected Attachment(String filename, String mimetype, String data) {
        if (filename == null) {
            throw new NullPointerException("file name must of attachment not be null");
        }
        if (mimetype == null) {
            throw new NullPointerException("mime type name of attachment must not be null");
        }
        if (data == null) {
            throw new NullPointerException("data of attachment must not be null");
        }
        this.filename = filename;
        this.mimetype = mimetype;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public String getData() {
        return data;
    }

}
