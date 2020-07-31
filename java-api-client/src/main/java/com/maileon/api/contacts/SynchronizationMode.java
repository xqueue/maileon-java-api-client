package com.maileon.api.contacts;

/**
 * The Class SynchronizationMode.
 *
 */
public class SynchronizationMode {

    /**
     * The Constant UPDATE.
     */
    public static final SynchronizationMode UPDATE = new SynchronizationMode(1);

    /**
     * The Constant IGNORE.
     */
    public static final SynchronizationMode IGNORE = new SynchronizationMode(2);

    /**
     * The code.
     */
    private final int code;

    /**
     * Instantiates a new synchronization mode.
     *
     * @param code the code
     */
    protected SynchronizationMode(int code) {
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
