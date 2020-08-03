/**
 * xqueue.com
 */
package com.maileon.api.mailings;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class represents a single Mailing Property.
 *
 * @author Oleksii Saukh
 * @since 03.07.2017
 */
@XmlType(propOrder = {"key", "value"})
public class Property implements Serializable {

    protected String key;

    protected String value;

    public Property() {

    }

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    @XmlElement(name = "key", required = true)
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    @XmlElement(name = "value", required = true)
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
