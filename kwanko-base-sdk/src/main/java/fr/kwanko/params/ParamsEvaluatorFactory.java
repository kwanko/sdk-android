package fr.kwanko.params;

import fr.kwanko.SupportedFormats;

/**
 * SourceCode
 * Created by erusu on 5/16/2017.
 */

public class ParamsEvaluatorFactory {

    public static ParamsEvaluatorFactory instance(){
        return new ParamsEvaluatorFactory();
    }

    public ParamsEvaluator getParamsEvaluator(String format){
        switch (format){
            case SupportedFormats.FORMAT_NATIVE:
                return new NativeAdsTrackingParamsEvaluator();
            default:
                return new TrackingParamsEvaluator();
        }
    }
}
