package fr.kwanko.model;

/**
 * Created by vfatu on 25.01.2017.
 */

public class ExpandProperties {

    private SizeProperties sizeProperties;
    private boolean useCustomClose;

    public SizeProperties getSizeProperties() {
        return sizeProperties;
    }

    public void setSizeProperties(SizeProperties sizeProperties) {
        this.sizeProperties = sizeProperties;
    }

    public boolean isUseCustomClose() {
        return useCustomClose;
    }

    public void setUseCustomClose(boolean useCustomClose) {
        this.useCustomClose = useCustomClose;
    }
}
