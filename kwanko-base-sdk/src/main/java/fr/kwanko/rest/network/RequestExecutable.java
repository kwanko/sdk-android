package fr.kwanko.rest.network;

import android.os.Handler;
import android.support.annotation.NonNull;


import java.io.File;

import fr.kwanko.AdRequest;
import fr.kwanko.common.Preconditions;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.params.ParamsEvaluator;
import fr.kwanko.params.TrackingParams;
import fr.kwanko.params.TrackingParamsEvaluator;
import fr.kwanko.rest.KwankoAdRetriever;

/**
 * SourceCode
 * Created by erusu on 3/10/2017.
 */

public abstract class RequestExecutable implements Runnable{


    protected static final String DEFAULT_METHOD = "POST";
    private AdRequest request;
    private Handler handler;
    private KwankoAdRetriever.KwankoAdRetrieverListener listener;
    private File extraResourcesFile;

    public RequestExecutable(){
        handler = new Handler();
    }

    public void setRequest(@NonNull AdRequest request,
                           KwankoAdRetriever.KwankoAdRetrieverListener listener) {
        Preconditions.checkNotNull(request);
        this.request = request;
        this.listener = listener;
    }

    protected abstract void performRequest(AdRequest request);

    @Override
    public void run() {
        Preconditions.checkNotNull(request);
        performRequest(request);
    }

    @SuppressWarnings("unchecked")
    protected void deliverResult(final AdModel model){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null) {
                    listener.onResult(model);
                }
            }
        });
    }

    public File getExtraResourcesFile() {
        return extraResourcesFile;
    }

    public void setExtraResourcesFile(File extraResourcesFile) {
        this.extraResourcesFile = extraResourcesFile;
    }
}