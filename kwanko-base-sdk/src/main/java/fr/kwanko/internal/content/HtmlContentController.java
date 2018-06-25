package fr.kwanko.internal.content;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import fr.kwanko.internal.KwankoWebView;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.model.CloseButtonMetadata;
import fr.kwanko.params.TrackingParamsUtils;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/29/2017.
 */

public class HtmlContentController extends ContentController {

    private KwankoWebView webView;

    public HtmlContentController(BaseContainer container, CloseButtonMetadata metadata) {
        super(container,metadata);
        initWebView();
        initWebClient();
        initWebViewProperties();
        container.addContentView(webView);
    }

    private void initWebView(){
        webView = new KwankoWebView(getContext());
        onInitWebView(webView);
    }

    protected void onInitWebView(WebView webView){
        //no implementation
    }

    private void initWebClient() {
        webView.addOnPageLoadedListener(new KwankoWebView.OnPageLoadedListener() {
            @Override
            public void onPageLoaded() {
                onWebViewContentLoaded();
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    protected void onWebViewContentLoaded(){
        container().show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewProperties() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setOpenBrowserForLinks(true);
    }

    @Override
    public void loadContentBasedOn(AdModel adModel) {
        if(adModel.isResponsiveHtml()){
            int width,height;
            if(adModel.getAdHeight() == 0 || adModel.getAdWidth() == 0){
                View adView = container().getAdView();
                width = adView.getWidth();
                height = adView.getHeight();
                if(width == 0 || height == 0){
                    width = TrackingParamsUtils.getScreenWidth();
                    height = TrackingParamsUtils.getScreenHeight();
                }
            }else{
                width = adModel.getAdWidth();
                height = adModel.getAdHeight();
            }
            webView.getLayoutParams().width = width;
            webView.getLayoutParams().height = height;
        }
        if(adModel.isUrl()){
            webView.loadUrl(adModel.getHtml());
        }else {
            webView.loadDataWithBaseURL("blarg://ignored", adModel.getHtml(), "text/html", "utf-8", "");
        }
        webView.setOpenStrategy(adModel.getOpenStrategy());
    }

    public KwankoWebView getWebView() {
        return webView;
    }

    @Override
    public void onCloseButtonClicked(View view) {
        notifyCloseListener(this, CloseEvents.GENERAL);
    }


}
