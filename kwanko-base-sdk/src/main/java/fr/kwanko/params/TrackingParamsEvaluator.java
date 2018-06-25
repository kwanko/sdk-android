package fr.kwanko.params;

import static fr.kwanko.params.TrackingParams.*;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 * This class should process the params received from user and complete the list with the one
 * filled by the sdk
 */

public class TrackingParamsEvaluator extends ParamsEvaluator {

    private static final String [] REQUESTED_PARAMS =
            new String[]{DEVICE_TYPE,LANGUAGE,MODEL,MAKE,OS,OSV, SDK_VERSION,TIMEZONE,
                    SCREEN_WIDTH,CONNECTIVITY,SCREEN_HEIGHT,USER_AGENT,DOMAIN,CARRIERS,
                    USER_ID,LAT,LONG, HOME_MOBILE_COUNTRY_CODE, HOME_MOBILE_NETWORK_CODE,
                    RADIO_TYPE,CELL_TOWERS,WIFI_ACCESS_POINTS};

    public TrackingParamsEvaluator() {
        super(REQUESTED_PARAMS, new ParamsRetrieverFactoryImpl(),
                ParamsCacheSingleton.Instance.getParamCache());
    }
}
