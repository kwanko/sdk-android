package fr.kwanko.services;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.permissions.PermissionUtils;
import fr.kwanko.common.Preconditions;

import fr.kwanko.common.permissions.request.PermissionRequester;
import fr.kwanko.common.permissions.request.featurers.CommonFeatures;

/**
 * SourceCode
 * Created by erusu on 2/24/2017.
 */

public class LocationProvider {

    private static final String PREF_NAME ="locationPrefs.dat";
    private static final String LAT = "lat";
    private static final String LONG = "long";
    private static final String PROVIDER = "provider";
    private static final int ACCURACY_HIGH = 1;
    private static final int ACCURACY_LOW = 0;
    private static final String [] PROVIDER_FOR_ACCURACY = new String []{
            LocationManager.GPS_PROVIDER,LocationManager.NETWORK_PROVIDER
    };
    private static final long TIMEOUT = 6000;


    public static Location getLastKnownLocation(Context context){

        Preconditions.checkNotNull(context);
        if(!havePermissions(context)){
            return getSavedLastKnownLocation(context);
        }

        Location highAccuracyLocation = getLocationForAccuracy(context,ACCURACY_HIGH);
        Location lowAccuracyLocation = getLocationForAccuracy(context,ACCURACY_LOW);

        Location location = returnRecentLocation(highAccuracyLocation,lowAccuracyLocation);
        if(location == null){
            return getSavedLastKnownLocation(context);
        }
        saveLastKnownLocation(context,location);
        return location;
    }

    private static boolean havePermissions(Context context){
        return PermissionUtils.checkAtLeastOnePermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private static Location returnRecentLocation(Location l1,Location l2){
        if(l1 == null){
            return l2;
        }
        if(l2 == null){
            return l1;
        }
        return l1.getTime() > l2.getTime() ? l1:l2;
    }

    @SuppressWarnings("all")
    private static Location getLocationForAccuracy(Context context, int accurracy){

        final LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null){
            return null;
        }
        try{
            Location location = locationManager.getLastKnownLocation(PROVIDER_FOR_ACCURACY[accurracy]);
            return location;
        } catch (SecurityException e) {
            KwankoLog.e(e);
        } catch (IllegalArgumentException e) {
            KwankoLog.e(e);
        } catch (NullPointerException e) { // This happens on 4.2.2 on a few Android TV devices
            KwankoLog.e(e);
        }
        return null;
    }

    private static Location getSavedLastKnownLocation(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String latString = preferences.getString(LAT,null);
        if(latString == null){
            return null;
        }
        String longString = preferences.getString(LONG,null);
        String provider = preferences.getString(PROVIDER,null);
        Location location = new Location(provider);
        location.setLatitude(Double.parseDouble(latString));
        location.setLongitude(Double.parseDouble(longString));
        return location;
    }

    private static void saveLastKnownLocation(Context context, Location location){
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        preferences
                .edit()
                .putString(LAT,String.valueOf(location.getLatitude()))
                .putString(LONG,String.valueOf(location.getLongitude()))
                .putString(PROVIDER,location.getProvider())
                .apply();
    }

    public static void requestLocation(Context context,boolean forceGeoloc,LocationListener listener){
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(listener);
        if(forceGeoloc){
            forceRequestLocation(context,listener);
            return;
        }
        if(PermissionUtils.checkPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)){
            requestFreshLocation(context, listener);
            return;
        }
        listener.onLocationResult(getSavedLastKnownLocation(context));
    }

    @SuppressWarnings("all")
    private static void requestFreshLocation(Context context,LocationListener listener){
        new LocationRequestManager(context,listener,TIMEOUT).performRequest();
    }

    private static void forceRequestLocation(final Context context, final LocationListener listener){
        PermissionRequester.instance()
                .withContext(context)
                .requestFor(CommonFeatures.Location)
                .whenGranted(new PermissionRequester.Callback<PermissionRequester.GrantedArgument>() {
                    @Override
                    public void call(PermissionRequester.GrantedArgument argument) {
                        requestFreshLocation(context,listener);
                    }
                })
                .whenDenied(new PermissionRequester.Callback<PermissionRequester.DeniedArgument>() {
                    @Override
                    public void call(PermissionRequester.DeniedArgument argument) {
                        listener.onLocationResult(getLastKnownLocation(context));
                    }
                })
                .startRequest();
    }

    public interface LocationListener{
        void onLocationResult(Location location);
    }

    private static class LocationRequestManager{

        private long timeoutMs;
        private Context context;
        private LocationListener listener;
        private LocationManager locationManager;
        private Handler handler;

        private android.location.LocationListener innerListener = new android.location.LocationListener() {
            @SuppressWarnings("all")
            @Override
            public void onLocationChanged(Location location) {
                saveLastKnownLocation(context,location);
                cleanUp();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //do nothing
            }

            @Override
            public void onProviderEnabled(String s) {
                //do nothing
            }

            @Override
            public void onProviderDisabled(String s) {
                //do nothing
            }
        };

        private Runnable timeoutRunnable = new Runnable() {
            @SuppressWarnings("all")
            @Override
            public void run() {
                cleanUp();
            }
        };

        public LocationRequestManager(Context context,LocationListener listener, long timeoutMs){
            this.context = context;
            this.listener = listener;
            this.timeoutMs = timeoutMs;
            this.handler = new Handler();
        }

        @SuppressWarnings("all")
        public void performRequest(){
            locationManager =
                    (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager == null){
                listener.onLocationResult(getSavedLastKnownLocation(context));
                return;
            }
            try{
                String provider = getMainAvailableProvider(locationManager);
                if(provider == null){
                    listener.onLocationResult(getSavedLastKnownLocation(context));
                    KwankoLog.e(new IllegalStateException("no providers availble"));
                    return;
                }
                locationManager.requestSingleUpdate(provider,innerListener, null);
                listener.onLocationResult(getLastKnownLocation(context));
            }catch (SecurityException e) {
                KwankoLog.e(e);
                listener.onLocationResult(getSavedLastKnownLocation(context));
            } catch (IllegalArgumentException e) {
                KwankoLog.e(e);
                listener.onLocationResult(getSavedLastKnownLocation(context));
            } catch (NullPointerException e) { // This happens on 4.2.2 on a few Android TV devices
                KwankoLog.e(e);
                listener.onLocationResult(getSavedLastKnownLocation(context));
            }
            handler.postDelayed(timeoutRunnable,timeoutMs);
        }

        @Nullable
        private String getMainAvailableProvider(LocationManager manager){
            Criteria highAcuracyCriteria = new Criteria();
            highAcuracyCriteria.setAccuracy(Criteria.ACCURACY_FINE);
            String highAccuracyProvider = manager.getBestProvider(highAcuracyCriteria,true);
            if(highAccuracyProvider != null){
                return highAccuracyProvider;
            }
            Criteria coarseCriteria = new Criteria();
            coarseCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
            return manager.getBestProvider(coarseCriteria,true);
        }

        @SuppressWarnings("all")
        private void cleanUp(){
            if(locationManager != null && innerListener != null){
                locationManager.removeUpdates(innerListener);
            }
            if(handler!= null){
                handler.removeCallbacksAndMessages(null);
            }
            context = null;
            listener = null;
            innerListener = null;
            locationManager = null;
            handler = null;
        }
    }



}
