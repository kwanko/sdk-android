package fr.kwanko.bridge;

import android.view.View;
import android.webkit.WebView;

import fr.kwanko.SupportedFeatures;
import fr.kwanko.model.KwankoAdState;
import fr.kwanko.model.KwankoPlacementType;
import fr.kwanko.model.PositionProperties;
import fr.kwanko.model.SizeProperties;

/**
 * Created by vfatu on 03.01.2017.
 */

public class WebViewBridge {

    private static final String COMMA = "' , '";
    private WebView webView;

    public WebViewBridge(WebView webView) {
        this.webView = webView;
    }

    public void loadAd(String slotId, String size) {
        webView.loadUrl("javascript:loadAd('" + slotId + COMMA + size + "')");
    }

    public void sendGeoLocation() {
        webView.loadUrl("javascript:sendGeolocation('test location bla bla')");
    }

    public void setCurrentPosition(PositionProperties positionProperties) {
        webView.loadUrl("javascript:window.mraidbridge.setCurrentPosition('" +
                positionProperties.getX() + COMMA + positionProperties.getY() + COMMA +
                positionProperties.getWidth() + COMMA + positionProperties.getHeight() + "')");
    }

    public void setDefaultPosition(PositionProperties positionProperties) {
        webView.loadUrl("javascript:window.mraidbridge.setDefaultPosition('" +
                positionProperties.getX() + COMMA + positionProperties.getY() + COMMA +
                positionProperties.getWidth() + COMMA + positionProperties.getHeight() + "')");
    }

    public void setPlacementType(KwankoPlacementType type){
        webView.loadUrl("javascript:window.mraidbridge.setPlacementType('"+type.toString()+"'");
    }

    public void setMaxSize(SizeProperties sizeProperties) {
        webView.loadUrl("javascript:window.mraidbridge.setMaxSize('" + sizeProperties.getWidth() +
                COMMA + sizeProperties.getHeight() + "')");
    }

    public void setScreenSize(SizeProperties sizeProperties) {
        webView.loadUrl("javascript:window.mraidbridge.setScreenSize('" + sizeProperties.getWidth()
                + COMMA + sizeProperties.getHeight() + "')");
    }

    public void notifyViewableStateChanged(boolean isViewable) {
        webView.loadUrl("javascript:window.mraidbridge.setIsViewable('" + isViewable + "')");
    }

    public void notifyStateChanged(KwankoAdState state) {
        webView.loadUrl("javascript:window.mraidbridge.setState('" + state.toString() + "')");
    }

    public void notifySizeChanged(int width, int height) {
        webView.loadUrl("javascript:window.mraidbridge.notifySizeChangeEvent('" + width + COMMA + height + "')");
    }

    public void notifyError(String method, String errorMessage) {
        webView.loadUrl("javascript:window.mraidbridge.notifyErrorEvent('" + method + COMMA + errorMessage + "')");
    }

    public void notifyCallFinished(String methodName) {
        webView.loadUrl("javascript:window.mraidbridge.nativeCallComplete('" + methodName + "')");
    }

    public void setSupportedFeatures(SupportedFeatures supportedFeatures, View view) {
        if(view == null){
            throw new NullPointerException("view is null");
        }
        webView.loadUrl("javascript:window.mraidbridge.setSupports('" +
                supportedFeatures.isSmsAvailable() + COMMA +
                supportedFeatures.isTelAvailable() + COMMA +
                supportedFeatures.isCalendarAvailable() + COMMA +
                supportedFeatures.isStorePictureAvailable() + COMMA +
                supportedFeatures.isInlineVideoAvailable(view) + "')");
    }

    public void callReady() {
        webView.loadUrl("javascript:window.mraidbridge.notifyReadyEvent()");
    }

}
