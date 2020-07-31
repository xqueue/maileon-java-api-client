package com.maileon.api.contacts;

import java.util.ArrayList;
import java.util.List;

public class SynchronizationReport {

    private boolean success;

    private int countContacts;

    private int countNewContacts;

    private int countExistingContacts;

    private int countUnsubscribedContacts;

    private int countInvalidContacts;

    private final List<InvalidContact> invalidContacts = new ArrayList<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCountContacts() {
        return countContacts;
    }

    public void setCountContacts(int countContacts) {
        this.countContacts = countContacts;
    }

    public int getCountNewContacts() {
        return countNewContacts;
    }

    public void setCountNewContacts(int countNewContacts) {
        this.countNewContacts = countNewContacts;
    }

    public int getCountExistingContacts() {
        return countExistingContacts;
    }

    public void setCountExistingContacts(int countExistingContacts) {
        this.countExistingContacts = countExistingContacts;
    }

    public int getCountUnsubscribedContacts() {
        return countUnsubscribedContacts;
    }

    public void setCountUnsubscribedContacts(int countUnsubscribedContacts) {
        this.countUnsubscribedContacts = countUnsubscribedContacts;
    }

    public int getCountInvalidContacts() {
        return countInvalidContacts;
    }

    public void setCountInvalidContacts(int countInvalidContacts) {
        this.countInvalidContacts = countInvalidContacts;
    }

    public List<InvalidContact> getInvalidContacts() {
        return invalidContacts;
    }

    @Override
    public String toString() {
        return String
                .format("SynchronizationReport [success=%s, countContacts=%s, countNewContacts=%s, countExistingContacts=%s, countUnsubscribedContacts=%s, countInvalidContacts=%s]",
                        success, countContacts, countNewContacts,
                        countExistingContacts, countUnsubscribedContacts, countInvalidContacts);
    }
}
