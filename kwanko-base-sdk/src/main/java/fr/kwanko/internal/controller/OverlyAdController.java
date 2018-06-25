package fr.kwanko.internal.controller;

import android.content.Context;

import fr.kwanko.internal.overly.BaseOverlyAdDisplayController;
import fr.kwanko.internal.overly.OverlyAdDisplayControllerFactory;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.overly.OverlyAdListener;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public class OverlyAdController extends BaseAdLoadController {

    private Context context;
    private BaseOverlyAdDisplayController displayController;
    private OverlyAdDisplayControllerFactory displayControllerFactory;
    private OverlyAdListener listener;

    public OverlyAdController(Context context,
                              OverlyAdDisplayControllerFactory factory){
        this.context = context;
        this.displayControllerFactory = factory;
    }

    @Override
    public void onResult(AdModel adModel) {
        if (displayController != null) {
            displayController.onDestroy();
        }
        displayController = displayControllerFactory.createDisplayController(getContext(),
                adModel,listener);
        if(displayController == null && listener!= null){
            listener.onError(new IllegalStateException("no display controller available for " +
                    "the received adModel"));
        }
        if(listener != null) {
            listener.onOverlyAdLoaded();
        }
    }

    @Override
    protected Context getContext() {
        return context;
    }

    public void show(){
        if(displayController == null){
            throw new IllegalStateException("please wait for the ad to load, then call show method");
        }
        displayController.show();
    }

    public void cancel(){
        onDestroy();
        if(displayController != null){
            displayController.cancel();
        }
    }

    public void setOverlyAdListener(OverlyAdListener listener){
        this.listener = listener;
    }

    public boolean isLoaded(){
        return displayController != null;
    }

    @Override
    public void onDestroy() {
        if(displayController != null){
            displayController.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach() {
        //do nothing
    }

    @Override
    public void onResume() {
        //do nothing
    }

    @Override
    public void onPause() {
        //do nothing
    }
}
