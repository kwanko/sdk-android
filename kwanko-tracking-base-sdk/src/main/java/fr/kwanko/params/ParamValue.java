package fr.kwanko.params;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class ParamValue {

    private boolean isFromSdk;
    private boolean cacheable = true;

    public ParamValue(){
    }

    public ParamValue(boolean cacheable){
        this.cacheable = cacheable;
    }

    public boolean isFromSdk() {
        return isFromSdk;
    }

    void setFromSdk(boolean fromSdk) {
        isFromSdk = fromSdk;
    }

    void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    boolean isCacheable() {
        return cacheable;
    }
}
