package fr.kwanko.internal.nativeAds.renderer;

import android.app.Activity;
import android.view.View;

import fr.kwanko.internal.model.NativeAdModel;
import fr.kwanko.nativeads.ViewBinder;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public interface NativeAdsRenderer {

    public void render(Activity context, ViewBinder binder, NativeAdModel model);

    View createView(ViewBinder binder);

    void setOnClickListener(View.OnClickListener listener);
}
