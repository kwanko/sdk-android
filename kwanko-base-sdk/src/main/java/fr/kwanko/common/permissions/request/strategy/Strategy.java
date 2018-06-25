package fr.kwanko.common.permissions.request.strategy;

import android.content.Intent;
import android.support.annotation.NonNull;

import fr.kwanko.common.permissions.request.activity.RequestFeatureActivity;

/**
 * Created by erusu on 10/18/2016.
 */

public interface Strategy {

    void execute(RequestFeatureActivity activity);

    void onActivityResult(RequestFeatureActivity activity, int requestCode, int resultCode, Intent data);

    void onPermissionResult(RequestFeatureActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
