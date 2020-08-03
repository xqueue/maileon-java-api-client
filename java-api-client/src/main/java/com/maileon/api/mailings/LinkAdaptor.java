package com.maileon.api.mailings;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.maileon.api.MaileonException;

class LinkAdaptor {

    static Link fromXML(Element e) throws MaileonException {
        Link link = new Link();
        link.setId(parseLongSafe(e.elementText("id")));
        link.setFormat(e.elementText("format"));
        link.setLayout(e.elementText("layout"));
        link.setUrl(e.elementText("url"));
        List<String> tags = new ArrayList<>();
        Element tagsE = e.element("tags");
        if (tagsE != null) {
            List<Element> ee = tagsE.elements("tag");
            for (Element tagE : ee) {
                tags.add(tagE.getText());
            }
        }
        link.setTags(tags);
        return link;
    }

    static List<Link> fromXML(List<Element> elements) throws MaileonException {
        List<Link> links = new ArrayList<>();
        for (Element e : elements) {
            links.add(fromXML(e));
        }
        return links;
    }

    private static Long parseLongSafe(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
