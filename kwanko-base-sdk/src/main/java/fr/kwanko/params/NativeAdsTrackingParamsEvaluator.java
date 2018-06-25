package fr.kwanko.params;

import static fr.kwanko.params.TrackingParams.CARRIERS;
import static fr.kwanko.params.TrackingParams.CELL_TOWERS;
import static fr.kwanko.params.TrackingParams.CONNECTIVITY;
import static fr.kwanko.params.TrackingParams.DEVICE_TYPE;
import static fr.kwanko.params.TrackingParams.DOMAIN;
import static fr.kwanko.params.TrackingParams.HOME_MOBILE_COUNTRY_CODE;
import static fr.kwanko.params.TrackingParams.HOME_MOBILE_NETWORK_CODE;
import static fr.kwanko.params.TrackingParams.LANGUAGE;
import static fr.kwanko.params.TrackingParams.LAT;
import static fr.kwanko.params.TrackingParams.LONG;
import static fr.kwanko.params.TrackingParams.MAKE;
import static fr.kwanko.params.TrackingParams.MODEL;
import static fr.kwanko.params.TrackingParams.OS;
import static fr.kwanko.params.TrackingParams.OSV;
import static fr.kwanko.params.TrackingParams.RADIO_TYPE;
import static fr.kwanko.params.TrackingParams.SCREEN_HEIGHT;
import static fr.kwanko.params.TrackingParams.SCREEN_WIDTH;
import static fr.kwanko.params.TrackingParams.SDK_VERSION;
import static fr.kwanko.params.TrackingParams.TIMEZONE;
import static fr.kwanko.params.TrackingParams.USER_AGENT;
import static fr.kwanko.params.TrackingParams.USER_ID;
import static fr.kwanko.params.TrackingParams.WIFI_ACCESS_POINTS;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class NativeAdsTrackingParamsEvaluator extends ParamsEvaluator {

    private static final String [] REQUESTED_PARAMS =
            new String[]{DEVICE_TYPE,LANGUAGE,MODEL,MAKE,OS,OSV, SDK_VERSION,TIMEZONE,
                    SCREEN_WIDTH,CONNECTIVITY,SCREEN_HEIGHT,USER_AGENT,DOMAIN,CARRIERS,
                    USER_ID,LAT,LONG, HOME_MOBILE_COUNTRY_CODE, HOME_MOBILE_NETWORK_CODE,
                    RADIO_TYPE,CELL_TOWERS,WIFI_ACCESS_POINTS};

    public NativeAdsTrackingParamsEvaluator() {
        super(REQUESTED_PARAMS, new ParamsRetrieverFactoryImpl(),
                ParamsCacheSingleton.Instance.getParamCache());
    }
}
