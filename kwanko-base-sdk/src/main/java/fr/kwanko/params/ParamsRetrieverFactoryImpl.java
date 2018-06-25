package fr.kwanko.params;

import android.content.Context;
import android.location.Location;

import java.util.HashMap;

import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.services.LocationProvider;

import static fr.kwanko.common.KwankoDimensionUtils.*;
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
import static fr.kwanko.params.TrackingParamsUtils.getApplicationName;
import static fr.kwanko.params.TrackingParamsUtils.getCarrier;
import static fr.kwanko.params.TrackingParamsUtils.getCellTowers;
import static fr.kwanko.params.TrackingParamsUtils.getConnectivity;
import static fr.kwanko.params.TrackingParamsUtils.getDeviceLanguage;
import static fr.kwanko.params.TrackingParamsUtils.getDeviceManufacturer;
import static fr.kwanko.params.TrackingParamsUtils.getDeviceModel;
import static fr.kwanko.params.TrackingParamsUtils.getDeviceType;
import static fr.kwanko.params.TrackingParamsUtils.getMobileCountryCode;
import static fr.kwanko.params.TrackingParamsUtils.getMobileNetworkCode;
import static fr.kwanko.params.TrackingParamsUtils.getOsVersion;
import static fr.kwanko.params.TrackingParamsUtils.getRadioType;
import static fr.kwanko.params.TrackingParamsUtils.getScreenHeight;
import static fr.kwanko.params.TrackingParamsUtils.getScreenWidth;
import static fr.kwanko.params.TrackingParamsUtils.getSdkVersion;
import static fr.kwanko.params.TrackingParamsUtils.getTimezone;
import static fr.kwanko.params.TrackingParamsUtils.getUserAgent;
import static fr.kwanko.params.TrackingParamsUtils.getWifiAccessPoints;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

class ParamsRetrieverFactoryImpl implements ParamsRetrieverFactory {

    private  final HashMap<String,ParamsRetriever> retrievers;

    ParamsRetrieverFactoryImpl(){
        retrievers = new  HashMap<>();
        createRetrieverMap();
    }

    private void createRetrieverMap(){
        retrievers.put(DEVICE_TYPE, new DeviceTypeRetriever(DEVICE_TYPE));
        retrievers.put(LANGUAGE, new LanguageRetriever(LANGUAGE));
        retrievers.put(MAKE, new MakeRetriever(MAKE));
        retrievers.put(MODEL, new ModelRetriever(MODEL));
        retrievers.put(OS,new OsRetriever(OS));
        retrievers.put(OSV, new OsvRetriever(OSV));
        retrievers.put(SDK_VERSION,new SdkVersionRetriever(SDK_VERSION));
        retrievers.put(TIMEZONE, new TimezoneRetriever(TIMEZONE));
        retrievers.put(SCREEN_WIDTH, new ScreenWidthRetriever(SCREEN_WIDTH));
        retrievers.put(CONNECTIVITY, new ConnectivityRetriever(CONNECTIVITY));
        retrievers.put(SCREEN_HEIGHT, new ScreenHeightRetriever(SCREEN_HEIGHT));
        retrievers.put(USER_AGENT, new UserAgentRetriever(USER_AGENT));
        retrievers.put(DOMAIN, new DomainRetriever(DOMAIN));
        retrievers.put(CARRIERS, new CarrierRetriever(CARRIERS));
        retrievers.put(USER_ID, new UserIdRetriever(USER_ID));
        retrievers.put(LAT, new LatRetriever(LAT));
        retrievers.put(LONG,new LongRetriever(LONG));
        retrievers.put(HOME_MOBILE_COUNTRY_CODE, new HmCountryCodeRetriever());
        retrievers.put(HOME_MOBILE_NETWORK_CODE, new HmNetworkCodeRetriever());
        retrievers.put(CELL_TOWERS,new CellTowerInfoRetriever());
        retrievers.put(WIFI_ACCESS_POINTS, new WifiAccessPointsRetriever());
        retrievers.put(RADIO_TYPE, new RatioTypeRetriever());
    }

