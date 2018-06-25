package fr.kwanko.internal.nativeAds.renderer;

import android.app.Activity;
import android.content.Context;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

class RendererUtils {

    private RendererUtils(){
        throw new AssertionError("instance is not allowed");
    }

    @SuppressWarnings("unchecked")
    static <T> T findView(Activity activity, int id){
        return (T) activity.findViewById(id);
    }
}
