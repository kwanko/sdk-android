package fr.kwanko.internal.controller;

import android.content.Context;
import android.location.Location;

import fr.kwanko.AdRequest;
import fr.kwanko.AdView;
import fr.kwanko.SupportedFormats;
import fr.kwanko.common.KwankoBitmapUtils;
import fr.kwanko.common.Preconditions;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.params.ParamMap;
import fr.kwanko.params.ParamValue;
import fr.kwanko.params.StringParamValue;
import fr.kwanko.params.TrackingParams;
import fr.kwanko.rest.KwankoAdRetriever;
import fr.kwanko.services.LocationProvider;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

abstract class BaseAdLoadController implements KwankoAdRetriever.KwankoAdRetrieverListener<AdModel>,
        AdController {

    private KwankoAdRetriever retriever;
    private CheckTrackingParamTask checkTrackingParamTask;


    public void load(String slotId){
        Preconditions.checkNotNull(slotId);
        ParamMap params = new TrackingParams.Builder().build();
        load(new AdRequest.Builder()
                .trackingParams(params)
                .slotId(slotId)
                .build());
    }

    public void load(AdRequest adRequest){
        Preconditions.checkNotNull(adRequest);
        evaluateAndCompleteAdRequest(adRequest);
        checkTrackingParamsThenRequestLocation(adRequest);
    }

    void evaluateAndCompleteAdRequest(AdRequest request){
        if(request.getSlotId() == null){
            throw new IllegalArgumentException("impossible to load an ad without slotId");
        }
        if(request.getParamMap() == null){
            request.setParamMap(new TrackingParams.Builder().build());
        }
        if(request.getFormat() == null){
            request.setFormat(SupportedFormats.FORMAT_BANNER);
        }
    }

    protected void checkTrackingParamsThenRequestLocation(AdRequest request) {
        if(checkTrackingParamTask != null){
            checkTrackingParamTask.cancel(true);
        }
        checkTrackingParamTask = new CheckTrackingParamTask(getContext()){
            @Override
            protected void onPostExecute(AdRequest request) {
                checkLocationAndPerformRequest(request);
            }
        };
        checkTrackingParamTask.execute(request);
    }

    private void checkLocationAndPerformRequest(final AdRequest request){
        ParamValue value =  request.getParamMap().get(TrackingParams.FORCE_GEOLOC);
        if(value == null){
            retrieveAdAfterLocationRequest(request);
            return;
        }
        boolean forceGeoloc = Boolean.valueOf(value.toString());

        LocationProvider.requestLocation(getContext(), forceGeoloc, new LocationProvider.LocationListener() {
            @Override
            public void onLocationResult(Location location) {
                if (location != null) {
                    request.getParamMap().put(TrackingParams.LAT,
                            new StringParamValue(location.getLatitude()));
                    request.getParamMap().put(TrackingParams.LONG,
                            new StringParamValue(location.getLongitude()));
                }
                BaseAdLoadController.this.retrieveAdAfterLocationRequest(request);
            }
        });
    }

    private void retrieveAdAfterLocationRequest(final AdRequest adRequest){
        retriever = KwankoAdRetriever.Factory.createAdRetriever();
        retriever.setExtraResourceFile(KwankoBitmapUtils.getCloseBtnFile(getContext()));
        retriever.retrieveAd(adRequest,this);
    }

    protected abstract Context getContext();

    public void onDestroy(){
        if(retriever != null){
            retriever.cancelAllRequest();
        }
        if(checkTrackingParamTask != null){
            checkTrackingParamTask.cancel(true);
            checkTrackingParamTask = null;
        }
    }

    @Override
    public void onClose() {
        //do nothing
    }

    @Override
    public boolean allowAutoShow() {
        return true;
    }
}
