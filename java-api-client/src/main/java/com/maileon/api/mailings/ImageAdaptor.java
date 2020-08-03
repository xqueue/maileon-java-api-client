package com.maileon.api.mailings;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.maileon.api.MaileonException;
import com.maileon.api.utils.BooleanHelper;

class ImageAdaptor {

    static Image fromXML(Element e) throws MaileonException {
        Image image = new Image();
        image.setSrc(e.elementText("src"));
        image.setHosted(parseBooleanSafe(e.elementText("hosted")));
        image.setWidth(parseIntegerSafe(e.elementText("width")));
        image.setHeight(parseIntegerSafe(e.elementText("height")));
        image.setAlt(e.elementText("alt"));
        image.setTitle(e.elementText("title"));
        return image;
    }

    static List<Image> fromXML(List<Element> elements) throws MaileonException {
        List<Image> images = new ArrayList<>();
        for (Element e : elements) {
            images.add(fromXML(e));
        }
        return images;
    }

    private static Integer parseIntegerSafe(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Boolean parseBooleanSafe(String str) {
        if (str == null) {
            return null;
        }
        return BooleanHelper.parseBoolean(str);
    }
}
