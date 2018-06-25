package fr.kwanko.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * SourceCode
 * Command to fake the broadcast :
 * adb shell
 *  am broadcast -a com.android.vending.INSTALL_REFERRER -e "referrer" "someprivatecible"
 * Created by erusu on 3/17/2017.
 */

public class KwankoReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras == null){
            return;
        }
        String referrerString = extras.getString("referrer");
        if(referrerString != null) {
            SharesPreferencesHelper.with(context).setIdCible(referrerString);
        }
    }
}
