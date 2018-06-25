package fr.kwanko.params;

import java.util.List;
import java.util.Map;

/**
 * SourceCode
 * Created by erusu on 3/14/2017.
 */

public class TrackingParams extends ParamMap {

    static final String DEVICE_TYPE = "devicetype";
    static final String LANGUAGE = "language";
    static final String MAKE = "make";
    static final String MODEL = "model";
    static final String OS = "os";
    static final String OSV = "osv";
    static final String SDK_VERSION ="sdkv";
    static final String TIMEZONE = "timezone";
    static final String SCREEN_WIDTH = "screenWidth";
    static final String CONNECTIVITY = "connectivity";
    static final String SCREEN_HEIGHT = "screenHeight";
    static final String CATEGORIES = "categories";
    static final String USER_AGENT = "ua";
    static final String DOMAIN = "domain";
    public static final String AD_WIDTH = "adWidth";
    public static final String AD_HEIGHT = "adHeight";
    public static final String POSITION = "adPosition";
    static final String CUSTOM_PARAM = "customParam";
    static final String CARRIERS = "carriers";
    public static final String AD_SIZE_STRATEGY = "adSizeStrategy";
    static final String USER_ID = "userId";
    public static final String FORCE_GEOLOC = "forceGeoloc";
    public static final String LAT = "lat";
    public static final String LONG = "long";
    static final String HOME_MOBILE_COUNTRY_CODE = "homeMobileCountryCode";
    static final String HOME_MOBILE_NETWORK_CODE = "homeMobileNetworkCode";
    static final String RADIO_TYPE = "radioType";
    static final String CELL_TOWERS = "cellTowers";
    static final String WIFI_ACCESS_POINTS = "wifiAccessPoints";

    private TrackingParams(Map<String, ParamValue> params) {
        super(params);
    }

    public static class Builder extends ParamMap.Builder{

        public Builder(){
            super();
        }

        Builder(ParamMap params){
            super(params);
        }

        @Override
        public ParamMap build(){
            return new ParamMap(buildMap);
        }

        Builder setLanguage(String language){
            return setParam(LANGUAGE,new StringParamValue(language));
        }

        /**
         * @param manufactureName is the name of the device manufacturer for instance Google for
         * the device Pixel;
         * @return builder instance
         */
        public Builder setDeviceManufacturer(String manufactureName){
            return setParam(MAKE,new StringParamValue(manufactureName));
        }

        /**
         * @param deviceModel is the device model, for instace for a Nexus phone the device model
         * @return builder instance
         */
        public Builder setDeviceModel(String deviceModel){
            return setParam(MODEL,new StringParamValue(deviceModel));
        }

        public Builder setOs(@TrackingTypes.Os String os){
            return setParam(OS, new StringParamValue(os));
        }

        public Builder setOsVersion(String osVersion){
            return setParam(OSV,new StringParamValue(osVersion));
        }

        public Builder setTimezone(String timezone){
            return setParam(TIMEZONE, new StringParamValue(timezone));
        }

        public Builder setConnectivity(String connectivity){
            return setParam(CONNECTIVITY, new StringParamValue(connectivity));
        }

        public Builder setCategories(String [] categories){
            return setParam(CATEGORIES, new StringParamValue(categories));
        }

        public Builder setCategories(List<String> categories){
            return setParam(CATEGORIES, new StringParamValue(categories));
        }

        public Builder setAdWidth(int dimen){
            return setParam(AD_WIDTH, new StringParamValue(dimen));
        }

        public Builder setAdHeight(int dimen){
            return setParam(AD_HEIGHT, new StringParamValue(dimen));
        }

        public Builder setPosition(@TrackingTypes.Position String position){
            return setParam(POSITION,new StringParamValue(position));
        }

        public Builder setCustomParams(Map<String,String> params){
            return setParam(CUSTOM_PARAM,new StringParamValue(params));
        }

        public Builder setCarrier(String carrier){
            return setParam(CARRIERS,new StringParamValue(carrier));
        }

        public Builder setAdSizeStrategy(@TrackingTypes.AdSizeStrategy String adSizeStrategy){
            return  setParam(AD_SIZE_STRATEGY, new StringParamValue(adSizeStrategy));
        }

        public Builder setForceGeoloc(boolean forceGeoloc){
            return setParam(FORCE_GEOLOC,new StringParamValue(forceGeoloc));
        }
    }
}
