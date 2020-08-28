package com.maileon.api.transactions;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Transaction {

    private long type;
    private String typeName;
    private ContactReference contact;
    private ImportContactReference importReference;
    private Object content;
    private final List<Attachment> attachments = new ArrayList<>();

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ContactReference getContact() {
        return contact;
    }

    public void setContact(ContactReference contact) {
        this.contact = contact;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setContentFromString(String jsonContent) {
        try {
            setContent(new JSONParser().parse(jsonContent));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ImportContactReference getImportReference() {
        return importReference;
    }

    public void setImportReference(ImportContactReference importReference) {
        this.importReference = importReference;
    }

    /**
     * Read an attachment from a file and add it to the transaction, using the name of the provided {@link File} as the attachment's file name.
     *
     * @param file the file to read the attachment contents from
     * @param mimetype the mime type of the attachment
     * @throws IOException
     */
    public void addAttachment(File file, String mimetype) throws IOException {
        addAttachment(file.getName(), mimetype, file);
    }

    /**
     * Read an attachment from a file and add it to the transaction.
     *
     * @param filename the file name of the attachment to use in the e-mail
     * @param mimetype the mime type of the attachment
     * @param file the file to read the attachment contents from
     * @throws IOException
     */
    public void addAttachment(String filename, String mimetype, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            addAttachment(filename, mimetype, fis);
        }
    }

    /**
     * Read an attachment from a stream and add it to the transaction. This method closes the provided input stream.
     *
     * @param filename the file name of the attachment to use in the e-mail
     * @param mimetype the mime type of the attachment
     * @param inputStream the input stream to read the attachment contents from.
     * @throws IOException
     */
    public void addAttachment(String filename, String mimetype, InputStream inputStream) throws IOException {
        try {
            byte[] attachmentContent = IOUtils.toByteArray(inputStream);
            addAttachment(filename, mimetype, attachmentContent);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Add an attachment to the transaction.
     *
     * @param filename the file name of the attachment to use in the e-mail
     * @param mimetype the mime type of the attachment
     * @param contents the attachment contents
     */
    public void addAttachment(String filename, String mimetype, byte[] contents) {
        String encodedContents = Base64.getEncoder().encodeToString(contents);
        Attachment attachment = new Attachment(filename, mimetype, encodedContents);
        attachments.add(attachment);
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
