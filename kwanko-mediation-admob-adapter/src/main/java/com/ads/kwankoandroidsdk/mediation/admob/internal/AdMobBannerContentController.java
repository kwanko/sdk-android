package com.ads.kwankoandroidsdk.mediation.admob.internal;

import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.content.ContentController;
import fr.kwanko.internal.mediation.admob.AdMobMediationParams;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * This class is instantiated using reflection
 * Created by erusu on 5/31/2017.
 */

public class AdMobBannerContentController extends ContentController {

    private AdView adView;

    public AdMobBannerContentController(BaseContainer container) {
        super(container,null);
    }

    @Override
    public void onCloseButtonClicked(View view) {
        //no impl here
    }

    @Override
    public void loadContentBasedOn(AdModel adModel) {
        /*MobileAds.initialize(getContext().getApplicationContext(),
                adModel.getMediation().getParams().get(AdMobMediationParams.APPLICATION_ID));*/
        container().show();
        adView = new AdView(getContext().getApplicationContext());
        container().addContentView(adView);
        adView.setAdUnitId(adModel.getMediation().getParams().get(AdMobMediationParams.UNIT_ID));
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("kwanko","adMob content controller onDestroy");
        if(adView != null){
            adView.destroy();
            adView = null;
        }
    }
}
