package com.maileon.api.contacts;

public class CustomContactFieldDefinition {

    private String name;

    private String type;

    public CustomContactFieldDefinition() {

    }

    public CustomContactFieldDefinition(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "CustomContactFieldDefinition [name=%s, type=%s]",
                name, type);
    }

}
