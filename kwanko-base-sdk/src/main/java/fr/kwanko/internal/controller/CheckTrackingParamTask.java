package fr.kwanko.internal.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import fr.kwanko.AdRequest;
import fr.kwanko.params.ParamsEvaluator;
import fr.kwanko.params.ParamsEvaluatorFactory;
import fr.kwanko.params.TrackingParams;

/**
 * SourceCode
 * Created by erusu on 6/12/2017.
 */

public class CheckTrackingParamTask extends AsyncTask<AdRequest,Void,AdRequest> {


    private Context context;

    public CheckTrackingParamTask(Context context) {
        this.context = context;
    }

    @Override
    protected AdRequest doInBackground(AdRequest... params) {
        if(isCancelled()){
            return null;
        }
        AdRequest request = params[0];
        ParamsEvaluator evaluator = ParamsEvaluatorFactory.instance()
                .getParamsEvaluator(request.getFormat());
        if (request.getParamMap() == null) {
            request.setParamMap(new TrackingParams.Builder().build());
        }
        request.setParamMap(evaluator.process(context, request.getParamMap()));
        if(!isCancelled()) {
            return request;
        }else{
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        context = null;
    }
}
