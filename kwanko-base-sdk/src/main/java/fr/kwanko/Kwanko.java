package fr.kwanko;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import fr.kwanko.common.Preconditions;
import fr.kwanko.common.ViewUtils;
import fr.kwanko.internal.controller.ControllerFactory;
import fr.kwanko.internal.nativeAds.NativeAdController;
import fr.kwanko.nativeads.ViewBinder;
import fr.kwanko.params.ParamMap;
import fr.kwanko.params.Position;
import fr.kwanko.params.TrackingParams;

/**
 * SourceCode
 * Helper class to inject overly ad into an activity.
 * This class will extend in the future to inject other types of ads into
 * a container.
 * Created by erusu on 2/27/2017.
 */

public final class Kwanko {

    private ActivityLifecycleObserver lifecycle;
    private AdRequest request;
    private Context context;

    private Kwanko(Context  context){
        this.context = context;
    }

    public static Kwanko with(Context context){
        Preconditions.checkNotNull(context);
        if(context instanceof Activity){
            return new Kwanko(context);
        }else{
            throw new IllegalArgumentException("please pass an context which is activity");
        }

    }

    public Kwanko lifecycle(@NonNull ActivityLifecycleObserver lifecycle){
        Preconditions.checkNotNull(lifecycle);
        this.lifecycle = lifecycle;
        return this;
    }

    public RequestCreator request(AdRequest request) {
        Preconditions.checkNotNull(lifecycle);
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.getSlotId());
        this.request = request;
        if(this.request.getParamMap() == null){
            this.request.setParamMap(new TrackingParams
                    .Builder()
                    .build());
        }
        return new RequestCreator(this);
    }

    public RequestCreator request(@NonNull String slotId){
        Preconditions.checkNotNull(slotId);
        return request(new AdRequest.Builder()
                    .slotId(slotId)
                    .build());
    }

    public static final class RequestCreator {

        private Kwanko kwanko;

        private RequestCreator(Kwanko kwanko){
            this.kwanko = kwanko;
        }

        public void into(Activity activity) {
            into(ViewUtils.getRootView(activity));
        }

        public void into(AppCompatActivity activity) {
            into(ViewUtils.getRootView(activity));
        }

        private void into(ViewGroup rootView){
            KwankoAd ad  = new KwankoAd(rootView.getContext(), ControllerFactory.DEFAULT_FACTORY);
            kwanko.lifecycle.forwardEventsTo(ad);
            ad.setBackgroundColor(ContextCompat.getColor(rootView.getContext(),android.R.color.holo_green_dark));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = gravityFromParamPosition(kwanko.request.getParamMap());
            if(isPositionTop()){
                boolean actionBarIsPresent = rootView.getParent() instanceof ActionBarOverlayLayout;

                if(!actionBarIsPresent && translucentStatusBar()) {
                    params.topMargin = ViewUtils.getStatusBarHeight(rootView.getContext());
                }
            }
            ad.setBackgroundColor(ContextCompat.getColor(rootView.getContext(),android.R.color.white));
            rootView.addView(ad,params);
            kwanko.request.setFormat(SupportedFormats.FORMAT_OVERLY);
            ad.load(kwanko.request);
        }

        private boolean isPositionTop(){
            return kwanko.request.getParamMap().get(TrackingParams.POSITION)!= null &&
                    kwanko.request.getParamMap()
                            .get(TrackingParams.POSITION).toString().equals(Position.TOP);
        }

        private int gravityFromParamPosition(ParamMap params){
            if(!params.containsKey(TrackingParams.POSITION)){
                return Gravity.CENTER;
            }
            return Position.gravityFromPositionValue(params.get(TrackingParams.POSITION).toString());
        }

        public void into(ViewBinder binder){
            NativeAdController controller = new NativeAdController(binder,kwanko.context);
            kwanko.lifecycle.forwardEventsTo(controller);
            controller.load(kwanko.request);
        }
    }

    private static boolean translucentStatusBar(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public final static class ActivityLifecycleObserver implements ActivityLifecycle {

        ActivityLifecycle forwardLifecycle;

        void forwardEventsTo(ActivityLifecycle activityLifecycle){
            this.forwardLifecycle = activityLifecycle;
        }

        @Override
        public void onResume() {
            if(forwardLifecycle!= null) {
                forwardLifecycle.onResume();
            }
        }

        @Override
        public void onPause() {
            if(forwardLifecycle != null) {
                forwardLifecycle.onPause();
            }
        }

        @Override
        public void onDestroy() {
            if(forwardLifecycle != null) {
                forwardLifecycle.onDestroy();
            }
        }
    }
}
