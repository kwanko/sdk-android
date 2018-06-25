package fr.kwanko.model;

import android.view.ViewGroup;

import fr.kwanko.bridge.WebViewBridge;
import fr.kwanko.internal.model.AdModel;

/**
 * Created by vfatu on 13.01.2017.
 */

public class KwankoAdContext {

    private WebViewBridge webViewBridge;
    private PositionProperties defaultPosition;
    private ViewGroup defaultParent;
    private ViewGroup.LayoutParams defaultLayoutParams;
    private KwankoPlacementType placementType;
    private KwankoAdState state;
    private ExpandProperties expandProperties;
    private OrientationProperties orientationProperties;
    private PositionProperties resizeProperties;
    private boolean isActivityRunning;
    private AdModel currentModel;

    public KwankoAdContext(KwankoPlacementType placementType, WebViewBridge webViewBridge) {
        this.webViewBridge = webViewBridge;
        this.placementType = placementType;
        this.state = KwankoAdState.LOADING;
        this.orientationProperties = new OrientationProperties();
        this.orientationProperties.setAllowOrientationChange(true);
        this.orientationProperties.setForceOrientation("none");
        this.isActivityRunning = true;
    }

    public PositionProperties getDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(PositionProperties defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    public ViewGroup getDefaultParent() {
        return defaultParent;
    }

    public void setDefaultParent(ViewGroup defaultParent) {
        this.defaultParent = defaultParent;
    }

    public ViewGroup.LayoutParams getDefaultLayoutParams() {
        return defaultLayoutParams;
    }

    public void setDefaultLayoutParams(ViewGroup.LayoutParams defaultLayoutParams) {
        this.defaultLayoutParams = defaultLayoutParams;
    }

    public KwankoPlacementType getPlacementType() {
        return placementType;
    }

    public void setPlacementType(KwankoPlacementType placementType) {
        this.placementType = placementType;
    }

    public KwankoAdState getState() {
        return state;
    }

    public void setState(KwankoAdState state) {
        this.state = state;
        System.out.println("android state change to : " + state.toString());
        this.webViewBridge.notifyStateChanged(state);
    }

    public ExpandProperties getExpandProperties() {
        return expandProperties;
    }

    public void setExpandProperties(ExpandProperties expandProperties) {
        this.expandProperties = expandProperties;
    }

    public OrientationProperties getOrientationProperties() {
        return orientationProperties;
    }

    public void setOrientationProperties(OrientationProperties orientationProperties) {
        this.orientationProperties = orientationProperties;
    }

    public PositionProperties getResizeProperties() {
        return resizeProperties;
    }

    public void setResizeProperties(PositionProperties resizeProperties) {
        this.resizeProperties = resizeProperties;
    }

    public boolean isActivityRunning() {
        return isActivityRunning;
    }

    public void setActivityRunning(boolean activityRunning) {
        isActivityRunning = activityRunning;
    }

    public AdModel getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(AdModel currentModel) {
        this.currentModel = currentModel;
    }
}
