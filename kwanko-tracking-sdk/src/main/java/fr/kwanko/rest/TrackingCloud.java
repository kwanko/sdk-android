package fr.kwanko.rest;


import com.ads.kwankoandroidsdk.tracking.base.BuildConfig;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.params.KwankoConversion;
import fr.kwanko.rest.network.ParamsAdapter;
import fr.kwanko.rest.network.http.HttpSerialiser;
import fr.kwanko.rest.network.http.HttpStack;

/**
 * SourceCode
 * Created by erusu on 3/17/2017.
 */

public class TrackingCloud {

    /**
     * https://action.metaffiliation.com/trk.php
     * http://trk.preprod.netav3.c2bdev.net/
     */
    private static final String URL = "https://action.metaffiliation.com/trk.php";
    private static final String METHOD = "POST";

    public static void sendConversion(KwankoConversion conversion){
        Map<String,String> headers = new HashMap<>();
        headers.put("X-Kwanko-Content-Type","application/json");
        headers.put("x-kwanko-sdk-version","android-"+ BuildConfig.VERSION_NAME);
        ParamsAdapter adapter = new ParamsAdapter(conversion);
        List<Pair> params = adapter.getPairListFromTrackingParams();
        Request request = new Request();
        request.headers = headers;
        request.method = METHOD;
        request.url = URL;
        request.params = params;
        executeRequest(request);
    }

    private static void executeRequest(Request r){
        Executors.newCachedThreadPool().execute(new RequestRunnable(r));
    }

    private static class RequestRunnable implements Runnable{

        private final Request request;
        private HttpStack<String> httpStack;

        private RequestRunnable(Request r){
            this.request = r;
            httpStack = new HttpStack<>(new HttpSerialiser<String>() {
                @Override
                public String parse(HttpURLConnection connection) throws IOException, JSONException {
                    String data = readData(connection);
                    KwankoLog.logResponse(data);
                    return data;
                }
            });
        }

        @Override
        public void run() {
            httpStack.sendNoResponse(request.url,request.headers,request.params,request.method);
        }
    }

    private static class Request{

        private Map<String,String> headers;
        private List<Pair> params;
        private String url;
        private String method;


    }
}
