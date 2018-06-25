package fr.kwanko.params;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import static fr.kwanko.params.KwankoConversion.ARGANN;
import static fr.kwanko.params.KwankoConversion.ID_CIBLE;
import static fr.kwanko.params.KwankoConversion.USER_ID;
import static fr.kwanko.services.SharesPreferencesHelper.with;

/**
 * SourceCode
 * Created by erusu on 3/17/2017.
 */

class KwankoConversionParamsRetrieverFactoryImpl implements ParamsRetrieverFactory {

    private  final Map<String,ParamsRetriever> retrievers;

    KwankoConversionParamsRetrieverFactoryImpl(){
        retrievers = new HashMap<>();
        retrievers.put(USER_ID, new UserIdRetriever(USER_ID));
        retrievers.put(ARGANN, new UserIdRetriever(USER_ID));
        retrievers.put(ID_CIBLE, new IdCibleRetriever());
    }

    @Override
    public ParamsRetriever getParamRetriever(String paramKey) {
        return retrievers.get(paramKey);
    }

    private static class IdCibleRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            String id = with(context).getIdCible();
            return id != null ? new StringParamValue(id) : null;
        }
    }

}
