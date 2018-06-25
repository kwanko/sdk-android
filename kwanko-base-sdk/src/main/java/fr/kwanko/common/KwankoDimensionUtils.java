package fr.kwanko.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by vfatu on 05.01.2017.
 */

public class KwankoDimensionUtils {

    private static float CACHED_PIXEL_RATIO = 0f;

    private KwankoDimensionUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static float getDpFromPixels(Context context, float pixels) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return pixels / displayMetrics.density;
    }

    public static float getPixelsFromDp(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float getPixelRatio(Context context){
        DisplayMetrics m = context.getResources().getDisplayMetrics();
        CACHED_PIXEL_RATIO = m.density;
        return m.density;
    }

    public static int scaleToPx(Context context, int size){
        float pr;
        if(!(Float.compare(CACHED_PIXEL_RATIO,0f) == 0)){
            pr = CACHED_PIXEL_RATIO;
        }else{
            pr = getPixelRatio(context);
        }
        return (int)(size*pr);
    }

    public static int scaleToServerSize(Context context, int size){
        float pr;
        if(!(Float.compare(CACHED_PIXEL_RATIO,0f) == 0)){
            pr = CACHED_PIXEL_RATIO;
        }else{
            pr = getPixelRatio(context);
        }
        return (int)(size/pr);
    }

}
