package fr.kwanko.internal.model;

import android.support.annotation.IntDef;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.kwanko.SupportedFormats;
import fr.kwanko.SupportedMediationTargets;
import fr.kwanko.common.HtmlUtils;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.mediation.admob.AdMobMediationParams;
import fr.kwanko.internal.mraid.js.MraidJavascript;
import fr.kwanko.rest.KwankoAdType;

import static fr.kwanko.params.TrackingParams.AD_HEIGHT;
import static fr.kwanko.params.TrackingParams.AD_SIZE_STRATEGY;
import static fr.kwanko.params.TrackingParams.AD_WIDTH;

/**
 * Created by vfatu on 26.01.2017.
 * This class contain a lot of vulnerable code because of the necessity to mock stuff
 * that currently we do not receive from server.
 */

public class AdModel implements Serializable{

    public static final int WEBVIEW = 0;
    public static final int BROWSER = 1;
    private static final String TIME_BEFORE_OVERLAY = "timeBeforeOverlay";
    private static final String OVERLAY_COUNTDOWN = "overlayCountdown";
    private static final String OPEN_STRATEGY = "openStrategy";
    private static final String FORCE_GEOLOC = "forceGeoloc";
    private static final String IP_GEO_FALLBACK = "ipGeoFallback";
    private static final Integer LAT = 0;
    private static final Integer LONG = 1;
    private static final String MRAID_EVENTS = "mraidEvents";
    private static final int DEFAULT_STRATEGY = WEBVIEW;
    private static final List<String> OPEN_STRATEGIES = Arrays.asList("webview", "browser");

    public static  int DEFAULT_TIME_BEFORE_OVERLY = 0;
    public static  int DEFAULT_OVERLY_COUNTDOWN = 0;

    private String html;
    private String cookie;
    private KwankoAdType adType;
    @OpenStrategy
    private  int openStrategy;
    private int timeBeforeOverly = DEFAULT_TIME_BEFORE_OVERLY;
    private int overlyCountdown = DEFAULT_OVERLY_COUNTDOWN;
    private boolean isUrl = false;
    private boolean responsiveHtml = false;
    private boolean forceGeoloc = false;
    private double fallbackLat;
    private double fallbackLong;
    private Map<String,String> mraidEvents;
    private int adWidth;
    private int adHeight;
    private String adSizeStrategy;
    private String format;
    private boolean isMraid = false;
    private String slotId;
    private Mediation mediation;
    private String position;
    private CloseButtonMetadata closeButtonMetadata;


    public AdModel(){
        //empty constructor
    }

