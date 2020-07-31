package com.maileon.api.contacts;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Contact implements Serializable {

    private static final long serialVersionUID = 7264187092112740159L;

    private Long id;

    private Permission permission;

    /**
     * The email.
     */
    private String email;

    private String externalId;

    private Timestamp updated;

    private Timestamp created;

    /**
     * The anonymous.
     */
    private boolean anonymous;

    /**
     * The standard fields.
     */
    private final Map<StandardContactField, String> standardFields = new HashMap<>();

    /**
     * The custom fields.
     */
    private final Map<String, String> customFields = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if is anonymous.
     *
     * @return true, if is anonymous
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Sets the anonymous.
     *
     * @param anonymous the new anonymous
     */
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    /**
     * Gets the standard fields.
     *
     * @return the standard fields
     */
    public Map<StandardContactField, String> getStandardFields() {
        return standardFields;
    }

    /**
     * Gets the custom fields.
     *
     * @return the custom fields
     */
    public Map<String, String> getCustomFields() {
        return customFields;
    }


    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Contact [id = %d, email=%s, anonymous=%s, created=%s, updated=%s standardFields=%s, customFields=%s]",
                id, email, anonymous, created, updated, standardFields, customFields);
    }

}
