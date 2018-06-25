package fr.kwanko.internal.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import fr.kwanko.AdRequest;
import fr.kwanko.KwankoAdType;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.content.ContentController;
import fr.kwanko.internal.factory.ContainerFactory;
import fr.kwanko.internal.factory.ContentControllerFactory;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.params.StringParamValue;
import fr.kwanko.rest.network.HtmlContentProcessors;
import fr.kwanko.services.LocationProvider;

import static fr.kwanko.SupportedFormats.*;
import static fr.kwanko.common.KwankoDimensionUtils.*;
import static fr.kwanko.params.TrackingParams.*;

/**
 * SourceCode
 * Created by erusu on 3/28/2017.
 */

class KwankoAdController extends BaseAdLoadController {

    private final KwankoAdType adView;
    private ContainerFactory containerFactory;
    private ContentControllerFactory contentControllerFactory;
    private Handler handler = new Handler();
    private AdRequest lastRequest;
    private ContentController contentController;

    KwankoAdController(KwankoAdType adView, ContainerFactory containerFactory,
                       ContentControllerFactory contentControllerFactory){
        this.adView = adView;
        this.containerFactory = containerFactory;
        this.contentControllerFactory = contentControllerFactory;
    }

    public void load(final AdRequest adRequest) {
        stopResfreshLoop();
        super.load(adRequest);
        lastRequest = adRequest;
    }

    protected Context getContext(){
        return adView.getContext();
    }

    @Override
    public void onAttach() {
        // do nothing
    }

    @Override
    void evaluateAndCompleteAdRequest(AdRequest request) {
        super.evaluateAndCompleteAdRequest(request);
        if (FORMAT_BANNER.equals(request.getFormat())
                && (request.getParamMap().get(AD_HEIGHT) == null || request.getParamMap().get(AD_HEIGHT) == null)) {
            request.getParamMap().put(AD_HEIGHT,
                    new StringParamValue(scaleToServerSize(getContext(),adView.getAdHeight())));
            request.getParamMap().put(AD_WIDTH,
                    new StringParamValue(scaleToServerSize(getContext(),adView.getAdWidth())));
        }
    }

    public void onResult(AdModel adModel){
        if(adModel.getFormat().equals(FORMAT_NATIVE)){
            loadModelIntoContainers(adModel);
        }else {
            processModel(adModel, getContext());
        }
        if (lastRequest != null && lastRequest.getRefreshDelay() != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    load(lastRequest);
                }
            }, lastRequest.getRefreshDelay() * 1000);
        }
    }

    private void processModel(final AdModel model, final Context context) {
        /*if (model.isForceGeoloc()) {
            LocationProvider.requestLocation(context, true, new LocationProvider.LocationListener() {
                @Override
                public void onLocationResult(Location location) {
                    onLocationRequest(model, context, location);
                }
            });
        } else {
            continueWithHtmlProcess(model, context, HtmlContentProcessors.instance());
        }*/
        LocationProvider.requestLocation(context,model.isForceGeoloc(), new LocationProvider.LocationListener() {
            @Override
            public void onLocationResult(Location argLocation) {
                Location location = argLocation;
                if (location == null) {
                    location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(model.getFallbackLat());
                    location.setLongitude(model.getFallbackLong());
                }

                HtmlContentProcessors processors = HtmlContentProcessors.instance(context, location);
                continueWithHtmlProcess(model, processors);
            }
        });
    }

    /*private void onLocationRequest(final AdModel model, final Context context, Location argLocation) {
        Location location = argLocation;
        if (location == null) {
            location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(model.getFallbackLat());
            location.setLongitude(model.getFallbackLong());
        }

        HtmlContentProcessors processors = HtmlContentProcessors.instance();
        processors.addCustomHtmlProcessor(new HtmlContentProcessors.ReplaceLocationMacro(location));
        continueWithHtmlProcess(model, context, processors);
    }*/

    private void continueWithHtmlProcess(AdModel model,
                                         HtmlContentProcessors processors) {
        String processedHtml = processors.processHtml(model.getHtml());
        model.setHtml(processedHtml);

        for (String key : model.getMraidEvents().keySet()) {
            String url = model.getMraidEvents().get(key);
            String processedUrl = processors.processHtml(url);
            model.getMraidEvents().put(key, processedUrl);
        }

        loadModelIntoContainers(model);
    }

    private void loadModelIntoContainers(AdModel adModel){
        if (contentController == null) {
            BaseContainer container = containerFactory.createContainer(adModel, getContext());
            adView.integrateAdContainer(container);
            contentController = contentControllerFactory.create(adModel, container);
            if (contentController == null) {
                adView.hide();
                return;
            }
            setContentControllerToContainer(container, contentController);
        }
        contentController.loadContentBasedOn(adModel);
    }

    void setContentControllerToContainer(BaseContainer container,ContentController contentController){
        container.setContentController(contentController);
    }

    private void stopResfreshLoop() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopResfreshLoop();
        this.lastRequest = null;
    }

    public void onPause() {
        stopResfreshLoop();
    }

    public void onResume() {
        if (lastRequest != null && lastRequest.getRefreshDelay() != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    load(lastRequest);
                }
            });
        }
    }
}
