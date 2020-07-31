package com.maileon.api.contacts;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class StandardContactField.
 *
 */
public class StandardContactField implements Serializable {

    /**
     * The Constant known.
     */
    private static final ArrayList<StandardContactField> KNOWN = new ArrayList<>(19);

    /*
     * Contact system attributes
     */
    /**
     * Sendout status can be "blocked" or "allowed"
     */
    public static final StandardContactField SENDOUT_STATUS = new StandardContactField("SENDOUT_STATUS");

    /**
     * Permission status can be "available" (permission != none and not unsubscribed), "none" (no permission given, yet), or "unsubscribed"
     */
    public static final StandardContactField PERMISSION_STATUS = new StandardContactField("PERMISSION_STATUS");

    /**
     * The Constant ADDRESS.
     */
    public static final StandardContactField ADDRESS = new StandardContactField("ADDRESS");

    /**
     * The Constant BIRTHDAY.
     */
    public static final StandardContactField BIRTHDAY = new StandardContactField("BIRTHDAY");

    /**
     * The Constant CITY.
     */
    public static final StandardContactField CITY = new StandardContactField("CITY");

    /**
     * The Constant COUNTRY.
     */
    public static final StandardContactField COUNTRY = new StandardContactField("COUNTRY");

    /**
     * The Constant FIRSTNAME.
     */
    public static final StandardContactField FIRSTNAME = new StandardContactField("FIRSTNAME");

    /**
     * The Constant FULLNAME.
     */
    public static final StandardContactField FULLNAME = new StandardContactField("FULLNAME");

    /**
     * The Constant GENDER.
     */
    public static final StandardContactField GENDER = new StandardContactField("GENDER");

    /**
     * The Constant HNR.
     */
    public static final StandardContactField HNR = new StandardContactField("HNR");

    /**
     * The Constant LASTNAME.
     */
    public static final StandardContactField LASTNAME = new StandardContactField("LASTNAME");

    /**
     * The Constant LOCALE.
     */
    public static final StandardContactField LOCALE = new StandardContactField("LOCALE");

    /**
     * The Constant NAMEDAY.
     */
    public static final StandardContactField NAMEDAY = new StandardContactField("NAMEDAY");

    /**
     * The Constant ORGANIZATION.
     */
    public static final StandardContactField ORGANIZATION = new StandardContactField("ORGANIZATION");

    /**
     * The Constant REGION.
     */
    public static final StandardContactField REGION = new StandardContactField("REGION");

    /**
     * The Constant SALUTATION.
     */
    public static final StandardContactField SALUTATION = new StandardContactField("SALUTATION");

    /**
     * The Constant STATE.
     */
    public static final StandardContactField STATE = new StandardContactField("STATE");

    /**
     * The Constant TITLE.
     */
    public static final StandardContactField TITLE = new StandardContactField("TITLE");

    /**
     * The Constant ZIP.
     */
    public static final StandardContactField ZIP = new StandardContactField("ZIP");

    /**
     * The _name.
     */
    private final String _name;

    /**
     * Instantiates a new standard contact field.
     *
     * @param name the name
     */
    protected StandardContactField(String name) {
        _name = name;
        synchronized (StandardContactField.class) {
            KNOWN.add(this);
        }
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _name;
    }

    /**
     * Parses the.
     *
     * @param name the name
     * @return the standard contact field
     */
    public static synchronized StandardContactField parse(String name) {
        for (int i = 0; i < KNOWN.size(); i++) {
            StandardContactField scf = KNOWN.get(i);
            if (name.equals(scf._name)) {
                return scf;
            }
        }
        throw new IllegalArgumentException("unknown standard contact field: " + name);
    }
}
