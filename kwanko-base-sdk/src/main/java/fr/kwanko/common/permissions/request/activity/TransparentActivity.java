package fr.kwanko.common.permissions.request.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fr.kwanko.common.permissions.request.PermissionRequester;
import fr.kwanko.common.permissions.request.conf.Configuration;
import fr.kwanko.common.permissions.request.featurers.Feature;
import fr.kwanko.common.permissions.request.strategy.Strategy;

/**
 * Created by erusu on 10/18/2016.
 */

public class TransparentActivity extends AppCompatActivity implements RequestFeatureActivity{

    public static final String FEATURE_KEY = "feature";

    private Feature requestedFeature;
    private Strategy strategy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestedFeature = (Feature) getIntent().getSerializableExtra(FEATURE_KEY);
        if(requestedFeature == null){
            throw new NullPointerException("requestedFeature is null");
        }
        doRequestLogic();
    }

    private void doRequestLogic(){
        strategy = Configuration.INSTANCE.getStrategySelector().getStrategy(requestedFeature);
        strategy.execute(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        strategy.onActivityResult(this,requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        strategy.onPermissionResult(this,requestCode, permissions, grantResults);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void callFeatureGranted(PermissionRequester.GrantedArgument argument) {
        Intent intent = new Intent(PermissionRequester.UFR_ACTION_FILTER);
        intent.putExtra(PermissionRequester.RESULT,argument);
        intent.putExtra(PermissionRequester.RESULT_TYPE, PermissionRequester.GRANTED);
        sendBroadcast(intent);
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void callFeatureDenied(PermissionRequester.DeniedArgument argument) {
        Intent intent = new Intent(PermissionRequester.UFR_ACTION_FILTER);
        intent.putExtra(PermissionRequester.RESULT,argument);
        intent.putExtra(PermissionRequester.RESULT_TYPE, PermissionRequester.DENIED);
        sendBroadcast(intent);
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

}
