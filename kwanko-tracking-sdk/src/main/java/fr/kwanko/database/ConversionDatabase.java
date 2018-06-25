package fr.kwanko.database;

import android.content.Context;

/**
 * SourceCode
 * Created by erusu on 3/17/2017.
 */

public interface ConversionDatabase {

    boolean isConversionRepeatable(String trackingId);

    void updateConversion(Conversion conversion);

    boolean existConversion(String trackingId);

    class Factory{
        public static ConversionDatabase getConversionDatabase(Context context){
            return new SharedPreferencesConversionDatabaseImpl(context);
        }
    }
}
