package com.maileon.api.transactions;

import java.io.Serializable;

public class Attribute implements Serializable {

    private Long id;
    private String name;
    private AttributeType type;
    private boolean required = false;
    private String description;

    /**
     * Returns ID of attribute.
     *
     * @return id of attribute
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of transaction type.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of transaction type.
     *
     * The name of attribute must start with a letter and has no more than 32 characters. Only letters, numbers and '.' are allowed.
     *
     * @param name the name of attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Returns description of attribute.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of attribute.
     *
     * @param description short description of attribute.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Attribute{" + id + ":" + name + ":" + type + (required ? ":required:" : "") + ":" + description + '}';
    }

}
