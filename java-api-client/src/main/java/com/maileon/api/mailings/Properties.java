/**
 * xqueue.com
 */
package com.maileon.api.mailings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oleksii Saukh
 * @since 03.07.2017
 */
@XmlRootElement(name = "properties")
public class Properties implements Serializable {

    protected List<Property> properties = new ArrayList<>();

    /**
     * @return the properties
     */
    @XmlElement(name = "property")
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
