package fr.kwanko.internal.overly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.KwankoAd;
import fr.kwanko.common.KwankoBroadcastUtils;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.OverlyContainer;
import fr.kwanko.internal.content.ContentController;
import fr.kwanko.internal.controller.OverlyControllerFactory;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.params.Position;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * SourceCode
 * Created by erusu on 5/26/2017.
 */

public class OverlyActivity extends AppCompatActivity implements OnCloseListener{

    private IntentFilter receiverFilter =
            new IntentFilter("com.kwanko.ads.overly.activity.receiver");
    private static final String sendFilter = "com.kwanko.ads.overly.controller.receiver";

    private BroadcastReceiver receiver;
    private boolean forseDistroyed = false;
    private boolean paused = false;

    public static void start(Context context, AdModel model){
        Intent intent = new Intent(context,OverlyActivity.class);
        intent.putExtra("model",model);
        context.startActivity(intent);
    }

    private AdModel adModel;
    private KwankoAd adView;
    private OverlyContainer container;
    private ContentController contentController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        overridePendingTransition(0,0);
        registerBroadCast();
        setContentView(R.layout.kwanko_overly_activity);
        adModel = (AdModel) getIntent().getSerializableExtra("model");
        if(adModel == null){
            reportException(new IllegalStateException("model is null"));
            finish();
        }
        createAdView();
        if(forseDistroyed){
            return;
        }
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT);
        setContentView(adView,params);
        getWindow().setGravity(
                Position.gravityFromPositionValue(
                adModel.getPosition()!=null?
        adModel.getPosition():Position.DEFAULT));
        KwankoBroadcastUtils.sendAction(this,"open",sendFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        adView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    private void createAdView(){
        if(forseDistroyed){
            return;
        }
        adView = new KwankoAd(this, new OverlyControllerFactory(adModel,this));
        for(int i=0;i<adView.getChildCount();i++){
            if(adView.getChildAt(i) instanceof OverlyContainer){
                container = (OverlyContainer) adView.getChildAt(i);
                contentController = container.getContentController();
                break;
            }
        }
    }

    private void reportException(Exception e){
        KwankoLog.e(e);
        KwankoBroadcastUtils.sendAction(this,"error",sendFilter);
    }

    @Override
    public void onCloseEvent(Object source, CloseEvent event) {
        if(paused){
            return;
        }
        KwankoBroadcastUtils.sendAction(this,"close",sendFilter);
        if(!isFinishing() && receiver != null) {
            super.onBackPressed();
        }
        contentController = null;
        container = null;
    }

    private void registerBroadCast(){
        if(receiver != null){
            unregisterBroadcast();
            receiver = null;
        }

        receiver = new ActivityBroadCastReceiver();
        this.registerReceiver(receiver , receiverFilter);
    }

    private void unregisterBroadcast(){
        if(receiver == null){
            return;
        }
        unregisterReceiver(receiver);
        receiver = null;
    }

    private void cancel(){
        if(adView != null){
            adView.setVisibility(View.GONE);
            ViewGroup viewGroup = (ViewGroup) adView.getParent();
            viewGroup.removeView(adView);
        }

    }

    private void forceClose(){
        if(adView == null){
            finish();
        }

        if(container == null || contentController == null){
            return;
        }
        container.getContentController().onCloseButtonClicked(container);
    }

    private class ActivityBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra("action")){

                case "cancel":
                    cancel();
                    break;
                case "forcedClose":
                    forceClose();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        onCloseEvent(null,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        receiver = null;
    }
}
