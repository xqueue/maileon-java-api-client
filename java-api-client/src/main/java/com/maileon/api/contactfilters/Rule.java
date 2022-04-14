package com.maileon.api.contactfilters;


/**
 * @author Balogh Viktor balogh.viktor@maileon.hu | Maileon - Wanadis Kft.
 */
public class Rule {
    private boolean customfield;
    private String field;
    private String operator;
    private String value;
    private String type;

    public Rule() {}

    public Rule(boolean isCustomField, String field, String operator, String value) {
        this(isCustomField, field, operator, value, "string");
    }

    public Rule(boolean isCustomField, String field, String operator, String value, String type) {
        this.customfield = isCustomField;
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.type = type;
    }

    /**
     * @return the customfield
     */
    public boolean isCustomfield() {
        return customfield;
    }

    /**
     * @param customfield the customfield to set
     */
    public void setCustomfield(boolean customfield) {
        this.customfield = customfield;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Valid operators are: EQUALS, NOTEQUALS, CONTAINS, NOTCONTAINS, STARTS_WITH
     *
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Valid types are: string, integer, float, date, boolean
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
