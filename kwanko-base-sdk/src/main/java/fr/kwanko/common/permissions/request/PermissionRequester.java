package fr.kwanko.common.permissions.request;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;

import fr.kwanko.common.permissions.request.activity.TransparentActivity;
import fr.kwanko.common.permissions.request.conf.Configuration;
import fr.kwanko.common.permissions.request.strategy.selector.StrategySelector;
import fr.kwanko.common.permissions.request.featurers.Feature;

/**
 * Created by erusu on 10/18/2016.
 */

public class PermissionRequester {

    public static final String RESULT_TYPE = "result_type";
    public static final String RESULT = "result";

    public static final int GRANTED = 1;
    public static final int DENIED = 0;
    public static final String UFR_ACTION_FILTER = "ro.hevsoft.urf.ACTION_RESULT";
    public static final IntentFilter UFR_FILTER = new IntentFilter(UFR_ACTION_FILTER);

    private Feature requestedFeature;
    private Callback whenGranted;
    private Callback whenDenied;
    private Context context;
    private Receiver receiver;

    public static PermissionRequester instance(){
        return new PermissionRequester();
    }

    private PermissionRequester(){
        receiver = new Receiver();
    }

    public PermissionRequester requestFor(Feature feature){
        requestedFeature = feature;
        return this;
    }

    public PermissionRequester withContext(Context context){
        this.context = context;
        return this;
    }

    public PermissionRequester whenGranted(Callback callback){
        this.whenGranted = callback;
        return this;
    }

    public PermissionRequester whenDenied(Callback callback){
        this.whenDenied = callback;
        return this;
    }

    public void startRequest(){
        Intent intent = new Intent(context,TransparentActivity.class);
        intent.putExtra(TransparentActivity.FEATURE_KEY,requestedFeature);
        context.startActivity(intent);
        registerBroadcast();
    }

    public PermissionRequester withStrategySelector(StrategySelector strategySelector){
        Configuration.INSTANCE.setStrategySelector(strategySelector);
        return this;
    }

    private void registerBroadcast(){
        context.registerReceiver(receiver,UFR_FILTER);
    }

    private void unregisterBroadcast(){
        context.unregisterReceiver(receiver);
    }

    @SuppressWarnings("unchecked")
    private void performGrantedAction(Intent intent){
        GrantedArgument argument = (GrantedArgument) intent.getSerializableExtra(RESULT);
        if(whenGranted != null){
            whenGranted.call(argument);
        }
        destroy();
    }

    @SuppressWarnings("unchecked")
    private void performDeniedAction(Intent intent){
        DeniedArgument argument = (DeniedArgument) intent.getSerializableExtra(RESULT);
        if(whenDenied != null){
            whenDenied.call(argument);
        }
        destroy();
    }

    private void destroy(){
        unregisterBroadcast();
        context = null;
        receiver = null;
        whenGranted = null;
        whenDenied = null;
    }

    public interface Callback<T extends ArgumentContract>{
        void call(T argument);
    }

    public interface ArgumentContract extends Serializable{

    }

    static abstract class Argument implements ArgumentContract,Serializable{
        public Feature requestedFeature;

        Argument(Feature requestedFeature) {
            this.requestedFeature = requestedFeature;
        }
    }

    public static class GrantedArgument extends Argument{

        public GrantedArgument(Feature requestedFeature) {
            super(requestedFeature);
        }
    }

    public static class DeniedArgument extends  Argument{
        public int deniedCause;

        public DeniedArgument(Feature requestedFeature,int deniedCause) {
            super(requestedFeature);
            this.deniedCause  = deniedCause;
        }
    }

    private  class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(RESULT_TYPE,DENIED);
            if(type == GRANTED){
                performGrantedAction(intent);
            }else{
                performDeniedAction(intent);
            }
        }
    }
}
