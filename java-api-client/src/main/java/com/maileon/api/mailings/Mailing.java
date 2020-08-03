package com.maileon.api.mailings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Represents a mailing in the account. This class is extensible and supports different parameters and contains only the id by default.</p>
 * This class is used by the API for external representation.
 *
 */
public class Mailing implements Serializable {

    private static final long serialVersionUID = 3475689503475638798L;

    private long id;

    private Map<String, String> fields = new HashMap<>();

    public Mailing(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        String fieldsString = "";

        if (fields.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(", fields{");
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            fieldsString = sb.toString();
        }
        return String.format("Mailing{id=%d%s}", id, fieldsString);
    }

}
