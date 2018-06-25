package fr.kwanko.params;

import java.util.Map;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 3/20/2017.
 */

public class KwankoRemarketing extends KwankoConversion {

    public static String EVENT_ID = "argann";
    public static String AMOUNT = "argmon";
    public static String CURRENCY = "nacur";
    public static String PAYNAME = "argmodp";

    private KwankoRemarketing(Map<String, ParamValue> params) {
        super(params);
    }

    public static class Builder extends KwankoConversion.Builder{

        public Builder setEventId(String eventId){
            Preconditions.checkNotNull(eventId);
            return setParam(EVENT_ID, new StringParamValue(eventId));
        }

        public Builder setAmount(String amount){
            Preconditions.checkNotNull(amount);
            return setParam(AMOUNT,new StringParamValue(amount));
        }

        public Builder setCurrency(String currency){
            Preconditions.checkNotNull(currency);
            return setParam(CURRENCY, new StringParamValue(currency));
        }

        public Builder setPaymentMethod(String paymentMethod){
            Preconditions.checkNotNull(paymentMethod);
            return setParam(PAYNAME, new StringParamValue(paymentMethod));
        }

        public Builder setCustomParams(Map<String,String> customParams){
            Preconditions.checkNotNull(customParams);
            for(Map.Entry<String,String> entry:customParams.entrySet()){
                setParam(entry.getKey(),new StringParamValue(entry.getValue()));
            }
            return this;
        }

        @Override
        public Builder setLabel(String action) {
            super.setLabel(action);
            return this;
        }

        @Override
        public Builder setConversionId(String trackingId) {
            super.setConversionId(trackingId);
            return this;
        }

        @Override
        public Builder setAlternativeId(String alternativeId) {
            super.setAlternativeId(alternativeId);
            return this;
        }

        @Override
        public Builder setIsRepeatable(boolean isRepeatable) {
            super.setIsRepeatable(isRepeatable);
            return this;
        }

        public KwankoRemarketing build() {
            return new KwankoRemarketing(buildMap);
        }
    }
}
