package fr.kwanko.internal.nativeAds;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import fr.kwanko.ActivityLifecycle;
import fr.kwanko.AdRequest;
import fr.kwanko.bridge.OpenAdWebViewActivity;
import fr.kwanko.internal.model.NativeAdModel;
import fr.kwanko.internal.nativeAds.renderer.NativeAdRendererFactory;
import fr.kwanko.internal.nativeAds.renderer.NativeAdsRenderer;
import fr.kwanko.nativeads.ViewBinder;
import fr.kwanko.rest.KwankoAdRetriever;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class NativeAdController  implements KwankoAdRetriever.KwankoAdRetrieverListener<NativeAdModel>, ActivityLifecycle{

    private final ViewBinder binder;
    private KwankoAdRetriever retriever;
    private NativeAdRendererFactory rendererFactory;
    private Context context;

    public NativeAdController(ViewBinder binder, Context context){
        this.binder = binder;
        this.retriever = KwankoAdRetriever.Factory.createAdRetriever();
        this.rendererFactory = new NativeAdRendererFactory();
        this.context = context;
    }

    public void load(AdRequest request){
        retriever.retrieveAd(request,this);
    }

    @Override
    public void onResult(final NativeAdModel adModel) {
        NativeAdsRenderer renderer = rendererFactory.createRenderer();
        renderer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAdWebViewActivity.start(context,adModel.getUrlToOpen());
            }
        });
        renderer.render((Activity) context,binder,adModel);
    }

    @Override
    public void onResume() {
        //do nothing
    }

    @Override
    public void onPause() {
        //do nothing
    }

    @Override
    public void onDestroy() {
        //do nothing
    }
}
