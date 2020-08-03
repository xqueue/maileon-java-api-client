package com.maileon.api.mailings;

import com.maileon.api.DateTimeConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.maileon.api.MaileonClientException;
import com.maileon.api.MaileonException;

class AttachmentAdaptor {

    static Attachment fromXML(Element e) throws MaileonException {
        Attachment attachment = new Attachment();
        attachment.setId(Long.parseLong(e.elementText("id")));
        attachment.setFilename(e.elementText("filename"));
        attachment.setSizeKB(Long.parseLong(e.elementText("sizekb")));
        attachment.setDiagnosis(e.elementText("diagnosis"));
        attachment.setMimeType(e.elementText("mime_type"));
        attachment.setCreated(parseTimestamp(e.elementText("created")));
        attachment.setUpdated(parseTimestamp(e.elementText("updated")));
        return attachment;
    }

    static List<Attachment> fromXML(List<Element> elements) throws MaileonException {
        List<Attachment> attachments = new ArrayList<>();
        for (Element e : elements) {
            attachments.add(fromXML(e));
        }
        return attachments;
    }

    private static long parseTimestamp(String yyyyMMddHHmmss) throws MaileonClientException {
        SimpleDateFormat f = (SimpleDateFormat) SimpleDateFormat.getInstance();
        f.applyPattern(DateTimeConstants.SQL_DATE_TIME_FORMAT);
        try {
            return f.parse(yyyyMMddHHmmss).getTime();
        } catch (ParseException e) {
            throw new MaileonClientException("unexpected date format: [" + yyyyMMddHHmmss + "]. Expected was: yyyy-MM-dd HH:mm:ss", e);
        }
    }

}
