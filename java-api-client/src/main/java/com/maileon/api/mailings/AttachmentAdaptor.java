package com.maileon.api.mailings;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.maileon.api.MaileonException;

class AttachmentAdaptor {

    static Attachment fromXML(Element e) throws MaileonException {
        Attachment attachment = new Attachment();
        attachment.setId(Long.parseLong(e.elementText("id")));
        attachment.setFilename(e.elementText("filename"));
        attachment.setSizeKB(Long.parseLong(e.elementText("sizekb")));
        attachment.setDiagnosis(e.elementText("diagnosis"));
        attachment.setMimeType(e.elementText("mime_type"));
        return attachment;
    }

    static List<Attachment> fromXML(List<Element> elements) throws MaileonException {
        List<Attachment> attachments = new ArrayList<>(elements.size());
        for (Element e : elements) {
            attachments.add(fromXML(e));
        }
        return attachments;
    }
}
