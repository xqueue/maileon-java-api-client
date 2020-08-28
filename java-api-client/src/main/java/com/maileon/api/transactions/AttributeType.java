package com.maileon.api.transactions;

public enum AttributeType {

    STRING, DOUBLE, FLOAT, INTEGER, BOOLEAN, TIMESTAMP, DATE, JSON;

    @Override
    public String toString() {
        switch (this) {
            case BOOLEAN:
                return "boolean";
            case DATE:
                return "date";
            case DOUBLE:
                return "double";
            case FLOAT:
                return "float";
            case INTEGER:
                return "integer";
            case JSON:
                return "json";
            case STRING:
                return "string";
            case TIMESTAMP:
                return "timestamp";
            default:
                return this.name().toLowerCase();
        }
    }
}
