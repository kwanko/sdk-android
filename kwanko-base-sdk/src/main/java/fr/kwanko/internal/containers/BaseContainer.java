package fr.kwanko.internal.containers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import fr.kwanko.ActivityLifecycle;
import fr.kwanko.KwankoAd;
import fr.kwanko.internal.content.ContentController;

/**
 * SourceCode
 * Created by erusu on 3/28/2017.
 */

public abstract class BaseContainer extends FrameLayout implements ActivityLifecycle {

    private ContentController contentController;

    public BaseContainer(Context context) {
        super(context);
    }

    public void setContentController(ContentController contentController){
        this.contentController = contentController;
        onSetContentController();
    }

    protected abstract void onSetContentController();

    public KwankoAd getAdView(){
        if(getParent() instanceof KwankoAd){
            return (KwankoAd) getParent();
        }
        throw new IllegalStateException("ad container is not placed inside an KwankoAd object");
    }

    public void show(){
        getAdView().show();
    }

    @Override
    public void onPause() {
        contentController.onPause();
    }

    @Override
    public void onResume() {
        contentController.onResume();
    }

    @Override
    public void onDestroy() {
        contentController.onDestroy();
    }

    public void addContentView(View view){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view,0,params);
    }

    public ContentController getContentController() {
        return contentController;
    }


}
