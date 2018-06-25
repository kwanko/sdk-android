package fr.kwanko.internal.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;

import fr.kwanko.InterstitialAdActivity;
import fr.kwanko.KwankoViewHolder;
import fr.kwanko.SupportedFeatures;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.model.CloseButtonMetadata;
import fr.kwanko.internal.mraid.js.KwankoWebAppInterface;
import fr.kwanko.bridge.WebViewBridge;
import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.internal.ActivityDelegate;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.model.ExpandProperties;
import fr.kwanko.model.KwankoAdContext;
import fr.kwanko.model.KwankoAdState;
import fr.kwanko.model.KwankoPlacementType;
import fr.kwanko.model.PositionProperties;
import fr.kwanko.model.SizeProperties;
import fr.kwanko.internal.model.AdModel;

import static android.view.View.VISIBLE;
import static fr.kwanko.common.KwankoDimensionUtils.getPixelsFromDp;

/**
 * SourceCode
 * Created by erusu on 3/29/2017.
 */

public class MraidContentController extends HtmlContentController {

    KwankoAdContext adContext;
    WebViewBridge webViewBridge;
    private KwankoPlacementType placementType;
    ViewGroup defaultParent;
    ViewGroup.LayoutParams defaultLayoutParams;
    private ActivityDelegate activityDelegate;

