package fr.kwanko.database;

/**
 * SourceCode
 * Created by erusu on 3/17/2017.
 */

public class Conversion {

    private String trackingId;
    private boolean isRepeatable;

    public Conversion(String trackingId, boolean isRepeatable) {
        this.trackingId = trackingId;
        this.isRepeatable = isRepeatable;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }
}
