package fr.kwanko.bridge;

import android.view.View;
import android.webkit.WebView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.powermock.core.classloader.annotations.PowerMockListener;

import fr.kwanko.SupportedFeatures;
import fr.kwanko.model.KwankoAdState;
import fr.kwanko.model.KwankoPlacementType;
import fr.kwanko.model.PositionProperties;
import fr.kwanko.model.SizeProperties;

import static org.mockito.Mockito.*;

/**
 * SourceCode
 * Created by erusu on 4/27/2017.
 */
@RunWith(JUnit4.class)
public class WebViewBridgeTest {

    private static final String SLOT_ID = "slotId";
    private static final String SIZE = "12X12";
    private static final String COMMA = "' , '";

    @Mock
    WebView webView;
    @Mock
    View view;
    @Mock
    SupportedFeatures features;
    WebViewBridge bridge;
    private PositionProperties pos = new PositionProperties(0,1,100,200);
    private SizeProperties sizeProperties = new SizeProperties(100,200);
    private String method = "expand";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        bridge = new WebViewBridge(webView);
    }

    @Test
    public void testLoadAd_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.loadAd(SLOT_ID,SIZE);
        verify(webView).loadUrl("javascript:loadAd('"+ SLOT_ID+ COMMA + SIZE + "')" );
    }

    @Test
    public void testSendGeoLocation_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.sendGeoLocation();
        verify(webView).loadUrl("javascript:sendGeolocation('test location bla bla')");
    }

    @Test(expected = NullPointerException.class)
    public void testSetCurrentPosition_throwsNullPointerExceptionWithNullArgs(){
        bridge.setCurrentPosition(null);
    }

    @Test
    public void testSetCurrentPosition_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setCurrentPosition(pos);
        verify(webView).loadUrl("javascript:window.mraidbridge.setCurrentPosition('" +
        pos.getX() + COMMA + pos.getY() + COMMA + pos.getWidth() + COMMA +
        pos.getHeight() + "')");
    }

    @Test(expected = NullPointerException.class)
    public void testSetDefaultPosition_throwsNullPointerExceptionWithNullArgs(){
        bridge.setDefaultPosition(null);
    }

    @Test
    public void testSetDefaultPosition_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setDefaultPosition(pos);
        verify(webView).loadUrl("javascript:window.mraidbridge.setDefaultPosition('"+
        pos.getX() + COMMA + pos.getY() + COMMA + pos.getWidth() +
        COMMA + pos.getHeight() + "')");
    }

    @Test(expected = NullPointerException.class)
    public void testSetPlacementType_throwsNullPointerExceptionForNullArgs(){
        bridge.setPlacementType(null);
    }

    @Test
    public void testSetPlacementTypeInline_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setPlacementType(KwankoPlacementType.INLINE);
        verify(webView).loadUrl("javascript:window.mraidbridge.setPlacementType('inline'");
    }

    @Test
    public void testSetPlacementTypeInterstitial_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setPlacementType(KwankoPlacementType.INTERSTITIAL);
        verify(webView).loadUrl("javascript:window.mraidbridge.setPlacementType('interstitial'");
    }

    @Test(expected = NullPointerException.class)
    public void testSetMaxSize_throwsNullPointerExceptionForNullArgs(){
        bridge.setMaxSize(null);
    }

    @Test
    public void testSetMaxSize_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setMaxSize(sizeProperties);
        verify(webView).loadUrl("javascript:window.mraidbridge.setMaxSize('" + sizeProperties.getWidth() +
                COMMA + sizeProperties.getHeight() + "')");
    }

    @Test(expected = NullPointerException.class)
    public void testSetScreenSize_throwsNullPointerExceptionForNullArgs(){
        bridge.setScreenSize(null);
    }

    @Test
    public void testSetScreenSize_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.setScreenSize(sizeProperties);
        verify(webView).loadUrl("javascript:window.mraidbridge.setScreenSize('" + sizeProperties.getWidth()
                + COMMA + sizeProperties.getHeight() + "')");
    }

    @Test
    public void testNotifyViewableStateChangedWithTrueArg_callsJavascriptCodeWithTrue(){
        bridge.notifyViewableStateChanged(true);
        verify(webView).loadUrl("javascript:window.mraidbridge.setIsViewable('true')");
    }

    @Test
    public void testNotifyViewableStateChangedWithFalseArg_callsJavascriptCodeWithFalse(){
        bridge.notifyViewableStateChanged(false);
        verify(webView).loadUrl("javascript:window.mraidbridge.setIsViewable('false')");
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyStateChanged_throwsNullPointerExceptionForNullArgs(){
        bridge.notifyStateChanged(null);
    }

    @Test
    public void testNotifyStateChanged_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        for(KwankoAdState state : KwankoAdState.values()){
            bridge.notifyStateChanged(state);
            verify(webView).loadUrl("javascript:window.mraidbridge.setState('" + state.toString() + "')");
        }
    }

    @Test
    public void testNotifySizeChanged_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.notifySizeChanged(100,200);
        verify(webView).loadUrl("javascript:window.mraidbridge.notifySizeChangeEvent('100"  +
                COMMA + "200')");
    }

    @Test
    public void testNotifyError_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        String errorMessage = "failed!!";
        bridge.notifyError(method,errorMessage);
        verify(webView).loadUrl("javascript:window.mraidbridge.notifyErrorEvent('" + method +
                COMMA + errorMessage + "')");
    }

    @Test
    public void testNotifyCallFinished_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.notifyCallFinished(method);
        verify(webView).loadUrl("javascript:window.mraidbridge.nativeCallComplete('" + method +
                "')");
    }

    @Test(expected = NullPointerException.class)
    public void testSupportedFeaturesWithFirstArgNull_throwsNullPointerException(){
        bridge.setSupportedFeatures(null,view);
    }

    @Test(expected = NullPointerException.class)
    public void testSupportedFeaturesWithSecondArgNull_throwsNullPointerException(){
        bridge.setSupportedFeatures(features,null);
    }

    @Test
    public void testSetSupportedFeatures_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        when(features.isSmsAvailable()).thenReturn(true);
        when(features.isTelAvailable()).thenReturn(true);
        when(features.isCalendarAvailable()).thenReturn(false);
        when(features.isStorePictureAvailable()).thenReturn(false);
        when(features.isInlineVideoAvailable(isA(View.class))).thenReturn(false);
        bridge.setSupportedFeatures(features,view);
        verify(webView).loadUrl("javascript:window.mraidbridge.setSupports('" +
                "true" + COMMA +
               "true" + COMMA +
                "false" + COMMA +
                "false" + COMMA +
                "false" + "')");
    }

    @Test
    public void testCallReady_webViewLoadUrlWithJavaScriptCodeIsCalled(){
        bridge.callReady();
        verify(webView).loadUrl("javascript:window.mraidbridge.notifyReadyEvent()");
    }

}
