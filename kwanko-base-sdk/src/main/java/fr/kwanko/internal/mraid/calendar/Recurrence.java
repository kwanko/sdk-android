package fr.kwanko.internal.mraid.calendar;

import java.util.Map;

/**
 * SourceCode
 * Created by erusu on 4/20/2017.
 */

class Recurrence {

    static final String REC_DAYS_IN_WEEK = "daysInWeek";
    static final String REC_DAYS_IN_MONTH = "daysInMonth";
    static final String REC_DAYS_IN_YEAR = "daysInYear";
    static final String REC_MONTHS_IN_YEAR = "monthsInYear";

    static final String EXPIRES = "expirationTime";
    static final int DEFAULT_INTERVAL = 1;
    private long expirationTime = CalendarEvent.INVALID_TIME;
    private String frequency;
    private Map<String,String> recurrencePlan;
    private int interval = DEFAULT_INTERVAL;

    String getFrequency() {
        return frequency;
    }

    void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    long getExpires() {
        return expirationTime;
    }

    void setExpires(long expires) {
        this.expirationTime = expires;
    }

    Map<String,String> getRecurrencePlan() {
        return recurrencePlan;
    }

    void setRecurrencePlan(Map<String,String> recurrencePlan) {
        this.recurrencePlan = recurrencePlan;
    }

    int getInterval() {
        return interval;
    }

    void setInterval(int interval) {
        this.interval = interval;
    }

}
