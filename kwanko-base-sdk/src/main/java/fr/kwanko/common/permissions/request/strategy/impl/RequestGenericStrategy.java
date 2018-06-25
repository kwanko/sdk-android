package fr.kwanko.common.permissions.request.strategy.impl;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import fr.kwanko.common.permissions.request.PermissionRequester;
import fr.kwanko.common.permissions.request.activity.RequestFeatureActivity;
import fr.kwanko.common.permissions.request.featurers.Feature;
import fr.kwanko.common.permissions.request.strategy.AbsStrategy;

/**
 * Created by erusu on 10/19/2016.
 */

public class RequestGenericStrategy extends AbsStrategy {

    public static final int PERMISSION_DENIED = 10;
    private static final int REQ_GENERIC_PERM = 500;

    public RequestGenericStrategy(Feature feature) {
        super(feature);
    }

    @Override
    public void execute(RequestFeatureActivity activity) {
        boolean havePerm =
                ActivityCompat.checkSelfPermission(activity.getContext(),feature.getPermission()) ==
                        PackageManager.PERMISSION_GRANTED;
        if(!havePerm){
            ActivityCompat
                    .requestPermissions(activity.getActivity(),
                            new String[]{feature.getPermission()},
                            REQ_GENERIC_PERM);
            return;
        }
        activity.callFeatureGranted(new PermissionRequester.GrantedArgument(feature));
    }

    @Override
    public void onActivityResult(RequestFeatureActivity activity, int requestCode, int resultCode, Intent data) {
        //no code required
    }

    @Override
    public void onPermissionResult(RequestFeatureActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_GENERIC_PERM &&
                grantResults.length>0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            activity.callFeatureGranted(new PermissionRequester.GrantedArgument(feature));
        }else{
            activity.callFeatureDenied(new PermissionRequester.DeniedArgument(feature, PERMISSION_DENIED));
        }
    }
}
