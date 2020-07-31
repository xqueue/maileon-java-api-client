package com.maileon.api.utils;

public class BooleanHelper {

    /**
     * This method parses the String <code>value</code> and returns true if the value is "true", "yes" or "1" (ignoring case). <code>null</code> is interpreted to be false.
     *
     * @param value string representation of boolean
     * @return parsed value
     */
    public static boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }

        String compare = value.toLowerCase();
        return "true".equals(compare) || "1".equals(compare) || "yes".equals(compare);
    }

    private BooleanHelper() {
    }

}
