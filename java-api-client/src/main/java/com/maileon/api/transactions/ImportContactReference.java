package com.maileon.api.transactions;

import java.io.Serializable;

public class ImportContactReference implements Serializable {

    private Long id;
    private String email;
    private String externalId;
    private Integer permission;

    private String errorField;

    /**
     * Get Maileon ID of contact.
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get email of contact
     *
     * @return email of contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email of contact.
     *
     * @param email email of contact
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get external ID of contact.
     *
     * @return external ID of contact.
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Set external ID of contact.
     *
     * @param externalId external id of contact
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getPermission() {
        return permission;
    }

    /**
     * Set permission of of contact.
     *
     * @param permission permission of contact.
     * @see com.maileon.api.contacts.Permission
     */
    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    /**
     * Get the name of a field with invalid value.
     *
     * @return the name of field.
     */
    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        boolean notempty = false;
        if (id != null) {
            s.append("id=").append(id);
            notempty = true;
        }
        if (email != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("email=").append(email);
            notempty = true;
        }
        if (externalId != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("externalId=").append(externalId);
        }
        if (permission != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("permission=").append(permission);
        }
        if (errorField != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("errorField=").append(errorField);
        }
        s.append('}');
        return s.toString();
    }

}