    public AdModel(String data) throws JSONException, EmptyAdModelException {
        KwankoLog.logResponse(data);
        Object obj = new JSONTokener(data).nextValue();
        if(obj instanceof JSONArray) {
            throw new EmptyAdModelException();
        }
        JSONObject dataObject = new JSONObject(data);
        this.cookie = dataObject.getString("ck");
        JSONObject ret = new JSONObject(dataObject.getString("ret"));
        String type = dataObject.getString("t");
        if ("E".equals(type)) {
            this.html = ret.getString("html");
            String scriptToAdd = "<script type=\"text/javascript\"> " + MraidJavascript.FILTERED_JAVASCRIPT_SOURCE + "</script>";
            this.html = HtmlUtils.insertScript(this.html, scriptToAdd);
            this.isMraid = true;
        } else if(SupportedMediationTargets.ADMOB.equals(type)){
            if(dataObject.has("adInfo")){
                JSONObject adInfo = dataObject.getJSONObject("adInfo");
                if(adInfo != null){
                    mediation = new Mediation();
                    if(adInfo.has("interstitial")){
                        mediation.setAdFormat(SupportedFormats.FORMAT_OVERLY);
                    }
                    if(adInfo.has("banner")){
                        mediation.setAdFormat(SupportedFormats.FORMAT_BANNER);
                    }
                    Map<String,String> params = new HashMap<>();
                    params.put(AdMobMediationParams.UNIT_ID,
                            adInfo.getString(AdMobMediationParams.UNIT_ID));
                    params.put(AdMobMediationParams.APPLICATION_ID,
                            adInfo.getString(AdMobMediationParams.APPLICATION_ID));
                    mediation.setParams(params);
                    mediation.setMediationTarget(SupportedMediationTargets.ADMOB);
                }
            }

        } else {
            this.html = ret.getString("html");
            this.isMraid = false;
        }
        //parse openStrategy
        if (dataObject.has(OPEN_STRATEGY)) {
            String openStrategyString = dataObject.getString(OPEN_STRATEGY);
            if (!OPEN_STRATEGIES.contains(openStrategyString)) {
                openStrategy = DEFAULT_STRATEGY;
                return;
            }
            @OpenStrategy int temp = OPEN_STRATEGIES.indexOf(openStrategyString);
            openStrategy = temp;
        } else {
            openStrategy = DEFAULT_STRATEGY;
        }
        //parseTimeBeforeOverlay
        if(dataObject.has(TIME_BEFORE_OVERLAY)){
            timeBeforeOverly = dataObject.getInt(TIME_BEFORE_OVERLAY);
        }
        //parse overlyCountdown
        if(dataObject.has(OVERLAY_COUNTDOWN)){
            overlyCountdown = dataObject.getInt(OVERLAY_COUNTDOWN);
        }
        //parse forceGeoloc
        if(dataObject.has(FORCE_GEOLOC)){
            forceGeoloc = dataObject.getBoolean(FORCE_GEOLOC);
        }
        //parse IpGeoFallback
        if(dataObject.has(IP_GEO_FALLBACK)){
            JSONArray ipGeoFallback = dataObject.getJSONArray(IP_GEO_FALLBACK);
            if(ipGeoFallback != null){
                fallbackLat = ipGeoFallback.getDouble(LAT);
                fallbackLong = ipGeoFallback.getDouble(LONG);
            }
        }
        //parse mRaidEvents
        mraidEvents = new HashMap<>();
        JSONObject mraidEventsObject = dataObject.optJSONObject(MRAID_EVENTS);
        if(mraidEventsObject != null){
            Iterator<String> iterator = mraidEventsObject.keys();
            while(iterator.hasNext()){
                String key = iterator.next();
                mraidEvents.put(key,mraidEventsObject.optString(key));
            }
        }
        //parse adWidth and adHeight and adSizeStrategy
        if(dataObject.has(AD_WIDTH)){
            adWidth = dataObject.optInt(AD_WIDTH);
        }
        if(dataObject.has(AD_HEIGHT)){
            adHeight = dataObject.optInt(AD_HEIGHT);
        }
        if(dataObject.has(AD_SIZE_STRATEGY)){
            adSizeStrategy = dataObject.optString(AD_SIZE_STRATEGY, null);
        }
        if(dataObject.has("closeButton")){
            JSONObject closeButtonObject = dataObject.getJSONObject("closeButton");
            String url = closeButtonObject.getString("src");
            String padding = closeButtonObject.getString("padding");
            String size = closeButtonObject.getString("size");
            closeButtonMetadata = new CloseButtonMetadata(url,size,padding);
        }else {
            closeButtonMetadata = getDefaultCloseButtonMetadata();
        }
    }

    public String getHtml() {
        return html;
    }

    private CloseButtonMetadata getDefaultCloseButtonMetadata(){
        return new CloseButtonMetadata(null, "50x50", "8");
    }

    public boolean isMraid(){
        return isMraid;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public KwankoAdType getAdType() {
        return adType;
    }

    public void setAdType(KwankoAdType adType) {
        this.adType = adType;
    }

    public int getTimeBeforeOverly() {
        return timeBeforeOverly;
    }

    public int getOverlyCountdown() {
        return overlyCountdown;
    }

    public void setUrl(boolean url) {
        isUrl = url;
    }

    public boolean isResponsiveHtml() {
        return responsiveHtml;
    }

    public void setResponsiveHtml(boolean responsiveHtml) {
        this.responsiveHtml = responsiveHtml;
    }

    public boolean isUrl() {
        return isUrl;
    }

    @OpenStrategy
    public  int getOpenStrategy() {
        return openStrategy;
    }

    public void setOpenStrategy(int openStrategy) {
        this.openStrategy = openStrategy;
    }

    public boolean isForceGeoloc() {
        return forceGeoloc;
    }

    public static int getWEBVIEW() {
        return WEBVIEW;
    }

    public double getFallbackLat() {
        return fallbackLat;
    }

    public double getFallbackLong() {
        return fallbackLong;
    }

    public void setTimeBeforeOverly(int timeBeforeOverly) {
        this.timeBeforeOverly = timeBeforeOverly;
    }

    public void setOverlyCountdown(int overlyCountdown) {
        this.overlyCountdown = overlyCountdown;
    }

    public Map<String, String> getMraidEvents() {
        return mraidEvents;
    }

    public int getAdWidth() {
        return adWidth;
    }

    public int getAdHeight() {
        return adHeight;
    }

    public String getAdSizeStrategy() {
        return adSizeStrategy;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public void setAdWidth(int adWidth) {
        this.adWidth = adWidth;
    }

    public void setAdHeight(int adHeight) {
        this.adHeight = adHeight;
    }

    public Mediation getMediation() {
        return mediation;
    }

    public void setMediation(Mediation mediation) {
        this.mediation = mediation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public CloseButtonMetadata getCloseButtonMetadata() {
        return closeButtonMetadata;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({WEBVIEW, BROWSER})
    public @interface OpenStrategy {
    }
}
