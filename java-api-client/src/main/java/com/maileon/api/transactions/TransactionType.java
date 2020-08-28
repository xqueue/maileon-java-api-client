package com.maileon.api.transactions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionType implements Serializable {

    public static final int DEFAULT_ARCHIVING_DURATION = 14;

    private Long id;
    private Date created;
    private String name;
    private String description;
    private Integer archivingDuration = DEFAULT_ARCHIVING_DURATION;
    private boolean storeOnly = true;
    private List<Attribute> attributes = new ArrayList<>();

    /**
     * Returns ID of transaction type.
     *
     * @return id of transaction type
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Returns the name of transaction type.
     *
     * The name of attribute must start with a letter. Only letters, numbers and '.' are allowed.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of transaction type.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of definitions of attributes.
     *
     * @return the list of attributes.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Returns the archiving duration of transactions in days.
     *
     * @return the number of days
     */
    public Integer getArchivingDuration() {
        return archivingDuration;
    }

    /**
     * Set the archiving duration of transactions in days. The default value is 14 days.
     *
     * @param archivingDuration the number of days or null for unlimited
     */
    public void setArchivingDuration(Integer archivingDuration) {
        this.archivingDuration = archivingDuration;
    }

    /**
     * Checks if the the transactions are stored in optimized formats.
     *
     * The transactions in optimized formats allows longer string-fields and are processed faster, but cannot be used in contact filters and only with restrictions in marketing
     * automation. The default value is {@code true}
     *
     * @return true if transactions are stored in optimized formats.
     */
    public boolean isStoreOnly() {
        return storeOnly;
    }

    /**
     * Set the storing of transactions in optimized formats. The transactions in optimized formats allows longer string-fields and are processed faster, but cannot be used in
     * contact filters and only with restrictions in marketing automation.
     *
     * The default value is {@code true}
     *
     * @param storeOnly true if transactions must be stored in optimized formats.
     */
    public void setStoreOnly(boolean storeOnly) {
        this.storeOnly = storeOnly;
    }

    /**
     * Returns description of transaction type.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of transaction type.
     *
     * @param description short description of transaction type.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionType{" + id + ":" + name + ":" + archivingDuration + ":" + storeOnly + ":" + description + '}';
    }

}
