package fr.kwanko.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * SourceCode
 * Created by erusu on 2/23/2017.
 * todo replace this class with UserData class
 */

public class SharesPreferencesHelper {

    private static final String  PREF_NAME = "kwanko_prefs";
    private static final String ADVERT_ID = "kwanko_advert_id";
    private static final String ID_CIBLE = "id_cible";
    private final SharedPreferences prefs;

    public static SharesPreferencesHelper with(Context context){
        return new SharesPreferencesHelper(context);
    }

    private SharesPreferencesHelper(Context context){
        prefs = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    void saveAdvertisingId(String id){
        prefs.edit().putString(ADVERT_ID,id).apply();
    }

    String getAdvertisingId(){
        return prefs.getString(ADVERT_ID,null);
    }

    public @Nullable String getIdCible(){
        return prefs.getString(ID_CIBLE, null);
    }

    void setIdCible(String idCible){
        prefs.edit().putString(ID_CIBLE,idCible).apply();
    }


}
