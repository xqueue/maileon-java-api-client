package com.maileon.api.contactfilters;


/**
 * 
 * This class wraps the result of creating a contact filter and contains a targetgroup (-1 if not created) and the ID of the (created) contact filter
 * 
 * @author Marcus Beckerle
 */
public class ContactfilterTargetgroupTuple {
    private final long contactfilterId;
    private final long targetgroupId;

    /**
     *
     * @param contactfilterId
     * @param targetgroupId
     */
    public ContactfilterTargetgroupTuple(long contactfilterId, long targetgroupId) {
        this.contactfilterId = contactfilterId;
        this.targetgroupId = targetgroupId;
    }

    public long getContactfilterId() {
        return contactfilterId;
    }

    public long getTargetgroupId() {
        return targetgroupId;
    }
}
