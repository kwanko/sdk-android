package fr.kwanko.params;

import static fr.kwanko.params.KwankoRemarketing.*;

/**
 * SourceCode
 * Created by erusu on 3/20/2017.
 */

public class KwankoRemarketingParamsEvaluator extends KwankoConversionParamsEvaluator {

    private static final String [] REQUESTED_PARAMS = {USER_ID,ARGANN,ID_CIBLE};
    private static final String [] MANDATORY = new String[]{TRACKING_ID,EVENT_ID,AMOUNT,CURRENCY,MODE,IS_REPEATABLE};

    public KwankoRemarketingParamsEvaluator(){
        super(REQUESTED_PARAMS,MANDATORY,new KwankoConversionParamsRetrieverFactoryImpl());
    }


}
