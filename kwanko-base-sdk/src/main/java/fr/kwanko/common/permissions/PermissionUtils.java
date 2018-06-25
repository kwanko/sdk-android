package fr.kwanko.common.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 2/24/2017.
 */

public class PermissionUtils {

    private PermissionUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static boolean checkPermission(@NonNull Context context, @NonNull String permission){
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(permission);
        return ContextCompat.checkSelfPermission(context,permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkAtLeastOnePermission(@NonNull Context context,
                                                    @NonNull String... permissions){
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(permissions);
        boolean res = false;
        for(String permission:permissions){
            res = res || checkPermission(context,permission);
        }
        return res;
    }


}
