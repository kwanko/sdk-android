package fr.kwanko.params;

import android.content.Context;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

abstract class ParamsRetriever {

    private final String paramKey;

    ParamsRetriever(){
        this(null);
    }

    ParamsRetriever(String paramKey){
        this.paramKey = paramKey;
    }


    abstract ParamValue retrieveParam(Context context);
}
