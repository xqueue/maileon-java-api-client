package com.maileon.api.contacts;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

class CustomContactFieldDefinitionAdaptor {

    static List<CustomContactFieldDefinition> fromXml(Element xml) {
        List<Element> elements = xml.elements("custom_field");
        List<CustomContactFieldDefinition> l = new ArrayList<>(elements.size());
        for (Element e : elements) {
            CustomContactFieldDefinition d = new CustomContactFieldDefinition(e.elementText("name"), e.elementText("type"));
            l.add(d);
        }
        return l;
    }

}
