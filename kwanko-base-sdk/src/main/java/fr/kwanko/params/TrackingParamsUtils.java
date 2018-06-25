package fr.kwanko.params;

import android.Manifest;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;

import com.ads.kwanko.kwankoandroidsdk.base.BuildConfig;
import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.common.KwankoStringUtils;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.Preconditions;
import fr.kwanko.common.permissions.PermissionUtils;
import fr.kwanko.services.GplayServicesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class TrackingParamsUtils {

    private static final String CELL_ID = "cellId";
    private static final String LOCATION_AREA_CODE = "locationAreaCode";
    private static final String SIGNAL_STRENGTH = "signalStrength";
    private static final String MOBILE_COUNTRY_CODE = "mobileCountryCode";
    private static final String MOBILE_NETWORK_CODE = "mobileNetworkCode";
    private static final String AGE = "age";
    private static final String TIMING_ADVANCE = "timingAdvance";
    private static final int LOWEST_SUB = -113;
    private static final int TWO = 2;
    private static final int MAX_RESULTS = 5;
    private final static List<Integer> channelsFrequency = new ArrayList<>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));
    private static final String MAC_ADDRESS = "macAddress";
    private static final String CHANNEL = "channel";

    private TrackingParamsUtils() {
        throw new AssertionError("instance is not allowed");
    }

    @TrackingTypes.DeviceType
    public static int getDeviceType(Context context) {
        Preconditions.checkNotNull(context);
        if (isDeviceTv(context)) {
            return DeviceType.TELEVISION;
        } else if (isDeviceTablet(context)) {
            return DeviceType.TABLET;
        } else if (isDevicePhone(context)) {
            return DeviceType.MOBILE;
        } else {
            return DeviceType.OTHER;
        }
    }

    private static boolean isDeviceTablet(Context context) {
        return context.getResources().getBoolean(R.bool.kwanko_is_tablet);
    }

    private static boolean isDeviceTv(Context context) {
        UiModeManager uiModeManager =
                (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager != null &&
                uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    private static boolean isDevicePhone(Context context) {
        TelephonyManager manager =
                (TelephonyManager) context.getApplicationContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);
        return manager != null &&
                manager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    static String getDeviceLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        if (KwankoStringUtils.isEmpty(manufacturer)) {
            manufacturer = "unknown manufacture";
        }
        return manufacturer;
    }

    public static String getDeviceModel() {
        String deviceModel = Build.MODEL;
        if (KwankoStringUtils.isEmpty(deviceModel)) {
            deviceModel = "unknown model";
        }
        return deviceModel;
    }

    public static String getOsVersion() {
        return String.valueOf(Build.VERSION.RELEASE);
    }

    static String getSdkVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getTimezone() {
        return TimeZone.getDefault().getID();
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }

    @Nullable
    public static @TrackingTypes.Connectivity int getConnectivity(Context context) {
        ConnectivityManager connManager = null;
        try {
             connManager = (ConnectivityManager)
                    context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifi.isConnected()) {
                    return Connectivity.WIFI;
                }
            } else {
                Network current = connManager.getActiveNetwork();
                NetworkInfo currentInfo = connManager.getNetworkInfo(current);
                if(currentInfo == null){
                    return getMobileNetworkType(context);
                }
                if(currentInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    return Connectivity.WIFI;
                }
                if(currentInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                    return Connectivity.getConnectivity(currentInfo.getSubtype());
                }
                return Connectivity.UNKNOWN;
            }
        } catch (SecurityException e) {
            /*ignore exception because it can throw exception in case of not having permission
            for network state*/
            KwankoLog.e(e);
        }
        return Connectivity.UNKNOWN;
    }

    private static @TrackingTypes.Connectivity int getMobileNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        return Connectivity.getConnectivity(networkType);
    }

    static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    static String getApplicationName(Context context) {
        return context.getApplicationInfo()
                .loadLabel(context.getPackageManager()).toString();
    }

    public static String getCarrier(Context context) {
        try {
            TelephonyManager manager =
                    (TelephonyManager) context.getApplicationContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);
            if (manager == null) {
                return null;
            }
            if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return manager.getNetworkOperatorName();
            }
        } catch (SecurityException e) {
            KwankoLog.e(e);
            return null;
        }
        return null;
    }

    public static String getUserId(Context context) {
        return GplayServicesUtils.getAdvertisingId(context);
    }

    static String getMobileCountryCode(Context context) {

        try {
            TelephonyManager tel = (TelephonyManager) context
                    .getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (!KwankoStringUtils.isEmpty(networkOperator) && networkOperator.length() > 3) {
                return networkOperator.substring(0, 3);
            }
        } catch (SecurityException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    static String getMobileNetworkCode(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context
                    .getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (!KwankoStringUtils.isEmpty(networkOperator) && networkOperator.length() > 3) {
                return networkOperator.substring(3);
            }
        } catch (SecurityException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    static int getRadioType(Context context) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager)
                    context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyManager.getNetworkType();
        } catch (SecurityException e) {
            KwankoLog.e(e);
        }
        return TelephonyManager.NETWORK_TYPE_UNKNOWN;
    }

    static JSONArray getCellTowers(Context ctx) {
        try {
            TelephonyManager tel = (TelephonyManager) ctx
                    .getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            JSONArray cellList = new JSONArray();
            String defaultMcc = null, defaultMnc = null;
            String networkOperator = tel.getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 3) {
                defaultMcc = networkOperator.substring(0, 3);
                defaultMnc = networkOperator.substring(3);
            }
            int defaultCellId = 0, defaultLac = 0;
            CellLocation cellLocation = tel.getCellLocation();
            if (cellLocation != null) {
                if (cellLocation instanceof GsmCellLocation) {
                    defaultCellId = ((GsmCellLocation) cellLocation).getCid();
                    defaultLac = ((GsmCellLocation) cellLocation).getLac();
                }
                if (cellLocation instanceof CdmaCellLocation) {
                    defaultCellId = ((CdmaCellLocation) cellLocation).getNetworkId();
                }
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                List<NeighboringCellInfo> neighCells = tel.getNeighboringCellInfo();
                for (int i = 0; i < neighCells.size(); i++) {
                    try {
                        JSONObject cellObj = new JSONObject();
                        NeighboringCellInfo cellInfo = neighCells.get(i);
                        if (cellInfo.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS ||
                                cellInfo.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE) {
                            cellObj.put(CELL_ID, cellInfo.getCid());
                            cellObj.put(LOCATION_AREA_CODE, cellInfo.getLac());
                        } else {
                            if (defaultLac != 0) {
                                cellObj.put(LOCATION_AREA_CODE, defaultLac);
                            }
                            if (defaultCellId != 0) {
                                cellObj.put(CELL_ID, defaultCellId);
                            }
                        }
                        cellObj.put(SIGNAL_STRENGTH, convertTodBm(cellInfo.getRssi()));
                        if (defaultMcc != null) {
                            cellObj.put(MOBILE_COUNTRY_CODE, defaultMcc);
                        }
                        if (defaultMnc != null) {
                            cellObj.put(MOBILE_NETWORK_CODE, defaultMnc);
                        }
                        cellList.put(cellObj);
                    } catch (JSONException e) {
                        KwankoLog.e(e);
                    }
                }
            } else {
                List<CellInfo> infos = tel.getAllCellInfo();
                if(infos == null || infos.isEmpty()){
                    return cellList;
                }
                for (int i = 0; i < infos.size(); ++i) {
                    try {
                        JSONObject cellObj = new JSONObject();
                        CellInfo info = infos.get(i);
                        if (info instanceof CellInfoGsm) {
                            CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                            CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                            cellObj.put(CELL_ID, identityGsm.getCid());
                            cellObj.put(LOCATION_AREA_CODE, identityGsm.getLac());
                            cellObj.put(MOBILE_COUNTRY_CODE, identityGsm.getMcc());
                            cellObj.put(MOBILE_NETWORK_CODE, identityGsm.getMnc());
                            cellObj.put(SIGNAL_STRENGTH, gsm.getDbm());
                            cellObj.put(AGE, info.getTimeStamp());
                        } else if (info instanceof CellInfoLte) {
                            CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                            CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                            cellObj.put(CELL_ID, identityLte.getCi());
                            cellObj.put(LOCATION_AREA_CODE, identityLte.getTac());
                            cellObj.put(SIGNAL_STRENGTH, lte.getDbm());
                            cellObj.put(MOBILE_COUNTRY_CODE, identityLte.getMcc());
                            cellObj.put(MOBILE_NETWORK_CODE, identityLte.getMnc());
                            cellObj.put(AGE, info.getTimeStamp());
                            cellObj.put(TIMING_ADVANCE, lte.getTimingAdvance());
                        } else if (info instanceof CellInfoWcdma &&
                                Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                            cellObj.put(SIGNAL_STRENGTH, wcdma.getDbm());
                            CellIdentityWcdma wcdmaIdentity = ((CellInfoWcdma) info).getCellIdentity();
                            cellObj.put(CELL_ID, wcdmaIdentity.getCid());
                            cellObj.put(LOCATION_AREA_CODE, wcdmaIdentity.getLac());
                            cellObj.put(MOBILE_COUNTRY_CODE, wcdmaIdentity.getMcc());
                            cellObj.put(MOBILE_NETWORK_CODE, wcdmaIdentity.getMnc());
                            cellObj.put(AGE, info.getTimeStamp());
                        } else if (info instanceof CellInfoCdma) {
                            CellIdentityCdma cdmaIdentity = ((CellInfoCdma) info).getCellIdentity();
                            CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                            cellObj.put(CELL_ID, cdmaIdentity.getNetworkId());
                            cellObj.put(SIGNAL_STRENGTH, cdma.getDbm());
                            cellObj.put(AGE, info.getTimeStamp());
                            if (defaultMcc != null) {
                                cellObj.put(MOBILE_COUNTRY_CODE, defaultMcc);
                            }
                            if (defaultMnc != null) {
                                cellObj.put(MOBILE_NETWORK_CODE, defaultMnc);
                            }
                            if (defaultLac != 0) {
                                cellObj.put(LOCATION_AREA_CODE, defaultLac);
                            }
                        } else {
                            if (defaultCellId != 0) {
                                cellObj.put(CELL_ID, defaultCellId);
                            }
                            if (defaultMcc != null) {
                                cellObj.put(MOBILE_COUNTRY_CODE, defaultMcc);
                            }
                            if (defaultMnc != null) {
                                cellObj.put(MOBILE_NETWORK_CODE, defaultMnc);
                            }
                            if (defaultLac != 0) {
                                cellObj.put(LOCATION_AREA_CODE, defaultLac);
                            }
                        }
                        cellList.put(cellObj);
                    } catch (JSONException ex) {
                        KwankoLog.e(ex);
                    }
                }
            }
            return limitResults(cellList);
        } catch (SecurityException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    private static int convertTodBm(int rssi) {
        return rssi == NeighboringCellInfo.UNKNOWN_RSSI ? NeighboringCellInfo.UNKNOWN_RSSI :
                (LOWEST_SUB + TWO * rssi);
    }

    @SuppressWarnings("all")
    static JSONArray getWifiAccessPoints(Context context) {
        JSONArray array = new JSONArray();
        WifiManager manager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (manager == null) {
            return array;
        }
        if(!manager.isWifiEnabled()){
            return array;
        }
        if (PermissionUtils.checkPermission(context, Manifest.permission.CHANGE_WIFI_STATE)) {
            manager.startScan();
        }
        if (PermissionUtils.checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            List<ScanResult> results = manager.getScanResults();
            if (results != null && results.size() > 0) {
                for (ScanResult s : results) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put(MAC_ADDRESS, s.BSSID);
                        obj.put(SIGNAL_STRENGTH, s.level);
                        obj.put(CHANNEL, getChannelFromFrequency(s.frequency));
                        obj.put(AGE, s.timestamp);
                        array.put(obj);
                    } catch (JSONException e) {
                        KwankoLog.e(e);
                    }
                }
            } else {
                WifiInfo info = manager.getConnectionInfo();
                JSONObject obj = new JSONObject();
                try {
                    obj.put(MAC_ADDRESS, info.getBSSID());
                    obj.put(SIGNAL_STRENGTH, info.getRssi());
                    obj.put(CHANNEL, getChannelFromFrequency(info.getFrequency()));
                    array.put(obj);
                } catch (JSONException e) {
                    KwankoLog.e(e);
                }
            }
        }
        return limitResults(array);
    }

    private static JSONArray limitResults(JSONArray array){
        if(array == null){
            return null;
        }
        if(array.length() ==0){
            return array;
        }
        if(array.length() <= MAX_RESULTS){
            return array;
        }
        List<JSONObject> list = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            list.add(array.optJSONObject(i));
        }
        Collections.sort(list, SIGNAL_COMPARATOR);
        JSONArray res = new JSONArray();
        for(int i=0;i<MAX_RESULTS;i++){
            res.put(list.get(0));
        }
        return res;
    }

    private static Comparator<JSONObject> SIGNAL_COMPARATOR = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject j1, JSONObject j2) {
            int j1Signal = j1.optInt(SIGNAL_STRENGTH,Integer.MAX_VALUE);
            int j2Signal = j2.optInt(SIGNAL_STRENGTH,Integer.MAX_VALUE);

            if(j1Signal == Integer.MAX_VALUE && j2Signal == Integer.MAX_VALUE){
                return 0;
            }
            if(j2Signal == Integer.MAX_VALUE){
                return -1;
            }
            if(j1Signal == Integer.MAX_VALUE){
                return 1;
            }
            return WifiManager.compareSignalLevel(j2Signal,j1Signal);
        }
    };

    private static int getChannelFromFrequency(int frequency) {
        return channelsFrequency.indexOf(Integer.valueOf(frequency));
    }


}
