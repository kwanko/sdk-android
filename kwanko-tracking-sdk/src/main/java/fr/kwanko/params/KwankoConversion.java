package fr.kwanko.params;

import java.util.Map;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public class KwankoConversion extends ParamMap{

    public static final String TRACKING_ID = "mclic";
    public static final String ACTION = "action";
    public static final String ALTERNATIVE_ID = "altid";
    public static final String IS_REPEATABLE  = "isRepeatable";
    static final String USER_ID = "userId";
    static final String ARGANN = "argann";
    static final String ID_CIBLE = "cible";
    public static final String MODE = "mode";
    private static final String MODE_INAPP = "inapp";

    KwankoConversion(Map<String, ParamValue> params) {
        super(params);
        put(MODE, new StringParamValue(MODE_INAPP));
    }

    public static class Builder extends ParamMap.Builder{

        public Builder() {
            super();
        }

        public Builder(ParamMap params) {
            super(params);
        }

        public Builder setConversionId(String trackingId){
            Preconditions.checkNotNull(trackingId);
            return setParam(TRACKING_ID,new StringParamValue(trackingId));
        }

        public Builder setLabel(String action){
            Preconditions.checkNotNull(action);
            return setParam(ACTION, new StringParamValue(action));
        }

        public Builder setAlternativeId(String alternativeId){
            Preconditions.checkNotNull(alternativeId);
            return setParam(ALTERNATIVE_ID, new StringParamValue(alternativeId));
        }

        public Builder setIsRepeatable(boolean isRepeatable){
            return setParam(IS_REPEATABLE, new StringParamValue(isRepeatable));
        }

        public KwankoConversion build(){
            return new KwankoConversion(buildMap);
        }
    }
}
