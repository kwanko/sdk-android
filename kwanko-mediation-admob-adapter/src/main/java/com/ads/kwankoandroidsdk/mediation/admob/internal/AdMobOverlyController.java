package com.ads.kwankoandroidsdk.mediation.admob.internal;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.mediation.admob.AdMobMediationParams;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.model.Mediation;
import fr.kwanko.internal.overly.BaseOverlyAdDisplayController;
import fr.kwanko.overly.OverlyAdException;
import fr.kwanko.overly.OverlyAdListener;

/**
 * SourceCode
 * This class is instantiated using reflection
 * Created by erusu on 5/23/2017.
 */

public class AdMobOverlyController extends BaseOverlyAdDisplayController {

    private InterstitialAd interstitialAd;

    public AdMobOverlyController(Context context, AdModel adModel,
                                 OverlyAdListener externalListener) {
        super(context, adModel, externalListener);
        requestInterstitial(adModel.getMediation());
    }

    private  void requestInterstitial(Mediation mediation){
        interstitialAd = new InterstitialAd(getContext());

        interstitialAd.setAdUnitId(mediation.getParams().get(AdMobMediationParams.UNIT_ID));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                getListener().onOverlyAdLoaded();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getListener().onOverlyAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                getListener().onError(new OverlyAdException(i));
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                getListener().onOverlyAdOpen();
            }
        });

        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public boolean isLoaded() {
        return interstitialAd.isLoaded();
    }

    @Override
    public void show()  {
        interstitialAd.show();
    }

    @Override
    public void cancel() {
        KwankoLog.w("AdMobInterstitial ad does not support cancel method");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interstitialAd.setAdListener(null);
        interstitialAd = null;
    }
}
