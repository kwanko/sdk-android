package fr.kwanko.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.mraid.js.OpenExecutor;

/**
 * SourceCode
 * Created by erusu on 3/31/2017.
 */

public class KwankoWebView extends WebView {

    public List<OnPageLoadedListener> listeners;
    private boolean openBrowserForLinks;
    private @AdModel.OpenStrategy int openStrategy = AdModel.BROWSER;

    public KwankoWebView(Context context) {
        super(context);
        init();
    }

    public KwankoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KwankoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public KwankoWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        listeners = new ArrayList<>();
        this.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                for(OnPageLoadedListener l:listeners){
                    l.onPageLoaded();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!openBrowserForLinks) {
                    return super.shouldOverrideUrlLoading(view, url);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(intent);
                    return true;
                }
            }

            @TargetApi(24)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(!openBrowserForLinks) {
                    return super.shouldOverrideUrlLoading(view, request);
                }else{
                    OpenExecutor executor =
                            OpenExecutor.forUrlAndStrategy(request.getUrl().toString(),openStrategy);
                    if(executor != null) {
                        executor.open(getContext(), request.getUrl().toString());
                    }else{
                        return super.shouldOverrideUrlLoading(view,request);
                    }
                    return true;
                }
            }
        });
    }

    public void addOnPageLoadedListener(OnPageLoadedListener listener){
        listeners.add(listener);
    }

    public interface OnPageLoadedListener{
        void onPageLoaded();
    }

    public void setOpenBrowserForLinks(boolean openBrowserForLinks) {
        this.openBrowserForLinks = openBrowserForLinks;
    }

    public void setOpenStrategy(int openStrategy) {
        this.openStrategy = openStrategy;
    }
}
