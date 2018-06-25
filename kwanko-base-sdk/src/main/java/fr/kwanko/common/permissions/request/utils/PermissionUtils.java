package fr.kwanko.common.permissions.request.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by erusu on 10/19/2016.
 */

public class PermissionUtils {

    private PermissionUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static boolean havePermissionFor(Context activity, String permission){
        int result = ContextCompat.checkSelfPermission(activity,permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


}
