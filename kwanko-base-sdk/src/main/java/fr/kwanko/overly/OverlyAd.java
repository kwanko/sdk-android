package fr.kwanko.overly;

import android.content.Context;

import fr.kwanko.AdRequest;
import fr.kwanko.SupportedFormats;
import fr.kwanko.internal.controller.OverlyAdController;
import fr.kwanko.internal.overly.OverlyAdDisplayControllerFactory;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public class OverlyAd {

    private OverlyAdController adController;

    public OverlyAd(Context context){
        adController = new OverlyAdController(context,
                OverlyAdDisplayControllerFactory.DEFAULT_FACTORY);
    }

    public void load(AdRequest request){
        request.setFormat(SupportedFormats.FORMAT_OVERLY);
        adController.load(request);
    }

    public boolean isLoaded(){
        return adController.isLoaded();
    }

    public void show(){
        adController.show();
    }

    public void setListener(OverlyAdListener listener){
        adController.setOverlyAdListener(listener);
    }

    public void cancel(){
        adController.cancel();
    }

    public void onDestroy(){
        adController.onDestroy();
        adController = null;
    }

    public void onResume() {
        adController.onResume();
    }

    public void onPause() {
        adController.onPause();
    }
}
