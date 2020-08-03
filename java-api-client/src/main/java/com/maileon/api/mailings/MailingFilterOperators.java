package com.maileon.api.mailings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Describes valid values for mailing operations Valid for strings: 'contains','equals','starts_with','ends_with' Valid for lists: 'and','or', 'xor'
 *
 */
public abstract class MailingFilterOperators {

    public static final String LIST_OP_CONTAINS_ALL = "and";
    public static final String LIST_OP_CONTAINS_AT_LEAST_ONE = "or";
    public static final String LIST_OP_CONTAINS_AT_MOST_ONE = "xor";

    public static final String STRING_OP_CONTAINS = "contains";
    public static final String STRING_OP_EQUALS = "equals";
    public static final String STRING_OP_STARTS_WITH = "starts_with";
    public static final String STRING_OP_ENDS_WITH = "ends_with";

    private static final Set<String> VALUES_LIST = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(LIST_OP_CONTAINS_ALL, LIST_OP_CONTAINS_AT_LEAST_ONE, LIST_OP_CONTAINS_AT_MOST_ONE)));

    private static final Set<String> VALUES_STRING = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(STRING_OP_CONTAINS, STRING_OP_EQUALS, STRING_OP_STARTS_WITH, STRING_OP_ENDS_WITH)));

    public static boolean isValidStringOp(String value) {
        if (value == null) {
            return false;
        }
        return VALUES_STRING.contains(value);
    }

    private static final Map<String, String> COLUMNS_STRING = new HashMap<>(4);

    static {
        COLUMNS_STRING.put(STRING_OP_CONTAINS, "contains");
        COLUMNS_STRING.put(STRING_OP_EQUALS, "equals");
        COLUMNS_STRING.put(STRING_OP_STARTS_WITH, "starts_with");
        COLUMNS_STRING.put(STRING_OP_ENDS_WITH, "ends_with");
    }

    public static boolean isValidListOp(String value) {
        if (value == null) {
            return false;
        }
        return VALUES_LIST.contains(value);
    }

    private static final Map<String, String> COLUMNS_LIST = new HashMap<>(4);

    static {
        COLUMNS_LIST.put(LIST_OP_CONTAINS_ALL, "and");
        COLUMNS_LIST.put(LIST_OP_CONTAINS_AT_LEAST_ONE, "or");
        COLUMNS_LIST.put(LIST_OP_CONTAINS_AT_MOST_ONE, "xor");
    }
}
