package fr.kwanko;

import fr.kwanko.params.ParamMap;

/**
 * SourceCode
 * Created by erusu on 3/10/2017.
 */

public class AdRequest {

    private ParamMap paramMap;
    private String slotId;
    private String format;
    private boolean canceled;
    private Long refreshDelay;

    private AdRequest() {
        //private constructor
    }

    public ParamMap getParamMap() {
        return paramMap;
    }

    public void setParamMap(ParamMap paramMap) {
        this.paramMap = paramMap;
    }

    public String getSlotId() {
        return slotId;
    }

    void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public synchronized void cancel(){
        this.canceled = true;
    }

    public synchronized boolean isCanceled(){
        return canceled;
    }

    public Long getRefreshDelay() {
        return refreshDelay;
    }

    @Override
    public String toString() {
        return "{" +
                ",\"params\":" +
                paramMap.toString() +
                "}";
    }

    public static class Builder{

        private String slotId;
        private ParamMap params;
        private String format;
        private Long refreshDelay;

        public Builder trackingParams(ParamMap params){
            this.params = params;
            return this;
        }

        public Builder slotId(String slotId){
            this.slotId = slotId;
            return this;
        }

        public Builder setFormat(String format){
            this.format = format;
            return this;
        }

        public Builder refreshDelay(Long refreshDelay){
            this.refreshDelay = refreshDelay;
            return this;
        }
        public AdRequest build(){
            AdRequest request = new AdRequest();
            request.paramMap = params;
            request.slotId = slotId;
            request.format = format;
            request.refreshDelay = refreshDelay;
            return request;
        }
    }
}