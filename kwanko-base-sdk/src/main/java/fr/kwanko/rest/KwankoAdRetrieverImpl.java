package fr.kwanko.rest;

import android.content.Context;
import android.location.Location;

import fr.kwanko.AdRequest;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.rest.network.RequestExecutable;
import fr.kwanko.rest.network.RequestExecutableFactory;
import fr.kwanko.services.LocationProvider;
import fr.kwanko.params.ParamValue;
import fr.kwanko.params.StringParamValue;
import fr.kwanko.params.TrackingParams;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * This class handles retrieving ads from server
 * Created by vfatu on 26.01.2017.
 */

class KwankoAdRetrieverImpl implements KwankoAdRetriever {

    private RequestExecutableFactory factory;
    private File extraResourcesFile;

    KwankoAdRetrieverImpl(){
        factory = RequestExecutableFactory.getInstance();
    }

    @Override
    public void retrieveAd(Context context, String slotId, KwankoAdRetrieverListener responseListener) {
        //create ad request empty tracking params
    }

    @Override
    public void retrieveAd(final AdRequest request, KwankoAdRetrieverListener<? extends AdModel> listener) {
        RequestExecutable executable = factory.newRequestExecutable(request);
        executable.setRequest(request,listener);
        executable.setExtraResourcesFile(extraResourcesFile);
        Executors.newCachedThreadPool().execute(executable);
    }

    @Override
    public void cancelAllRequest() {

    }

    @Override
    public void setExtraResourceFile(File file) {
        this.extraResourcesFile = file;
    }
}
