package com.maileon.api.contacts;

import java.util.List;

import org.dom4j.Element;

import com.maileon.api.XmlUtils;
import com.maileon.api.utils.BooleanHelper;

class SynchronizationReportAdaptor {

    public static SynchronizationReport fromXml(Element xml) {
        SynchronizationReport report = new SynchronizationReport();
        report.setSuccess(BooleanHelper.parseBoolean(xml.elementText("success")));
        report.setCountContacts(Integer.parseInt(xml.elementText("count_contacts")));
        report.setCountNewContacts(Integer.parseInt(xml.elementText("count_new_contacts")));
        report.setCountExistingContacts(Integer.parseInt(xml.elementText("count_existing_contacts")));
        report.setCountUnsubscribedContacts(Integer.parseInt(xml.elementText("count_unsubscribed_contacts")));
        report.setCountInvalidContacts(Integer.parseInt(xml.elementText("count_invalid_contacts")));
        Element invalidContacts = xml.element("invalid_contacts");
        if (invalidContacts == null) {
            return report;
        }
        for (Element ic : (List<Element>) invalidContacts.elements()) {
            InvalidContact c = new InvalidContact();
            c.setEmail(XmlUtils.getNullableStringValue(ic.element("email")));
            c.setExternalId(XmlUtils.getNullableStringValue(ic.element("external_id")));
            Element error = ic.element("error");
            c.setErrorCode(Integer.parseInt(error.attributeValue("code")));
            c.setErrorField(XmlUtils.getNullableStringValue(error.element("error_field")));
            report.getInvalidContacts().add(c);
        }
        return report;
    }
}
