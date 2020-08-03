package com.maileon.api.mailings;

import java.io.Serializable;

public class Personalization implements Serializable {

    private String type;

    private boolean occursInConditionalContent;

    private String conditionalContentRulesetId;

    private String conditionalContentRuleId;

    private String propertyName;

    private String fallbackValue;

    private String eventType;

    private String optionName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOccursInConditionalContent() {
        return occursInConditionalContent;
    }

    public void setOccursInConditionalContent(boolean occursInConditionalContent) {
        this.occursInConditionalContent = occursInConditionalContent;
    }

    public String getConditionalContentRulesetId() {
        return conditionalContentRulesetId;
    }

    public void setConditionalContentRulesetId(String conditionalContentRulesetId) {
        this.conditionalContentRulesetId = conditionalContentRulesetId;
    }

    public String getConditionalContentRuleId() {
        return conditionalContentRuleId;
    }

    public void setConditionalContentRuleId(String conditionalContentRuleId) {
        this.conditionalContentRuleId = conditionalContentRuleId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFallbackValue() {
        return fallbackValue;
    }

    public void setFallbackValue(String fallbackValue) {
        this.fallbackValue = fallbackValue;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    @Override
    public String toString() {
        return String
                .format("Personalization [type=%s, occursInConditionalContent=%s, conditionalContentRulesetId=%s, conditionalContentRuleId=%s, propertyName=%s, fallbackValue=%s, eventType=%s, optionName=%s]",
                        type, occursInConditionalContent,
                        conditionalContentRulesetId, conditionalContentRuleId,
                        propertyName, fallbackValue, eventType, optionName);
    }

}
