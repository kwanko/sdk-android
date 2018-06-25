package fr.kwanko.common.permissions.request.activity;

import android.app.Activity;
import android.content.Context;

import fr.kwanko.common.permissions.request.PermissionRequester;

/**
 * Created by erusu on 10/19/2016.
 */

public interface RequestFeatureActivity {

    Context getContext();

    void callFeatureGranted(PermissionRequester.GrantedArgument argument);

    void callFeatureDenied(PermissionRequester.DeniedArgument argument);

    Activity getActivity();

}
