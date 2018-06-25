package fr.kwanko.common.permissions.request.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by erusu on 10/19/2016.
 */
@SuppressWarnings("all")
public class HardwareUtils {
    private HardwareUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static boolean isBleSupported(Context context){
        return true;
    }

    public static boolean isBluetoothSupported(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public static boolean isBluetoothOn(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter!=null && bluetoothAdapter.isEnabled();
    }

    public static boolean areMainLocationProvidersAvailable(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getAllProviders();
        for(String provider:providers){
            if(LocationManager.GPS_PROVIDER.equals(provider) || LocationManager.NETWORK_PROVIDER.equals(provider)){
                return true;
            }
        }
        return false;
    }

    public static boolean areMainLocationProvidersEnabled(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled;
        boolean networkEnabled;

        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gpsEnabled || networkEnabled;
    }
}
