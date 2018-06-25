package fr.kwanko.internal.nativeAds.renderer;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class NativeAdRendererFactory {

    public NativeAdsRenderer createRenderer(){
        return new SimpleNativeAdsRenderer();
    }
}
