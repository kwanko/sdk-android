package fr.kwanko;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import fr.kwanko.common.Preconditions;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.controller.AdController;
import fr.kwanko.internal.controller.ControllerFactory;
import fr.kwanko.internal.factory.ContainerFactoryImpl;
import fr.kwanko.internal.factory.ContentControllerFactoryImpl;
import fr.kwanko.params.TrackingParamsUtils;

/**
 * This is just a shell for the adContainer. The ad logic is implemented in the adContainer.
 * The adContainer will have a polymorphic behavior - it will manifest differently based on the
 * container implementation.
 * Created by vfatu on 13.02.2017 edited by erusu on 02/27/2017.
 */

public class KwankoAd extends FrameLayout implements ActivityLifecycle,KwankoAdType {

    private AdController adController;
    private BaseContainer container;
    private FrameLayout.LayoutParams defaultParams;

    public KwankoAd(Context context, ControllerFactory factory) {
        super(context);
        init();
        createController(factory);
        adController.onAttach();
    }

    public KwankoAd(Context context){
        this(context,(AttributeSet) null);
    }

    public KwankoAd(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        createController(ControllerFactory.DEFAULT_FACTORY);
        adController.onAttach();
    }

    private void init(){
        setVisibility(View.GONE);
        defaultParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void createController(ControllerFactory factory){
        adController = factory.createAdController(
                this,
                new ContainerFactoryImpl(),
                new ContentControllerFactoryImpl());
    }

    public void load(String slotId){
        adController.load(slotId);
    }

    public void load(AdRequest request){
        adController.load(request);

    }

    @Override
    public void integrateAdContainer(BaseContainer container){
        Preconditions.checkNotNull(container);
        this.container = container;
        addView(container,defaultParams);
    }

    public void hide(){
        setVisibility(View.GONE);
    }

    public void show(){
        if(isEnabled()) {
            setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPause() {
        if(container != null) {
            container.onPause();
        }
        adController.onPause();
    }

    @Override
    public void onResume() {
        if(container!= null) {
            container.onResume();
        }
        adController.onResume();
    }

    @Override
    public void onDestroy() {
        if(container != null) {
            container.onDestroy();
        }
        adController.onDestroy();
        adController = null;
        container = null;
    }

    @Override
    public int getAdWidth() {
        switch (getLayoutParams().width){
            case ViewGroup.LayoutParams.MATCH_PARENT:
                return TrackingParamsUtils.getScreenWidth();
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                throw new IllegalArgumentException("width of the banner ad can not be wrap_content");
            default:
                return getLayoutParams().width;
        }
    }

    @Override
    public int getAdHeight() {
        return getLayoutParams().height;
    }


}
