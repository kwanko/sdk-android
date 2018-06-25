package fr.kwanko.internal.mraid.js;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

import fr.kwanko.bridge.OpenAdWebViewActivity;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 2/15/2017.
 */

public abstract class OpenExecutor {

    private static final OpenExecutor WebViewExecutor = new OpenExecutor() {
        @Override
        public void open(Context context,String url) {
            OpenAdWebViewActivity.start(context,url);
        }
    };

    private static final OpenExecutor BrowserExecutor = new OpenExecutor() {
        @Override
        public void open(Context context,String url) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    };

    private static final OpenExecutor SmsOpenExecutor = new OpenExecutor() {
        @Override
        public void open(Context context, String url) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.fromParts("sms", OpenExecutor.parseNrFromUrl(url), null)));
        }
    };

    private static String parseNrFromUrl(String url){
        return url.contains("/")?url.substring(url.lastIndexOf('/')):"";
    }

    private static final OpenExecutor CallTelOpenExecutor = new OpenExecutor() {
        @Override
        public void open(Context context, String url) {
            context.startActivity(new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" +  OpenExecutor.parseNrFromUrl(url))));
        }
    };


    public abstract void open(Context context, String url);

    public static OpenExecutor forUrlAndStrategy(String url, @AdModel.OpenStrategy int openStrategy){
        if(URLUtil.isValidUrl(url)){
            switch (openStrategy){
                case  AdModel.BROWSER:
                    return BrowserExecutor;
                case AdModel.WEBVIEW:
                    return WebViewExecutor;
                default:
                    throw new IllegalArgumentException("unimplemented executor for strategy");
            }
        }else if(isSmsUrl(url)){
            return SmsOpenExecutor;
        } else if(isTelUrl(url)){
            return CallTelOpenExecutor;
        }
        throw new IllegalArgumentException("unimplemented executor for strategy");
    }

    private static boolean isSmsUrl(String url){
        return url!=null && url.startsWith("sms://");
    }

    private static boolean isTelUrl(String url){
        return url!=null && url.startsWith("tel://");
    }
}
