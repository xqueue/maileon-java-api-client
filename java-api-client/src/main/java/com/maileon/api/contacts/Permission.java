package com.maileon.api.contacts;

import java.io.Serializable;

/**
 * Representation of contact permission.
 *
 */
public class Permission implements Serializable {

    /**
     * No permission.
     */
    public static final Permission NONE = new Permission(1);

    /**
     * Single Opt-In.
     */
    public static final Permission SOI = new Permission(2);

    /**
     * Confirmed Opt-In.
     */
    public static final Permission COI = new Permission(3);

    /**
     * Double Opt-In.
     */
    public static final Permission DOI = new Permission(4);

    /**
     * Double Opt-In with tracking permission.
     */
    public static final Permission DOI_PLUS = new Permission(5);

    /**
     * Other type of permission.
     */
    public static final Permission OTHER = new Permission(6);

    public static final Permission get(int code) {
        return new Permission(code);
    }

    /**
     * The code.
     */
    private final int code;

    /**
     * Instantiates a new permission.
     *
     * @param code the code
     */
    protected Permission(int code) {
        this.code = code;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}
