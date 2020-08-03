package com.maileon.api.mailings;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.maileon.api.MaileonException;
import com.maileon.api.utils.BooleanHelper;

class PersonalizationAdaptor {

    static Personalization fromXML(Element e) throws MaileonException {
        Personalization personalization = new Personalization();
        personalization.setType(e.elementText("type"));
        personalization.setPropertyName(e.elementText("property_name"));
        personalization.setFallbackValue(e.elementText("fallback_value"));
        personalization.setOccursInConditionalContent(parseBooleanSafe(e.elementText("occurs_in_conditional_content")));
        personalization.setConditionalContentRuleId(e.elementText("conditional_content_ruleid"));
        personalization.setConditionalContentRulesetId(e.elementText("conditional_content_rulesetid"));
        personalization.setEventType(e.elementText("event_type"));
        personalization.setOptionName(e.elementText("option_name"));
        return personalization;
    }

    static List<Personalization> fromXML(List<Element> elements) throws MaileonException {
        List<Personalization> personalizations = new ArrayList<>();
        for (Element e : elements) {
            personalizations.add(fromXML(e));
        }
        return personalizations;
    }

    private static boolean parseBooleanSafe(String str) {
        if (str == null) {
            return false;
        }
        return BooleanHelper.parseBoolean(str);
    }
}