    public MraidContentController(BaseContainer container, CloseButtonMetadata metadata,
                                  KwankoPlacementType placementType) {
        super(container,metadata);
        this.placementType = placementType;
        this.adContext.setPlacementType(placementType);
        this.activityDelegate = new ActivityDelegate(container.getContext());
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onInitWebView(WebView webView) {
        super.onInitWebView(webView);

        webViewBridge = new WebViewBridge(webView);
        adContext = new KwankoAdContext(placementType, webViewBridge);
        webView.addJavascriptInterface(
                new KwankoWebAppInterface(getContext(), this, adContext, webViewBridge), "mobileNative");
    }

    @Override
    public void loadContentBasedOn(AdModel adModel) {
        adContext.setCurrentModel(adModel);
        super.loadContentBasedOn(adModel);
    }

    @Override
    protected void onWebViewContentLoaded(){
        super.onWebViewContentLoaded();
        setupSizeChangeListener();
        updateProperties();
        updateSupportedFeatures();
        initialiseExpandProperties();
        String slotId = adContext.getCurrentModel().getSlotId();
        float width = KwankoDimensionUtils.getDpFromPixels(getContext(), getWidth());
        float height = KwankoDimensionUtils.getDpFromPixels(getContext(), getHeight());
        String size = width + "x" + height;
        webViewBridge.loadAd(slotId, size);

        webViewBridge.sendGeoLocation();
        webViewBridge.setPlacementType(adContext.getPlacementType());
        webViewBridge.callReady();
        getAdContext().setState(KwankoAdState.DEFAULT);
    }

    private void updateSupportedFeatures() {
        webViewBridge.setSupportedFeatures(new SupportedFeatures(getContext()),getWebView());
    }

    private void initialiseExpandProperties() {
        ExpandProperties expandProperties = new ExpandProperties();
        expandProperties.setSizeProperties(getMaxSize());
        adContext.setExpandProperties(expandProperties);
    }

    public void resize(KwankoAdState newState, String customClosePosition, boolean allowOffscreen) {
        if (adContext.getPlacementType() == KwankoPlacementType.INTERSTITIAL) {
            webViewBridge.notifyError("resize", "Cannot resize an interstitial");
        } else if (adContext.getState() != KwankoAdState.DEFAULT) {
            webViewBridge.notifyError("resize", "Ad is not in default state");
        } else {
            initialiseDefaultParams();
            ViewGroup defaultRoot = activityDelegate.getRoot();
            defaultParent.removeView(container());
            PositionProperties resizeProperties = adContext.getResizeProperties();
            defaultRoot.addView(container(), getLayoutParamsFromResizeProperties(resizeProperties));
            webViewBridge.notifySizeChanged((int) resizeProperties.getWidth(),
                    (int) resizeProperties.getHeight());
            adContext.setState(newState);
            updateProperties();
            enableClosableRegion(customClosePosition);
        }

    }

    private void initialiseDefaultParams() {
        if (defaultParent == null) {
            defaultParent = ViewGroup.class.cast(container().getAdView());
        }
        if (defaultLayoutParams == null) {
            defaultLayoutParams = container().getLayoutParams();
        }
    }

    private ViewGroup.LayoutParams getLayoutParamsFromResizeProperties(PositionProperties resizeProperties) {
        ViewGroup.MarginLayoutParams resizeLayoutParams = new ViewGroup.MarginLayoutParams(
                (int) getPixelsFromDp(getContext(), resizeProperties.getWidth()),
                (int) getPixelsFromDp(getContext(), resizeProperties.getHeight()));
        resizeLayoutParams.leftMargin = (int) getPixelsFromDp(getContext(), resizeProperties.getX());
        resizeLayoutParams.topMargin = (int) getPixelsFromDp(getContext(), resizeProperties.getY());

        return resizeLayoutParams;
    }

    public void updateProperties() {
        PositionProperties currentPosition = getCurrentPosition();
        updateDefaultPosition(currentPosition);
        webViewBridge.setCurrentPosition(currentPosition);
        webViewBridge.setMaxSize(getMaxSize());
        webViewBridge.setScreenSize(getScreenSize());
        updateIsViewable();
        notifySizeChanged();
    }

    public PositionProperties getCurrentPosition() {
        int[] rootLocation = new int[2];
        activityDelegate.getRoot().getLocationOnScreen(rootLocation);

        int[] location = new int[2];
        container().getLocationOnScreen(location);
        return new PositionProperties(KwankoDimensionUtils.getDpFromPixels(getContext(), location[0] - rootLocation[0]),
                KwankoDimensionUtils.getDpFromPixels(getContext(), location[1] - rootLocation[1]),
                KwankoDimensionUtils.getDpFromPixels(getContext(), getWidth()),
                KwankoDimensionUtils.getDpFromPixels(getContext(), getHeight()));
    }

    private void updateDefaultPosition(PositionProperties positionProperties) {
        webViewBridge.setDefaultPosition(positionProperties);
    }

    public SizeProperties getMaxSize() {
        ViewGroup root = activityDelegate.getRoot();
        return new SizeProperties((int) KwankoDimensionUtils.getDpFromPixels(getContext(), root.getWidth()),
                (int) KwankoDimensionUtils.getDpFromPixels(getContext(), root.getHeight()));
    }

    public SizeProperties getScreenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        }
        return new SizeProperties((int) KwankoDimensionUtils.getDpFromPixels(getContext(), size.x),
                (int) KwankoDimensionUtils.getDpFromPixels(getContext(), size.y));
    }

    public void updateIsViewable() {
        webViewBridge.notifyViewableStateChanged(isViewable());
    }

    private boolean isViewable() {
        if (adContext.isActivityRunning() && container().getVisibility() == VISIBLE) {
            Rect rootBounds = new Rect();
            activityDelegate.getRoot().getHitRect(rootBounds);
            Rect viewBounds = new Rect();
            container().getHitRect(viewBounds);
            return Rect.intersects(rootBounds, viewBounds);
        } else {
            return false;
        }
    }

    public void notifySizeChanged() {
        webViewBridge.notifySizeChanged((int) KwankoDimensionUtils.getDpFromPixels(getContext(), getWidth()),
                (int) KwankoDimensionUtils.getDpFromPixels(getContext(), getHeight()));
    }

    private int getWidth(){
        return container().getWidth();
    }

    private int getHeight(){
        return container().getHeight();
    }

    public void expand() {
        if (adContext.getPlacementType() == KwankoPlacementType.INTERSTITIAL) {
            webViewBridge.notifyError("expand", "Cannot expand an interstitial");
        } else if (adContext.getState() != KwankoAdState.DEFAULT) {
            webViewBridge.notifyError("expand", "Ad is not in default state");
        } else {
            initialiseDefaultParams();

            KwankoViewHolder.setBaseWebViewContainer(container());
            getContext().startActivity(new Intent(getContext(), InterstitialAdActivity.class));

        }
    }

    @Override
    public void onCloseButtonClicked(View source) {
        OnCloseListener.CloseEvent event = getCloseEventForAdState(adContext.getState());
        collapse();
        if(event != null) {
            notifyCloseListener(this, event);
        }
    }

    public void collapse() {
        if (adContext.getState() == KwankoAdState.RESIZED
                || adContext.getState() == KwankoAdState.EXPANDED) {
            ViewGroup currentParent = (ViewGroup) container().getParent();
            currentParent.removeView(container());
            defaultParent.addView(container(), defaultLayoutParams);
            updateProperties();
            adContext.setState(KwankoAdState.DEFAULT);
            disableClosableRegion();
        } else {
            webViewBridge.notifyError("close", "Ad is not resized or expanded");
        }
    }

    protected OnCloseListener.CloseEvent getCloseEventForAdState(KwankoAdState state){
        switch (state){
            case RESIZED:
                return CloseEvents.RESIZE;
            case EXPANDED:
                return CloseEvents.EXPAND;
            case DEFAULT:
                return CloseEvents.GENERAL;
            default:
                return null;
        }
    }

    public KwankoAdContext getAdContext() {
        return adContext;
    }

    public void notifyExpandFinished() {
        updateProperties();
        adContext.setState(KwankoAdState.EXPANDED);
    }

    public void setupSizeChangeListener() {
        ViewTreeObserver vto = container().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateProperties();
            }
        });
    }
}
