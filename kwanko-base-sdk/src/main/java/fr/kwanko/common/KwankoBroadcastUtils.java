package fr.kwanko.common;

import android.content.Context;
import android.content.Intent;

/**
 * SourceCode
 * Created by erusu on 5/30/2017.
 */

public class KwankoBroadcastUtils {

    private KwankoBroadcastUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static void sendAction(Context context , String action, String sendFilter){
        Intent intent = new Intent(sendFilter);
        intent.putExtra("action",action);
        context.sendBroadcast(intent);
    }
}
