package fr.kwanko.internal.model;

import java.util.Map;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public class Mediation  {

    private String mediationTarget;
    private String adFormat;
    private Map<String,String>  params;

    public String getMediationTarget() {
        return mediationTarget;
    }

    public void setMediationTarget(String mediationTarget) {
        this.mediationTarget = mediationTarget;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getAdFormat() {
        return adFormat;
    }

    public void setAdFormat(String adFormat) {
        this.adFormat = adFormat;
    }
}
