package com.maileon.api.mailings.trigger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import org.xml.sax.SAXException;

public class DispatchOptionsXmlSerializer {

    public static final String XML_DISPATCHING_OPTIONS = "dispatch_options";
    public static final String XML_TYPE = "type";
    public static final String XML_EVENT = "event";
    public static final String XML_EVENT_ID = "event_id";
    public static final String XML_SPEED_LEVEL = "speed_level";
    public static final String XML_START_TRIGGER = "start_trigger";
    public static final String XML_TARGET = "target";
    public static final String XML_INTERVAL = "interval";
    public static final String XML_DAY_OF_MONTH = "day_of_month";
    public static final String XML_HOURS = "hours";
    public static final String XML_MINUTES = "minutes";
    public static final String XML_DAY_OF_WEEK = "day_of_week";
    public static final String XML_CONTACT_FILTER_ID = "contact_filter_id";

    public static DispatchOptions deserialize(String options) throws DocumentException {

        SAXReader reader = new SAXReader();
        try {
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (SAXException ex) {
        }

        Document document = reader.read(new StringReader(options));
        Element rootElement = document.getRootElement();
        return deserialize(rootElement);
    }

    /**
     * Deserialize options
     *
     * @param options
     * @return
     */
    public static DispatchOptions deserialize(Element options) {

        if (options == null) {
            return null;
        }

        DispatchOptions result = new DispatchOptions();

        Element type = options.element(XML_TYPE);
        if (type != null) {
            result.setType(type.getText());
        }

        // This is used for some special cases like double opt in
        Element event = options.element(XML_EVENT);
        if (event != null) {
            result.setEvent(event.getText());
        }

        // This is the normal way to identify an event
        Element eventId = options.element(XML_EVENT_ID);
        if (eventId != null) {
            result.setEvent(eventId.getText());
        }

        Element speedLevel = options.element(XML_SPEED_LEVEL);
        if (speedLevel != null) {
            result.setSpeedLevel(speedLevel.getText());
        } else {
            result.setSpeedLevel("medium");
        }

        Element startTrigger = options.element(XML_START_TRIGGER);
        if (startTrigger != null) {
            result.setStartTrigger(Boolean.parseBoolean(startTrigger.getText()));
        } else {
            result.setStartTrigger(false);
        }

        Element target = options.element(XML_TARGET);
        if (target != null) {
            result.setTarget(target.getText());
        }

        Element interval = options.element(XML_INTERVAL);
        if (interval != null) {
            result.setInterval(interval.getText());
        }

        Element dayOfMonth = options.element(XML_DAY_OF_MONTH);
        if (dayOfMonth != null) {
            result.setDayOfMonth(Integer.parseInt(dayOfMonth.getText()));
        }

        Element hours = options.element(XML_HOURS);
        if (hours != null) {
            result.setHours(Integer.parseInt(hours.getText()));
        }

        Element minutes = options.element(XML_MINUTES);
        if (minutes != null) {
            result.setMinutes(Integer.parseInt(minutes.getText()));
        }

        Element weekDay = options.element(XML_DAY_OF_WEEK);
        if (weekDay != null) {
            result.setDayOfWeek(Integer.parseInt(weekDay.getText()));
        }

        Element contactFilterId = options.element(XML_CONTACT_FILTER_ID);
        if (contactFilterId != null) {
            result.setContactFilterId(Long.parseLong(contactFilterId.getText()));
        }

        return result;
    }

    /**
     * Serializes the {@link DispatchOptions}
     *
     * @param options {@link DispatchOptions} to serialize.
     * @return serialized <code>Element</code> out the a given options.
     */
    public static Element serialize(DispatchOptions options) {
        Element root = DocumentHelper.createElement(XML_DISPATCHING_OPTIONS);

        if (options.getType() != null) {
            Element element = DocumentHelper.createElement(XML_TYPE);
            element.setText(options.getType());
            root.add(element);
        }

        if (options.getEvent() != null) {
            Element element = DocumentHelper.createElement(XML_EVENT);
            element.setText(options.getEvent());
            root.add(element);
        }

        // If the event is a pure number its probably the ID, also provide this one separated (cannot be distinguished)
        if (options.getEvent() != null) {
            try {
                Long.parseLong(options.getEvent());
                Element element = DocumentHelper.createElement(XML_EVENT_ID);
                element.setText(options.getEvent());
                root.add(element);
            } catch (NumberFormatException e) {
            }
        }

        if (options.getSpeedLevel() != null) {
            Element element = DocumentHelper.createElement(XML_SPEED_LEVEL);
            element.setText(options.getSpeedLevel());
            root.add(element);
        }

        Element startTriggerElement = DocumentHelper.createElement(XML_START_TRIGGER);
        startTriggerElement.setText(Boolean.toString(options.isStartTrigger()));
        root.add(startTriggerElement);

        if (options.getTarget() != null) {
            Element element = DocumentHelper.createElement(XML_TARGET);
            element.setText(options.getTarget());
            root.add(element);
        }

        if (options.getInterval() != null) {
            Element element = DocumentHelper.createElement(XML_INTERVAL);
            element.setText(options.getInterval());
            root.add(element);
        }

        if (options.getDayOfMonth() != null) {
            Element element = DocumentHelper.createElement(XML_DAY_OF_MONTH);
            element.setText(options.getDayOfMonth().toString());
            root.add(element);
        }

        if (options.getHours() != null) {
            Element element = DocumentHelper.createElement(XML_HOURS);
            element.setText(options.getHours().toString());
            root.add(element);
        }

        if (options.getMinutes() != null) {
            Element element = DocumentHelper.createElement(XML_MINUTES);
            element.setText(options.getMinutes().toString());
            root.add(element);
        }

        if (options.getDayOfWeek() != null) {
            Element element = DocumentHelper.createElement(XML_DAY_OF_WEEK);
            element.setText(options.getDayOfWeek().toString());
            root.add(element);
        }

        if (options.getContactFilterId() != null) {
            Element element = DocumentHelper.createElement(XML_CONTACT_FILTER_ID);
            element.setText(options.getContactFilterId().toString());
            root.add(element);
        }

        return root;
    }

}
