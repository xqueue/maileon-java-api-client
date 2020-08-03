/**
 * xqueue.com
 */
package com.maileon.api.mailings;

/**
 * Represents the Manual ReplyTo Address for a mailing. This class is used as a response wrapper for the method {@link MaileonMailingsService#getReplyToAddress(long)}
 *
 * @author Oleksii Saukh
 * @since 19.01.2017
 */
public class ReplyToAddress {

    /**
     * If <code>true</code>, the automatic replyTo address is set, if <code>false</code> the manually provided address is used.
     */
    private boolean auto;

    /**
     * If ReplyTo Address is generally is used.
     */
    private boolean active;

    /**
     * Reply email address. This field is empty of null if {@link #auto} is <code>true</code>. Provides manually set ReplyTo address if {@link #auto} is <code>false</code>.
     */
    private String customEmail;

    /**
     * Parse an xml string to get the filed values.
     *
     * @param s
     */
    public ReplyToAddress(String s) {
        String autoString = s.substring(s.indexOf("<auto>"), s.indexOf("</auto>"));
        String activeString = s.substring(s.indexOf("<active>"), s.indexOf("</active>"));
        if (s.contains("<customEmail>") && s.contains("</customEmail>")) {
            this.customEmail = s.substring(s.indexOf("<customEmail>"), s.indexOf("</customEmail>"));
        } else {
            this.customEmail = "";
        }
        this.auto = Boolean.parseBoolean(autoString);
        this.active = Boolean.parseBoolean(activeString);
    }

    /**
     * @return the auto
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * @param auto the auto to set
     */
    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the customEmail
     */
    public String getCustomEmail() {
        return customEmail;
    }

    /**
     * @param customEmail the customEmail to set
     */
    public void setCustomEmail(String customEmail) {
        this.customEmail = customEmail;
    }

    @Override
    public String toString() {
        return this.customEmail + " " + this.active + " " + this.auto;
    }
}
