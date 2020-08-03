package com.maileon.api.mailings;

public class Format {

    public static final Format HTML = new Format("html");

    public static final Format TEXT = new Format("text");

    private final String value;

    protected Format(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