    static class DeviceTypeRetriever extends ParamsRetriever{

        public DeviceTypeRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getDeviceType(context),true);
        }
    }

    public ParamsRetriever getParamRetriever(String paramKey){
        if(paramKey == null || paramKey.isEmpty()){
            throw new IllegalArgumentException("paramKey should not be null or empty");
        }
        if(!retrievers.containsKey(paramKey)){
            throw new IllegalArgumentException("no retriever corresponds to the given paramKey");
        }
        return retrievers.get(paramKey);
    }

    private static class LanguageRetriever extends ParamsRetriever{

        LanguageRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getDeviceLanguage(),true);
        }
    }

    private static class MakeRetriever extends ParamsRetriever{

        MakeRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getDeviceManufacturer(),true);
        }
    }

    private static class ModelRetriever extends ParamsRetriever{

        ModelRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getDeviceModel(),true);
        }
    }

    private static class OsRetriever extends ParamsRetriever{

        OsRetriever(String paramKey){
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(Os.ANDROID,true);
        }
    }

    private static class OsvRetriever extends ParamsRetriever{

        OsvRetriever(String paramKey){
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getOsVersion(),true);
        }
    }

    private static class SdkVersionRetriever extends ParamsRetriever{

        SdkVersionRetriever(String paramKey){
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getSdkVersion(),true);
        }
    }

    private static class TimezoneRetriever extends ParamsRetriever{

        TimezoneRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getTimezone(),true);
        }
    }

    private static class ScreenWidthRetriever extends ParamsRetriever{

        ScreenWidthRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(
                    scaleToServerSize(context,
                            getScreenWidth()),true);
        }
    }

    private static class ScreenHeightRetriever extends ParamsRetriever{

        ScreenHeightRetriever(String paramKey){
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(
                    scaleToServerSize(context,
                    getScreenHeight())
                    ,true);
        }
    }

    private static class UserAgentRetriever extends ParamsRetriever{

        UserAgentRetriever(String param){
            super(param);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getUserAgent(),true);
        }

    }

    private static class DomainRetriever extends ParamsRetriever{
        DomainRetriever(String param){
            super(param);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getApplicationName(context),true);
        }
    }

    private static class CarrierRetriever extends ParamsRetriever{

        CarrierRetriever(String param){
            super(param);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getCarrier(context),true,false);
        }
    }

    private static class LatRetriever extends ParamsRetriever{


        LatRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            Location loc = LocationProvider.getLastKnownLocation(context);
            if(loc!= null){
                return new StringParamValue(loc.getLatitude(),true,false);
            }
            return null;
        }
    }

    private static class LongRetriever extends ParamsRetriever{


        LongRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            Location loc = LocationProvider.getLastKnownLocation(context);
            if(loc!= null){
                return new StringParamValue(loc.getLongitude(),true,false);
            }
            return null;
        }
    }

    private static class HmCountryCodeRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getMobileCountryCode(context),true);
        }
    }

    private static class HmNetworkCodeRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getMobileNetworkCode(context),true);
        }
    }

    private static class CellTowerInfoRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getCellTowers(context),true,false);
        }
    }

    private static class WifiAccessPointsRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getWifiAccessPoints(context),true,false);
        }
    }

    private static class RatioTypeRetriever extends ParamsRetriever{

        @Override
        ParamValue retrieveParam(Context context) {
            return new StringParamValue(getRadioType(context),true,false);
        }
    }

    private static class ConnectivityRetriever extends ParamsRetriever{

        ConnectivityRetriever(String paramKey) {
            super(paramKey);
        }

        @Override
        ParamValue retrieveParam(Context context) {
            int connectivity = getConnectivity(context);
            return new StringParamValue(connectivity,true,false);
        }
    }

}
