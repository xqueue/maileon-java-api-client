package com.maileon.api.contactfilters;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the values of a contact filter
 *
 */

public class Contactfilter {

    protected String author;
    protected int countContacts;
    protected int countRules;
    protected String created;
    protected long id;
    protected String name;
    protected String status;
    protected long updated;

    protected List<Rule> rules = new ArrayList<>();

    public Contactfilter() {}

    public Contactfilter(long id, String name, String created, String author,
            int countContacts, int countRules, String status, long updated, List<Rule> rules) {
        super();
        this.id = id;
        this.name = name;
        this.created = created;
        this.author = author;
        this.countContacts = countContacts;
        this.countRules = countRules;
        this.status = status;
        this.updated = updated;

                if(rules != null) { this.rules = rules; }
    }

    /**
     * Gets the value of the author property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the countContacts property.
     *
     */
    public int getCountContacts() {
        return countContacts;
    }

    /**
     * Sets the value of the countContacts property.
     *
     */
    public void setCountContacts(int value) {
        this.countContacts = value;
    }

    /**
     * Gets the value of the countRules property.
     *
     */
    public int getCountRules() {
        return countRules;
    }

    /**
     * Sets the value of the countRules property.
     *
     */
    public void setCountRules(int value) {
        this.countRules = value;
    }

    /**
     * Gets the value of the created property.
     * yyyy-MM-dd HH:mm:ss
     *
     */
    public String getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * yyyy-MM-dd HH:mm:ss
     *
     */
    public void setCreated(String value) {
        this.created = value;
    }

    /**
     * Gets the value of the id property.
     *
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the updated property.
     *
     */
    public long getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     *
     */
    public void setUpdated(long value) {
        this.updated = value;
    }

    /**
     * Adds a single rule to this contact filter
     *
     * @param rule the rule to add
     */
    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    /**
     * Deletes every rule
     */
    public void clearRules() {
        this.rules.clear();
    }

    /**
     * Returns the current list of rules
     *
     * @return
     */
    public List<Rule> getRules() {
        return this.rules;
    }

    @Override
    public String toString() {
        StringBuilder rules = new StringBuilder();

        for(Rule rule : this.rules) {
            rules.append(rule.toString());
            rules.append(',');
        }

        if(rules.length() > 0) { rules.deleteCharAt(rules.length() - 1); }

        return "ContactFilter [author=" + author + ", countContacts="
                        + countContacts + ", countRules=" + countRules + ", created="
                        + created + ", id=" + id + ", name=" + name + ", status="
                        + status + ", updated=" + updated + ", rules={" + rules.toString() + "}]";
    }

}
