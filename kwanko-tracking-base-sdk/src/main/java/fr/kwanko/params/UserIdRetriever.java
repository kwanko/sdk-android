package fr.kwanko.params;

import android.content.Context;

import fr.kwanko.services.GplayServicesUtils;

/**
 * SourceCode
 * Created by erusu on 3/28/2017.
 */

class UserIdRetriever extends ParamsRetriever{

    UserIdRetriever(String paramKey) {
        super(paramKey);
    }

    @Override
    ParamValue retrieveParam(Context context) {
        return new StringParamValue(GplayServicesUtils.getAdvertisingId(context), true);
    }

}
