package com.maileon.api.mailings;

import java.io.Serializable;

public class Attachment implements Serializable {

    private long id;

    private String filename;

    private String mimeType;

    private long sizeKB;

    private String diagnosis;

    private long created;

    private long updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSizeKB() {
        return sizeKB;
    }

    public void setSizeKB(long sizeKB) {
        this.sizeKB = sizeKB;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return String
                .format("Attachment [id=%s, filename=%s, mimeType=%s, sizeKB=%s, diagnosis=%s, created=%s, updated=%s]",
                        id, filename, mimeType, sizeKB, diagnosis, created,
                        updated);
    }

}
