package fr.kwanko.bridge;



import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ads.kwanko.kwankoandroidsdk.base.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static fr.kwanko.bridge.OpenAdWebViewActivityStartTest.*;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * SourceCode
 * Created by erusu on 4/26/2017.
 */

@RunWith(PowerMockRunner.class)
@Config(constants = BuildConfig.class)
public class OpenAdWebViewActivityTest {



    @Mock
    WebView webView;
    @Mock
    WebSettings webSettings;
    @Mock
    Intent intent;
    @Mock
    Toolbar toolbar;
    private OpenAdWebViewActivity spy;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        OpenAdWebViewActivity activity = new OpenAdWebViewActivity();
        spy = spy(activity);
        when(spy.findViewById(com.ads.kwanko.kwankoandroidsdk.base.R.id.webView))
                .thenReturn(webView);
        when(spy.findViewById(com.ads.kwanko.kwankoandroidsdk.base.R.id.toolbar))
                .thenReturn(toolbar);
        when(webView.getSettings()).thenReturn(webSettings);
        when(spy.getIntent()).thenReturn(intent);
        when(intent.getStringExtra("url")).thenReturn(MOCK_URL);
    }


    @Test
    public void testOnCreate_setUpsTheWebViewWithDefinedSettings(){
        spy.onCreate(null);
        verify(webView).setWebViewClient(isA(WebViewClient.class));
        verify(webSettings).setJavaScriptEnabled(false);
        verify(webSettings).setDomStorageEnabled(true);
        verify(webSettings).setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnCreate_throwsExceptionForNoUrl(){
        when(intent.getStringExtra("url")).thenReturn(null);
        spy.onCreate(null);
    }

    @Test
    public void testOnCreate_loadsUrlIntoWebView(){
        spy.onCreate(null);
        verify(webView).loadUrl(MOCK_URL);
    }

    @Test
    public void testOnCreate_setsListenersToToolbar(){
        spy.onCreate(null);
        verify(toolbar).setNavigationOnClickListener(isA(View.OnClickListener.class));
    }

    @Test
    public void testOnBackPressed_canGoBackWebViewGoBackIsCalled(){
        when(webView.canGoBack()).thenReturn(true);
        spy.onCreate(null);
        spy.onBackPressed();
        verify(webView, VerificationModeFactory.times(1)).goBack();
    }

    @Test
    public void testOnBackPressed_canNotBackWebViewGoBackIsNotCalled(){
        when(webView.canGoBack()).thenReturn(false);
        spy.onCreate(null);
        spy.onBackPressed();
        verify(webView, VerificationModeFactory.times(0)).goBack();
    }
}
