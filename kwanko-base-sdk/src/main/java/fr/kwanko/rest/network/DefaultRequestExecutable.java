package fr.kwanko.rest.network;

import com.ads.kwanko.kwankoandroidsdk.base.BuildConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.kwanko.AdRequest;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.Url;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.params.ParamValue;
import fr.kwanko.params.TrackingParams;
import fr.kwanko.rest.network.http.HttpStack;

import static fr.kwanko.common.KwankoBitmapUtils.*;

/**
 * SourceCode
 * Created by erusu on 3/10/2017.
 */

class DefaultRequestExecutable extends RequestExecutable {

    private static final String method = DEFAULT_METHOD;
    private HttpStack<AdModel> httpStack;
    /*For security reasons the url will be a private static final property and it will not be
    http://192.168.46.46:80/trk.php
    http://trk.preprod.netav3.c2bdev.net/

    * organized in any public constant class util*/
    @Url
    private static final String URL = "https://action.metaffiliation.com/pool.php";

    DefaultRequestExecutable(HttpStack<AdModel> httpStack) {
        this.httpStack = httpStack;
    }

    @Override
    protected void performRequest(AdRequest request) {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Kwanko-Content-Type", "application/json");
        headers.put("x-kwanko-sdk-version", "android-" + BuildConfig.VERSION_NAME);
        ParamsAdapter adapter = new ParamsAdapter(request.getParamMap());
        String requestJsonString = adapter.getParamsAsStringWithRequestDetails(request.getSlotId(),
                request.getFormat());
        KwankoLog.logRequest(headers,requestJsonString);
        final AdModel adModel = httpStack.send(URL, headers, requestJsonString, method);
        if (adModel == null) {
            return;//handle fail
        }
        if(getExtraResourcesFile() != null) {
            setCustomCloseBitmapIfAvailable(
                    adModel,getExtraResourcesFile(),
                    adModel.getCloseButtonMetadata().getImageSrc()
                    );
        }
        adModel.setFormat(request.getFormat());
        adModel.setSlotId(request.getSlotId());
        ParamValue paramValue = request.getParamMap().get(TrackingParams.POSITION);
        adModel.setPosition(paramValue!=null?paramValue.toString():null);
        deliverResult(adModel);
    }

    private void setCustomCloseBitmapIfAvailable(AdModel adModel,File file, String url) {
        File bitmapFile = saveBitmapToFile(url, file);
        adModel.getCloseButtonMetadata().setCloseButtonFile(bitmapFile);
    }
}
