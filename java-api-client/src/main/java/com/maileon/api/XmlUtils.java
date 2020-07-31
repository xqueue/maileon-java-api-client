package com.maileon.api;

import com.maileon.api.utils.BooleanHelper;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.ws.rs.WebApplicationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlUtils {

    /**
     * Parses the XML.
     *
     * @param text the text
     * @return the element
     * @throws MaileonClientException the Maileon client exception
     */
    public static Element parseXml(String text) throws MaileonClientException {
        try {
            return parseText(text).getRootElement();
        } catch (Throwable t) {
            throw new MaileonClientException("failed to parse xml document", t);
        }
    }

    public static Element parseXmlDocument(InputStream in) throws IOException, DocumentException {
        try {
            return DocumentHelper.parseText(IOUtils.toString(in, StandardCharsets.UTF_8)).getRootElement();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Element parseXmlDocument(InputStream xsd, InputStream xml) throws MaileonException {
        ErrorHandler errorHandler = new ErrorHandler();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SchemaFactory schemaFactory
                    = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            factory.setSchema(schemaFactory.newSchema(
                    new Source[]{new StreamSource(xsd)}));

            SAXParser parser = factory.newSAXParser();

            SAXReader reader = new SAXReader(parser.getXMLReader());
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            reader.setErrorHandler(errorHandler);

            Element root = reader.read(xml).getRootElement();
            if (errorHandler.hasErros()) {
                throw new MaileonException("Problem while performing XML operations: "
                        + errorHandler.errorXml().asXML());
            }
            return root;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Throwable e) {
            if (errorHandler.hasErros()) {
                throw new MaileonException("Problem while performing XML operations: "
                        + errorHandler.errorXml().asXML(), e);
            } else {
                throw new MaileonException("Problem while performing XML operations:", e);
            }
        }

    }

    public static String errorXml(String error) {
        Element errorE = DocumentHelper.createElement("error");
        Element msg = DocumentHelper.createElement("message");
        msg.setText(error);
        errorE.add(msg);
        return errorE.asXML();
    }

    public static String formatNumber(String tag, Number number) {
        Element e = DocumentHelper.createElement(tag);
        e.setText(number.toString());
        return asXml(e);
    }

    public static String keyValuePairsToXml(String rootTag, Map<String, String> keyValuePairs) {
        return asXml(keyValuePairsToElement(rootTag, keyValuePairs));
    }

    public static Element keyValuePairsToElement(String rootTag, Map<String, String> keyValuePairs) {
        Element e = DocumentHelper.createElement(rootTag);
        for (String key : keyValuePairs.keySet()) {
            Element tag = DocumentHelper.createElement(key);
            String value = keyValuePairs.get(key);
            if (value == null) {
                tag.add(DocumentHelper.createAttribute(tag, "nil", "true"));
            } else {
                tag.setText(value);
            }
            e.add(tag);
        }
        return e;
    }

    public static String keyValuePairToXml(String key, String value) {
        Element e = DocumentHelper.createElement(key);
        if (value == null) {
            e.add(DocumentHelper.createAttribute(e, "nil", "true"));
        } else {
            e.setText(value);
        }
        return asXml(e);
    }

    public static String asXml(Element e) {
        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw, OutputFormat.createPrettyPrint());
        try {
            writer.write(e);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return sw.toString();
    }

    public static boolean isNil(Element e) {
        String nil = e.attributeValue("nil");
        return nil != null && BooleanHelper.parseBoolean(nil);
    }

    public static String getNullableStringValue(Element e) {
        return isNil(e) ? null : e.getText();
    }

    public static Boolean getNullableBooleanValue(Element e) {
        return isNil(e) ? null : BooleanHelper.parseBoolean(e.getText());
    }

    public static Integer getNullableIntegerValue(Element e) {
        return isNil(e) ? null : Integer.parseInt(e.getText());
    }

    public static Long getNullableLongValue(Element e) {
        return isNil(e) ? null : Long.parseLong(e.getText());
    }

    public static Double getNullableDoubleValue(Element e) {
        return isNil(e) ? null : Double.parseDouble(e.getText());
    }

    public static void addChildElement(Element parent, String childName, String value) {
        Element child = DocumentHelper.createElement(childName);
        parent.add(child);
        if (value != null) {
            child.setText(value);
        } else {
            child.add(DocumentHelper.createAttribute(child, "nil", "true"));
        }
    }

    public static void addChildElement(Element parent, String childName, boolean b) {
        XmlUtils.addChildElement(parent, childName, String.valueOf(b));
    }

    public static Element addChildElement(Element parent, String name) {
        Element child = DocumentHelper.createElement(name);
        parent.add(child);
        return child;
    }

    public static Element appendChildElementSafe(Element parent, String childName) {
        Element child = parent.element(childName);
        if (child == null) {
            child = DocumentHelper.createElement(childName);
            parent.add(child);
        }
        return child;
    }

    public static Document parseText(String text) throws DocumentException, SAXException {
        SAXReader reader = new SAXReader();
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        reader.setMergeAdjacentText(true);
        reader.setStripWhitespaceText(true);
        InputSource source = new InputSource(new StringReader(text));
        source.setEncoding("UTF-8");
        Document result = reader.read(source);
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding("UTF-8");
        }
        return result;
    }
}

class ErrorHandler implements org.xml.sax.ErrorHandler {

    private int errors = 0;

    private final List<ValidationError> validationErrors = new ArrayList<>();

    private boolean fatalError;

    private ValidationError fatalValidationError;

    public boolean hasErros() {
        return errors > 0 || fatalError;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        // nop
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        errors++;
        if (errors >= 10) {
            validationErrors.remove(0);
        }
        validationErrors.add(new ValidationError(exception));
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        fatalError = true;
        fatalValidationError = new ValidationError(exception);
    }

    public Element errorXml() {
        Element error = DocumentHelper.createElement("error");
        if (fatalError) {
            error.add(fatalValidationError.toXml());
        }
        for (ValidationError ve : validationErrors) {
            error.add(ve.toXml());
        }
        return error;
    }

    private static class ValidationError {

        private final int column;

        private final int line;

        private final String publicId;

        private final String systemId;

        private final String message;

        ValidationError(SAXParseException e) {
            column = e.getColumnNumber();
            line = e.getLineNumber();
            publicId = e.getPublicId();
            systemId = e.getSystemId();
            message = e.getMessage();
        }

        public Element toXml() {
            Element ve = DocumentHelper.createElement("validation_error");
            Element c = DocumentHelper.createElement("column");
            c.setText(String.valueOf(column));
            ve.add(c);
            Element l = DocumentHelper.createElement("line");
            l.setText(String.valueOf(line));
            ve.add(l);
            if (null != publicId) {
                Element pi = DocumentHelper.createElement("public_id");
                pi.setText(publicId);
                ve.add(pi);
            }
            if (null != systemId) {
                Element si = DocumentHelper.createElement("system_id");
                si.setText(systemId);
                ve.add(si);
            }
            if (null != message) {
                Element m = DocumentHelper.createElement("message");
                m.setText(message);
                ve.add(m);
            }

            return ve;
        }

    }
}
