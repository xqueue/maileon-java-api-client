package com.maileon.api.transactions;

import java.io.Serializable;

public class ContactReference implements Serializable {

    private Long id;
    private String email;
    private String externalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        boolean notempty = false;
        if (id != null) {
            s.append("id=").append(id);
            notempty = true;
        }
        if (email != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("email=").append(email);
            notempty = true;
        }
        if (externalId != null) {
            if (notempty) {
                s.append(',');
            }
            s.append("externalId=").append(externalId);
        }
        s.append('}');
        return s.toString();
    }

}
