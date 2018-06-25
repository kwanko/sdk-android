package fr.kwanko.internal.overly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import fr.kwanko.common.KwankoBroadcastUtils;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.overly.OverlyAdException;
import fr.kwanko.overly.OverlyAdListener;

/**
 * SourceCode
 * Created by erusu on 5/26/2017.
 */

class OverlyAdDisplayController extends BaseOverlyAdDisplayController {

    public static final int MILLISECOND_FACTOR = 1000;
    private BroadcastReceiver receiver;
    private IntentFilter receiverFilter =
            new IntentFilter("com.kwanko.ads.overly.controller.receiver");
    private static final String  sendFilter = "com.kwanko.ads.overly.activity.receiver";
    private Handler handler;
    private int timeBeforeOverly;
    private int overlyCountdown;
    private boolean destroyed = false;
    private Runnable showAdRunnable = new Runnable() {
        @Override
        public void run() {
            if(!destroyed) {
                startOverlyActivity();
            }
        }
    };
    private Runnable closeAdRunnable = new Runnable() {
        @Override
        public void run() {
            if(!destroyed) {
                KwankoBroadcastUtils.sendAction(getContext(), "forcedClose", sendFilter);
            }
        }
    };

    OverlyAdDisplayController(Context context, AdModel adModel, OverlyAdListener externalListener) {
        super(context, adModel, externalListener);
        registerBroadCast();
        handler = new Handler();
        timeBeforeOverly = adModel.getTimeBeforeOverly();
        overlyCountdown = adModel.getOverlyCountdown();
    }

    private void registerBroadCast(){
        if(receiver != null){
            unregisterBroadcast();
            receiver = null;
        }

        receiver = new OverlyBroadcastReceiver(getListener());
        getContext().registerReceiver(receiver , receiverFilter);
    }

    private void unregisterBroadcast(){
        if(receiver == null){
            return;
        }
        getContext().unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    public void show() {
        if(timeBeforeOverly>0) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(showAdRunnable,timeBeforeOverly*MILLISECOND_FACTOR);
        }else{
            handler.post(showAdRunnable);
        }
        if(overlyCountdown >0){
            int closeTime = (overlyCountdown + timeBeforeOverly) * MILLISECOND_FACTOR;
            handler.postDelayed(closeAdRunnable,closeTime);
        }
    }

    private void startOverlyActivity(){
        OverlyActivity.start(getContext(),getAdModel());
    }

    @Override
    public void cancel() {
        KwankoBroadcastUtils.sendAction(getContext(),"cancel",sendFilter);
    }

    @Override
    public void onDestroy() {
        destroyed = true;
        unregisterBroadcast();
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public boolean isLoaded() {
        throw new UnsupportedOperationException("isLoaded is not supported by this controller");
    }

    private class OverlyBroadcastReceiver extends BroadcastReceiver{

        private OverlyAdListener listener;

        OverlyBroadcastReceiver(OverlyAdListener listener){
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra("action")){
                case "error":
                    if(listener != null) {
                        listener.onError(new OverlyAdException());
                    }
                    break;
                case "close":
                    if(listener != null) {
                        listener.onOverlyAdClosed();
                    }
                    break;
                case "open":
                    if(listener != null) {
                        listener.onOverlyAdOpen();
                    }
                    break;
                default:
                    //ignore
                    break;
            }
        }
    }
}
