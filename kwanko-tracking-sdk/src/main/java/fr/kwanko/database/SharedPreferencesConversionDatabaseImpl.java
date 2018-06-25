package fr.kwanko.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SourceCode
 * Created by erusu on 3/17/2017.
 */

class SharedPreferencesConversionDatabaseImpl implements ConversionDatabase {

    private static final String NAME = "conversion.pref";
    private SharedPreferences pref;

    SharedPreferencesConversionDatabaseImpl(Context context){
        pref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    @Override
    public synchronized boolean isConversionRepeatable(String trackingId) {
        pref.getBoolean(trackingId,false);
        return false;
    }

    @Override
    public synchronized void updateConversion(Conversion conversion) {
        pref.edit()
                .putBoolean(conversion.getTrackingId(),conversion.isRepeatable())
                .apply();
    }

    @Override
    public synchronized boolean existConversion(String trackingId) {
        return pref.contains(trackingId);
    }
}
