package fr.kwanko.params;

import android.telephony.TelephonyManager;
import android.util.SparseIntArray;

/**
 * SourceCode
 * Created by erusu on 5/18/2017.
 */

public class Connectivity {

    public static final int UNKNOWN = 0;
    public static final int WIFI = 1;
    public static final int _4G = 2;
    public static final int _3G = 3;
    public static final int _2G = 6;
    public static final int EDGE = 4;
    public static final int H_PLUS = 5;

    private static final SparseIntArray map = new SparseIntArray();

    static {
        map.append(TelephonyManager.NETWORK_TYPE_GPRS,_2G);
        map.append(TelephonyManager.NETWORK_TYPE_EDGE,_2G);
        map.append(TelephonyManager.NETWORK_TYPE_CDMA,_2G);
        map.append(TelephonyManager.NETWORK_TYPE_1xRTT,_2G);
        map.append(TelephonyManager.NETWORK_TYPE_IDEN,_2G);
        map.append(TelephonyManager.NETWORK_TYPE_UMTS,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_EVDO_0,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_EVDO_A,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_HSDPA,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_HSUPA,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_HSPA,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_EVDO_B,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_EHRPD,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_HSPAP,_3G);
        map.append(TelephonyManager.NETWORK_TYPE_LTE,_4G);
    }

    private Connectivity(){
        throw new AssertionError("instance is not allowed");
    }

    public static @TrackingTypes.Connectivity int getConnectivity(int telephonyManagerConstant){
        @TrackingTypes.Connectivity int r = map.get(telephonyManagerConstant,Connectivity.UNKNOWN);
        return r;
    }
}
