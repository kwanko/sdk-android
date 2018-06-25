package fr.kwanko.params;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public class ParamsEvaluator {

    private String[] requestedParams;
    private ParamsRetrieverFactory factory;
    private ParamsCache cache;

    ParamsEvaluator(@NonNull String[] requestedParams,
                    @NonNull ParamsRetrieverFactory factory) {
        this(requestedParams, factory, null);
    }

    ParamsEvaluator(@NonNull String[] requestedParams,
                    @NonNull ParamsRetrieverFactory factory,
                    ParamsCache cache) {
        this.requestedParams = requestedParams;
        this.factory = factory;
        this.cache = cache;
    }

    public ParamMap process(Context context, ParamMap params) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(params);
        TrackingParams.Builder builder = new TrackingParams.Builder(params);
        for (String paramKey : requestedParams) {
            if (params.hasParam(paramKey)) {
                continue;
            }
            if(cache != null && cache.hasParam(paramKey)){
                builder.setParam(paramKey, cache.getParam(paramKey));
                continue;
            }
            ParamsRetriever retriever = factory.getParamRetriever(paramKey);
            ParamValue value =  retriever.retrieveParam(context);
            if(value == null){
                continue;
            }
            builder.setParam(paramKey,value);
            if(cache != null && value.isCacheable()){
                cache.setParam(paramKey,value);
            }
        }
        return builder.build();
    }
}
