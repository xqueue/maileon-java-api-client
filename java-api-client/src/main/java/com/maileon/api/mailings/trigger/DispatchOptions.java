package com.maileon.api.mailings.trigger;

import java.io.Serializable;

public class DispatchOptions implements Serializable {

    /**
     * One of "MULTI" (Sammelversand) || "EXTERNAL" (Marketing Automation) || "SINGLE" (Eventbasierter Einzelversand)
     */
    private String type;

    /**
     * Used if "MULTI" is selected as type; one of "CONTACTFILTER" || "EVENT" || "RSS"
     */
    private String target;

    /**
     * This should be the event ID but for some cases there are special (String) keywords like DOI mailings...
     */
    private String event;

    /**
     * One of "HOUR" || "DAY" || "WEEK" || "MONTH"
     */
    private String interval;

    private Integer dayOfMonth;

    private Integer hours;

    private Integer minutes;

    private Integer dayOfWeek;

    private Long contactFilterId;

    private boolean startTrigger = false;

    /**
     * One of "LOW", "MEDIUM", or "HIGH"
     */
    private String speedLevel;

    public boolean isStartTrigger() {
        return startTrigger;
    }

    public void setStartTrigger(boolean startTrigger) {
        this.startTrigger = startTrigger;
    }

    public String getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(String speedLevel) {
        this.speedLevel = speedLevel;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Long getContactFilterId() {
        return contactFilterId;
    }

    public void setContactFilterId(long contactFilterId) {
        this.contactFilterId = Math.max(0, contactFilterId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "DispatchOptions{" + "type=" + type + ", event=" + event + ", interval=" + interval + ", dayOfMonth=" + dayOfMonth
                + ", hours=" + hours + ", minutes=" + minutes + ", dayOfWeek=" + dayOfWeek + ", target=" + target + ", contactFilterId=" + contactFilterId + "}";
    }

}
