package fr.kwanko;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.ViewUtils;
import fr.kwanko.common.permissions.PermissionUtils;

/**Kwanko SDK
 * Created by vfatu on 06.02.2017.
 */

public class SupportedFeatures {

    private Context context;

    public SupportedFeatures(Context context) {
        this.context = context;
    }

    public boolean isSmsAvailable() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        return isAvailable(context, intent);
    }

    public boolean isTelAvailable() {
        Uri telUri = Uri.parse("tel:");
        Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
        return isAvailable(context, intent);
    }

    @SuppressLint("ObsoleteSdkInt")
    public boolean isCalendarAvailable() {
        Intent intent = new Intent(Intent.ACTION_INSERT).setType("vnd.android.cursor.item/event");
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                isAvailable(context, intent);
    }

    public boolean isStorePictureAvailable() {
        PackageManager pc = context.getPackageManager();
        try {
            PackageInfo info =
                    pc.getPackageInfo(context.getPackageName(),PackageManager.GET_PERMISSIONS);
            if(info == null||info.requestedPermissions == null){
                return false;
            }
            for(String s:info.requestedPermissions){
                if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(s)){
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isInlineVideoAvailable(View view) {
        View tempView = view;
        while (true) {
            if(tempView == null){
                break;
            }
            if (!tempView.isHardwareAccelerated()
                    || bitMaskContainsFlag(tempView.getLayerType(), View.LAYER_TYPE_SOFTWARE)) {
                return false;
            }

            if (!(tempView.getParent() instanceof View)) {
                break;
            }

            tempView = (View)tempView.getParent();
        }

        Window window = null;
        try {
             window = ViewUtils.getWindow(context);
        }catch (IllegalArgumentException e){
            KwankoLog.e(e);
        }
        if (window != null) {
            if (bitMaskContainsFlag(window.getAttributes().flags,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)) {
                return true;
            }
        }

        return false;
    }

    private static boolean bitMaskContainsFlag(final int bitMask, final int flag) {
        return (bitMask & flag) != 0;
    }


    private boolean isAvailable(Context context, Intent intent) {
        try {
            final PackageManager mgr = context.getPackageManager();
            List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return !list.isEmpty();
        }catch (NullPointerException e){
            KwankoLog.e(e);
            return false;
        }
    }

}
