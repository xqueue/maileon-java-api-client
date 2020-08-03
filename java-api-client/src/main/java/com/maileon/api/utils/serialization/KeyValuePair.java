package com.maileon.api.utils.serialization;

import java.io.Serializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class KeyValuePair implements Serializable {

    private static final long serialVersionUID = 3520878277382109104L;

    private String key;
    private String value;

    public KeyValuePair() {
        this.key = null;
        this.value = null;
    }

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte getValueAsByte() {
        return Byte.parseByte(value);
    }

    public int getValueAsInteger() {
        return Integer.parseInt(value);
    }

    public long getValueAsLong() {
        return Long.parseLong(value);
    }

    public float getValueAsFloat() {
        return Float.parseFloat(value);
    }

    public double getValueAsDouble() {
        return Double.parseDouble(value);
    }

    public <T> T getValue(Class<T> type) {
        if (value == null) {
            return null;
        }

        if (byte.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)
                || float.class.equals(type)
                || double.class.equals(type)) {
            throw new NumberFormatException("The generic method <T> T getValue(Class<T> type) can only handle classes, not raw types like '" + type + "'. Please use one of the methods like getValueAsInteger(...) if pass e.g. Integer.class as parameter of this method.");
        }

        /*
         *  type.cast(value) would work if a proper type is given as parameter, e.g. a Long.
         *  However, the deserializer does not know the type and cannot put a Long in but
         *  always a String. Thus, the type cast to e.g. Long would fail here.
         */
        if (String.class.equals(type)) {
            return type.cast(value);
        }
        if (Byte.class.equals(type)) {
            return type.cast(Byte.parseByte(value));
        }
        if (Integer.class.equals(type)) {
            return type.cast(Integer.parseInt(value));
        }
        if (Long.class.equals(type)) {
            return type.cast(Long.parseLong(value));
        }
        if (Float.class.equals(type)) {
            return type.cast(Float.parseFloat(value));
        }
        if (Double.class.equals(type)) {
            return type.cast(Double.parseDouble(value));
        }

        // Else assume its an generic object; this will not work with the current deserializer
        return type.cast(value);
    }

    public String toXML() {
        Element root = DocumentHelper.createElement(key);
        root.setText(value);
        return root.asXML();
    }
}
