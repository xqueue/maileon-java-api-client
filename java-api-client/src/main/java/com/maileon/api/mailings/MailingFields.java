package com.maileon.api.mailings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Describes valid fields that can be attached to a basic mailing object. This can be used e.g. if requesting details is required with one call.</p>
 *
 */
public abstract class MailingFields {

    public static final String TYPE = "type";
    public static final String STATE = "state";
    public static final String NAME = "name";
    public static final String SCHEDULE_TIME = "scheduleTime";

    private static final Set<String> VALIDFIELDS = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(TYPE, STATE, NAME, SCHEDULE_TIME)));

    private static final Map<String, String> COLUMNS = new HashMap<>(17);

    static {
        COLUMNS.put(TYPE, "type");
        COLUMNS.put(STATE, "state");
        COLUMNS.put(NAME, "name");
        COLUMNS.put(SCHEDULE_TIME, "scheduleTime");
    }

    public static boolean isValid(String field) {
        return VALIDFIELDS.contains(field);
    }

    public static String getColumn(String field) {
        return COLUMNS.get(field);
    }
}
