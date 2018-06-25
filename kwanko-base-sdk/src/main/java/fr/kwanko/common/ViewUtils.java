package fr.kwanko.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.Window;

import fr.kwanko.params.AdSizeStrategy;
import fr.kwanko.params.TrackingParamsUtils;

/**
 * SourceCode
 * Created by erusu on 2/27/2017.
 */

public class ViewUtils {

    private ViewUtils(){
        throw new AssertionError();
    }

    public static ViewGroup getRootView(Context context){
        if(context instanceof AppCompatActivity) {
            return (ViewGroup) ((AppCompatActivity)context).findViewById(android.R.id.content);
        }else if(context instanceof Activity){
            return (ViewGroup) ((Activity)context).findViewById(android.R.id.content);
        }else if(context instanceof ContextThemeWrapper) {
            Context baseContext = ((ContextThemeWrapper) context).getBaseContext();
            return getRootView(baseContext);
        }else{
            throw new IllegalArgumentException("context is not instance of activity or " +
                    "AppCompatActivity");
        }
    }

    public static int getStatusBarHeight(Context context){
        Rect rectangle = new Rect();
        Window window = getWindow(context);
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        if(rectangle.top > 0){
            return rectangle.top;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean isActionBar(){
        return false;
    }

    public  static Window getWindow(Context context){
        if(context instanceof AppCompatActivity) {
            return  ((AppCompatActivity)context).getWindow();
        }else if(context instanceof Activity){
            return ((Activity)context).getWindow();
        }else if(context instanceof ContextThemeWrapper) {
            Context baseContext = ((ContextThemeWrapper) context).getBaseContext();
            return getWindow(baseContext);
        }else{
            throw new IllegalArgumentException("context is not instance of activity or " +
                    "AppCompatActivity");
        }
    }

    public static int [] getSize(Context context,int w, int h, String sizeStrategy){
        switch (sizeStrategy){
            case AdSizeStrategy.PIXEL:
                return new int[]{w,h};
            case AdSizeStrategy.RATIO:
                float ri = w/(float)h;
                int maxWidth = TrackingParamsUtils.getScreenWidth();
                int maxHeight = TrackingParamsUtils.getScreenHeight();
                float rs = maxWidth/(float)maxHeight;
                float width = rs>ri?w*maxHeight/(float)h:maxWidth;
                float height = rs>ri?maxHeight:h*maxWidth/(float)w;



                width = KwankoDimensionUtils.scaleToServerSize(context, (int)width);
                height = KwankoDimensionUtils.scaleToServerSize(context, (int)height);
                return new int[]{(int)width, (int)height};
            default:
                throw new IllegalArgumentException("illegal argument sizeStrategy = "+sizeStrategy);
        }
    }
}
