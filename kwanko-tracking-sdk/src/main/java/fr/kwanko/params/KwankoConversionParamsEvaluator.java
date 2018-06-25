package fr.kwanko.params;

import android.content.Context;

import static fr.kwanko.params.KwankoConversion.*;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public class KwankoConversionParamsEvaluator extends ParamsEvaluator {

    private static final String [] REQUESTED_PARAMS = {USER_ID,ARGANN,ID_CIBLE};
    private static final String [] MANDATORY_PARAMS = {TRACKING_ID,IS_REPEATABLE,MODE};

    private final String [] mandatoryParams;

    KwankoConversionParamsEvaluator(String [] requestedParams,String [] mandatoryParams,
                                    ParamsRetrieverFactory factory) {
        super(requestedParams, factory,ParamsCacheSingleton.Instance.getParamCache());
        this.mandatoryParams = mandatoryParams;
    }

    public KwankoConversionParamsEvaluator() {
        this(REQUESTED_PARAMS,MANDATORY_PARAMS,new KwankoConversionParamsRetrieverFactoryImpl());
    }

    @Override
    public ParamMap process(Context context, ParamMap params) {
        checkMandatoryParams(mandatoryParams,params);
        return super.process(context, params);
    }

    private void checkMandatoryParams(String [] paramsKeys, ParamMap params){
        for(String key:paramsKeys){
            if(params.get(key) == null){
                throw new IllegalArgumentException("params does not contain "+key+
                        ", this param is mandatory");
            }
        }
    }
}
