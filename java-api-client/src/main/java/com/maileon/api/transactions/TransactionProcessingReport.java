package com.maileon.api.transactions;

import java.io.Serializable;

public class TransactionProcessingReport implements Serializable {

    private ContactReference contact;
    private boolean queued;
    private String message;
    private String transactionId;

    public ContactReference getContact() {
        return contact;
    }

    public void setContact(ContactReference contact) {
        this.contact = contact;
    }

    /**
     * Shows if the transaction was successfully validated and added to queue for further processing.
     *
     * @return true if transaction was accepted
     */
    public boolean isQueued() {
        return queued;
    }

    public void setQueued(boolean queued) {
        this.queued = queued;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{contact=");
        s.append(contact).append(',');
        s.append("queued=").append(queued);
        if (message != null) {
            s.append(",message=").append(message);
        }
        if (transactionId != null) {
            s.append(",transactionId=").append(transactionId);
        }
        s.append('}');
        return s.toString();
    }

}
