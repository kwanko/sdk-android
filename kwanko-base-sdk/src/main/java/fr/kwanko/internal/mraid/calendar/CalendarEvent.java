package fr.kwanko.internal.mraid.calendar;

/**
 * SourceCode
 * Created by erusu on 4/20/2017.
 */

class CalendarEvent {

    static final int INVALID_TIME = -1;

    private String description;
    private String location;
    private String summary;
    private long start;
    private long end;
    private boolean transparency;
    private String id;
    private Recurrence recurrence;

    String getDescription() {
        return description;
    }

    String getLocation() {
        return location;
    }

    String getSummary() {
        return summary;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setSummary(String summary) {
        this.summary = summary;
    }

    long getStart() {
        return start;
    }

    void setStart(long start) {
        this.start = start;
    }

    long getEnd() {
        return end;
    }

    void setEnd(long end) {
        this.end = end;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    boolean isStartDateValid() {
        return start != INVALID_TIME;
    }

    boolean isEndDateValid() {
        return end != INVALID_TIME;
    }

    void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    boolean isTransparent() {
        return transparency;
    }

    Recurrence getRecurrence() {
        return recurrence;
    }

    void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }
}
