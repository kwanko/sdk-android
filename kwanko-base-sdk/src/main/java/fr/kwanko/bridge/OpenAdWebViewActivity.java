package fr.kwanko.bridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.common.Preconditions;


/**
 * SourceCode
 * Created by erusu on 2/15/2017.
 */

public class OpenAdWebViewActivity extends Activity {

    private WebView webView;
    private Toolbar toolbar;

    public static void start(Context context, String url){
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(url);
        Intent intent = new Intent(context,OpenAdWebViewActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kwanko_webview_activity);
        webView = WebView.class.cast(findViewById(R.id.webView));
        toolbar = Toolbar.class.cast(findViewById(R.id.toolbar));

        setUpWebView();
        loadUrl();
        setListeners();
    }

    private void setUpWebView(){
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){});
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void loadUrl(){
        String url = getIntent().getStringExtra("url");
        if(url == null){
            throw new IllegalArgumentException("url is null");
        }
        webView.loadUrl(url);
    }

    private void setListeners(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }
}
