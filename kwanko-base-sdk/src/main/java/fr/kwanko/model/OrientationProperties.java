package fr.kwanko.model;

/**
 * Created by vfatu on 25.01.2017.
 */

public class OrientationProperties {

    private boolean allowOrientationChange;
    private String forceOrientation;

    public boolean isAllowOrientationChange() {
        return allowOrientationChange;
    }

    public void setAllowOrientationChange(boolean allowOrientationChange) {
        this.allowOrientationChange = allowOrientationChange;
    }

    public String getForceOrientation() {
        return forceOrientation;
    }

    public void setForceOrientation(String forceOrientation) {
        this.forceOrientation = forceOrientation;
    }
}
