package fr.kwanko.common;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.util.List;

import fr.kwanko.common.permissions.request.PermissionRequester;
import fr.kwanko.common.permissions.request.featurers.CommonFeatures;

/**
 * SourceCode
 * Created by erusu on 6/19/2017.
 */

public class KwankoDownloadUtils {

    private KwankoDownloadUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static void download(final Context context, final String uriString){
        PermissionRequester.instance().withContext(context)
                .requestFor(new CommonFeatures.GenericFeature(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .whenGranted(new PermissionRequester.Callback() {
                    @Override
                    public void call(PermissionRequester.ArgumentContract argument) {
                        downloadWithPermission(context,uriString);
                    }
                })
                .startRequest();
    }

    private static void downloadWithPermission(Context context, String uriString){
        Uri uri = Uri.parse(uriString);
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                uri);

        String fileName ;
        List<String> pathSegments =uri.getPathSegments();
        if(pathSegments == null || pathSegments.isEmpty()){
            fileName = "kwanko.png";
        }else{
            fileName = pathSegments.get(pathSegments.size() - 1);
        }
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDescription("Store picture")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        fileName);


        mgr.enqueue(request);
    }
}
