package fr.kwanko.rest.network;

import com.ads.kwanko.kwankoandroidsdk.base.BuildConfig;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import fr.kwanko.AdRequest;
import fr.kwanko.common.Josr;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.Url;
import fr.kwanko.internal.model.NativeAdModel;
import fr.kwanko.rest.network.http.HttpSerialiser;
import fr.kwanko.rest.network.http.HttpStack;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class NativeAdsRequestExecutable extends RequestExecutable {

    private HttpStack<NativeAdModel> httpStack;
    /*For security reasons the url will be a private static final property and it will not be
    http://192.168.46.46:80/trk.php
    http://trk.preprod.netav3.c2bdev.net/

    * organized in any public constant class util*/
    @Url
    private static final String URL = "http://trk.preprod.netav3.c2bdev.net/trk.php";

    private static final HttpSerialiser<NativeAdModel> serialiser = new HttpSerialiser<NativeAdModel>() {
        @Override
        public NativeAdModel parse(HttpURLConnection connection) throws IOException, JSONException {
            Josr josr = new Josr();
            return josr.fromJson(readData(connection), NativeAdModel.class);
        }
    };

    public NativeAdsRequestExecutable(){
        httpStack = new HttpStack<>(serialiser);
    }

    @Override
    protected void performRequest(AdRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Kwanko-Content-Type", "application/json");
        //headers.put("Host", "trk.feat_sdkjs_t24612.branches.netav3.c2bdev.net");
        headers.put("x-kwanko-sdk-version", "android-" + BuildConfig.VERSION_NAME);
        ParamsAdapter adapter = new ParamsAdapter(request.getParamMap());
        String requestJsonString = adapter.getParamsAsStringWithRequestDetails(request.getSlotId(),
                request.getFormat());

        KwankoLog.logRequest(headers,request.toString());
        final NativeAdModel adModel = httpStack.send(URL, headers, requestJsonString, DEFAULT_METHOD);
        if(adModel == null){
            return;
        }
        deliverResult(adModel);
    }

}
