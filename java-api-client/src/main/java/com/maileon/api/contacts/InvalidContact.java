package com.maileon.api.contacts;

public class InvalidContact extends Contact {

    private int errorCode;

    private String errorField;

    public InvalidContact() {

    }

    public InvalidContact(int errorCode, String errorField) {
        this.errorCode = errorCode;
        this.errorField = errorField;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    @Override
    public String toString() {
        return String.format("InvalidContact [errorCode=%s, errorField=%s, email=%s, externalId=%s]",
                errorCode, errorField, getEmail(), getExternalId());
    }

}
