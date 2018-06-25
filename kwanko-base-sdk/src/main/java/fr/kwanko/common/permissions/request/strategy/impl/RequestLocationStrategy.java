package fr.kwanko.common.permissions.request.strategy.impl;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.common.permissions.request.PermissionRequester;
import fr.kwanko.common.permissions.request.activity.RequestFeatureActivity;
import fr.kwanko.common.permissions.request.featurers.CommonFeatures;
import fr.kwanko.common.permissions.request.featurers.Feature;
import fr.kwanko.common.permissions.request.strategy.AbsStrategy;
import fr.kwanko.common.permissions.request.utils.HardwareUtils;
import fr.kwanko.common.permissions.request.utils.PermissionUtils;

/**
 * Created by erusu on 10/19/2016.
 */

public class RequestLocationStrategy extends AbsStrategy {

    static final int REQUEST_LOCATION_RESOLUTION_REQUIRED = 101;
    static final int REQUEST_LOCATION_PERMISSION = 102;
    static final int DENIED_SENDER_INTENT_EXCEPTION = 2;
    static final int DENIED_SETTINGS_CHANGE_UNAVAILABLE = 3;
    static final int DENIED_NOT_TURN_LOCATION_ON = 4;
    static final int DENIED_LOCATION_PERMISSION_REFUSED = 5;
    static final int DENIED_NO_PROVIDER_SUPPORTED = 6;

    public RequestLocationStrategy(Feature feature) {
        super(feature);
    }

    @Override
    public void execute(RequestFeatureActivity activity) {
        boolean areMainLocationProviderAvailable =
                HardwareUtils.areMainLocationProvidersAvailable(activity.getContext());
        if(!areMainLocationProviderAvailable){
            activity.callFeatureDenied(new PermissionRequester.DeniedArgument(CommonFeatures.Location,DENIED_NO_PROVIDER_SUPPORTED));
        }
        boolean locationHasPermission = PermissionUtils.havePermissionFor(activity.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (!locationHasPermission) {
            onRequestLocationPermission(activity);
            return;
        }
        boolean isLocationOn = HardwareUtils.areMainLocationProvidersEnabled(activity.getContext());
        if (!isLocationOn) {
            onLocationIsOff(activity);
            return;
        }
        activity.callFeatureGranted(new PermissionRequester.GrantedArgument(CommonFeatures.Location));
    }

    private void onLocationIsOff(final RequestFeatureActivity activity) {
        new AlertDialog.Builder(activity.getActivity())
                .setTitle(R.string.urf_turn_location_on_title)
                .setMessage(R.string.urf_turn_location_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.getActivity().startActivityForResult(callGPSSettingIntent, REQUEST_LOCATION_RESOLUTION_REQUIRED);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.callFeatureDenied(new PermissionRequester.DeniedArgument(CommonFeatures.Location,
                                DENIED_NOT_TURN_LOCATION_ON));
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        activity.callFeatureDenied(new PermissionRequester.DeniedArgument(CommonFeatures.Location,
                                DENIED_NOT_TURN_LOCATION_ON));
                    }
                }).create().show();
    }

    private void onRequestLocationPermission(RequestFeatureActivity activity) {
        ActivityCompat.requestPermissions(activity.getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onActivityResult(RequestFeatureActivity activity, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestLocationStrategy.REQUEST_LOCATION_RESOLUTION_REQUIRED:
                boolean isLocationOn = HardwareUtils.areMainLocationProvidersEnabled(activity.getContext());
                if (isLocationOn) {
                    activity.callFeatureGranted(new PermissionRequester.GrantedArgument(CommonFeatures.Location));
                } else {
                    activity.callFeatureDenied(new PermissionRequester.DeniedArgument(CommonFeatures.Location,
                            DENIED_NOT_TURN_LOCATION_ON));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionResult(RequestFeatureActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestLocationStrategy.REQUEST_LOCATION_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            execute(activity);
        } else {
            activity.callFeatureDenied(new PermissionRequester.DeniedArgument(CommonFeatures.Location,
                    DENIED_LOCATION_PERMISSION_REFUSED));
        }
    }
}
