package fr.kwanko.internal.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.kwanko.ActivityLifecycle;
import fr.kwanko.OrientationChangeListener;
import fr.kwanko.common.KwankoBitmapUtils;
import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.common.KwankoDownloadUtils;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.model.CloseButtonMetadata;

import static fr.kwanko.common.KwankoBitmapUtils.*;

/**
 * SourceCode
 * Created by erusu on 3/29/2017.
 */

public abstract class ContentController  implements ActivityLifecycle {

    private static final int CLOSEABLE_REGION_SIZE = 50;
    private BaseContainer container;
    private FrameLayout closableRegion;
    private ImageView closeIndicator;
    private Map<OnCloseListener.CloseEvent,List<OnCloseListener>> closeListenerSubscribers;
    private OrientationChangeListener orientationChangeListener;
    private CloseButtonMetadata closeButtonMetadata;

    public ContentController(BaseContainer container, CloseButtonMetadata closeButtonMetadata) {
        this.container = container;
        this.closeButtonMetadata = closeButtonMetadata;
        closeListenerSubscribers = new ArrayMap<>();
        if(closeButtonMetadata != null){
            initialiseClosableRegion(closeButtonMetadata);
        }
    }

    private void initialiseClosableRegion(CloseButtonMetadata closeButtonMetadata) {
        closableRegion = new FrameLayout(getContext());
        int padding = KwankoDimensionUtils.scaleToPx(getContext(),
                closeButtonMetadata.getPaddingDp());
        int width = KwankoDimensionUtils.scaleToPx(getContext(),
                CLOSEABLE_REGION_SIZE);
        int height = KwankoDimensionUtils.scaleToPx(getContext(),
                CLOSEABLE_REGION_SIZE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                width , height);
        container().addView(closableRegion, params);
        closeIndicator = new ImageView(getContext());
        int indicatorWidth = KwankoDimensionUtils.scaleToPx(getContext(),
                closeButtonMetadata.getWidthDp());
        int indicatorHeight = KwankoDimensionUtils.scaleToPx(getContext(),
                closeButtonMetadata.getHeightDp());
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                indicatorWidth,
                indicatorHeight);
        imageParams.gravity = Gravity.RIGHT|Gravity.TOP;
        imageParams.topMargin = padding;
        imageParams.rightMargin = padding;
        imageParams.leftMargin = padding;
        imageParams.bottomMargin = padding;
        closableRegion.addView(closeIndicator,imageParams);
        addCloseListener();
        setClosableRegionVisibility(false);
    }

    private void addCloseListener() {
        closableRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseButtonClicked(view);
            }
        });
    }

    public abstract void onCloseButtonClicked(View source);

    private void setClosableRegionVisibility(boolean closable) {
        closableRegion.setClickable(closable);
        closableRegion.setFocusable(closable);
        closableRegion.setVisibility(closable?View.VISIBLE:View.INVISIBLE);
    }

    protected void disableClosableRegion() {
        setClosableRegionVisibility(false);
    }

    protected void enableClosableRegion(String customClosePosition) {
        updateClosableRegionPosition(customClosePosition);
        setClosableRegionVisibility(true);
    }

    private void updateClosableRegionPosition(String customClosePosition) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) closableRegion.getLayoutParams();
        params.gravity = Gravity.NO_GRAVITY;

        if (customClosePosition.contains("center")) {
            params.gravity = params.gravity | Gravity.CENTER;
        }
        if (customClosePosition.contains("top")) {
            params.gravity = params.gravity | Gravity.TOP;
        }
        if (customClosePosition.contains("bottom")) {
            params.gravity = params.gravity | Gravity.BOTTOM;
        }
        if (customClosePosition.contains("right")) {
            params.gravity = params.gravity | Gravity.RIGHT;
        }
        if (customClosePosition.contains("left")) {
            params.gravity = params.gravity | Gravity.LEFT;
        }

        closableRegion.setLayoutParams(params);
        ((FrameLayout.LayoutParams)closeIndicator.getLayoutParams()).gravity = params.gravity;
    }

    /**
     * Called from Interstitial activity so the button is visible when ad loads
     */
    public void showClosableRegion(boolean useCustomClose){
        enableClosableRegion("top-right");
        if(!useCustomClose){
            boolean set = setBackgroundForClosableRegion(closableRegion,
                    closeButtonMetadata.getCloseButtonFile());
            if(!set){
                setImageResourceForClosableRegion(closableRegion,
                        R.drawable.kwanko_ic_default_close);
            }
        }
    }

    void clearCloseButtonBackground(){
        setImageBitmapForClosableRegion(closableRegion.getChildAt(0),null);
    }

    public abstract void loadContentBasedOn(AdModel adModel);

    protected BaseContainer container(){
        return container;
    }

    protected Context getContext(){
        return container.getContext();
    }

    public void notifyCloseListener(Object source,OnCloseListener.CloseEvent event) {
        if(closeListenerSubscribers.containsKey(event)){
            List<OnCloseListener> listeners =
                    closeListenerSubscribers.get(event);
            for(OnCloseListener listener:listeners){
                listener.onCloseEvent(source,event);
            }
        }
    }

    public void notifyOrientationChangeListener() {
        if (orientationChangeListener != null) {
            orientationChangeListener.onOrientationChange();
        }
    }

    public void subscribeForCloseEvent(OnCloseListener.CloseEvent event,
                                       OnCloseListener listener){
        if(closeListenerSubscribers.containsKey(event)){
            List<OnCloseListener> list = closeListenerSubscribers.get(event);
            list.add(listener);
            closeListenerSubscribers.put(event,list);
        }else {
            List<OnCloseListener> list = new ArrayList<>();
            list.add(listener);
            closeListenerSubscribers.put(event, list);
        }
    }

    public void unSubscribeForCloseEvent(OnCloseListener.CloseEvent event, OnCloseListener listener){
        if(!closeListenerSubscribers.containsKey(event)){
            return;
        }
        List<OnCloseListener> list = closeListenerSubscribers.get(event);
        list.remove(listener);
        closeListenerSubscribers.put(event,list);
    }

    public OrientationChangeListener getOrientationChangeListener() {
        return orientationChangeListener;
    }

    public void setOrientationChangeListener(OrientationChangeListener orientationChangeListener) {
        this.orientationChangeListener = orientationChangeListener;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        closeListenerSubscribers.clear();
    }
}
