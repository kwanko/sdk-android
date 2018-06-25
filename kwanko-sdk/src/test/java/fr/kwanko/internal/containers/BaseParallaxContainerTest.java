package fr.kwanko.internal.containers;

import android.content.Context;
import android.graphics.Rect;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.kwanko.KwankoAd;
import fr.kwanko.internal.KwankoWebView;

import static org.mockito.Mockito.*;

/**
 * SourceCode
 * Created by erusu on 5/2/2017.
 */

@RunWith(PowerMockRunner.class)
public class BaseParallaxContainerTest {

    @Mock
    KwankoWebView webView;
    @Mock
    WebSettings webSettings;
    @Mock
    Context context;
    @Mock
    BaseParallaxContainer.ParallaxParams params;
    @Mock
    KwankoAd parent;
    BaseParallaxContainer container;
    BaseParallaxContainer spyContainer;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        container = new BaseParallaxContainer(context,params) {
            @Override
            public Rect getParentVisibleRect() {
                return null;
            }
        };
        spyContainer = spy(container);
    }

    @Test
    public void testOnAddContentViewWithWebViewArg_webViewIsConfigured(){
        when(webView.getSettings()).thenReturn(webSettings);
        when(spyContainer.getParent()).thenReturn(parent);
        FrameLayout.LayoutParams mockParams = mock(FrameLayout.LayoutParams.class);
        when(parent.getLayoutParams()).thenReturn(mockParams);
        spyContainer.addContentView(webView);
        verify(webView).setHorizontalScrollBarEnabled(false);
        verify(webView).setVerticalScrollBarEnabled(false);
        verify(webSettings).setLoadWithOverviewMode(true);
        verify(webSettings).setUseWideViewPort(true);
    }

    @Test
    public void testOnAddContentViewWithWebViewArg_heightIsRetrievedFromAdViewLayoutParams(){

    }

}
