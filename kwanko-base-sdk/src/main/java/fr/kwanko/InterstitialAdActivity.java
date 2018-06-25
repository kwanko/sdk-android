package fr.kwanko;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.content.MraidContentController;
import fr.kwanko.model.KwankoAdContext;
import fr.kwanko.model.OrientationProperties;

public class InterstitialAdActivity extends AppCompatActivity implements OnCloseListener {

    private BaseContainer adContainer;
    private MraidContentController contentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.kwanko_activity_interstitial_ad);

        adContainer = KwankoViewHolder.getBaseWebViewContainer();
        if(adContainer == null){
            KwankoLog.e("container not found",new NullPointerException("adContainer is null"));
            finish();
        }
        contentController = (MraidContentController) adContainer.getContentController();
        contentController.showClosableRegion(isUseCustomClose(contentController.getAdContext()));
        setupCloseListener();
        setupOrientationChangeListener();
        changeAdParent();
        contentController.setupSizeChangeListener();
        contentController.notifyExpandFinished();
    }

    private boolean isUseCustomClose(KwankoAdContext adContext){
        return adContext.getExpandProperties().isUseCustomClose();
    }

    private void setupCloseListener() {
        contentController.subscribeForCloseEvent(CloseEvents.EXPAND,this);
    }

    private void setupOrientationChangeListener() {
        handleOrientation(contentController.getAdContext().getOrientationProperties());
        contentController.setOrientationChangeListener(new OrientationChangeListener() {
            @Override
            public void onOrientationChange() {
                handleOrientation(contentController.getAdContext().getOrientationProperties());
            }
        });
    }

    private void changeAdParent() {
        final ViewGroup newParent = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        ViewGroup defaultParent = ViewGroup.class.cast(adContainer.getParent());
        defaultParent.removeView(adContainer);

        /*If we are going to use expand properties, we should use this version, but since the mraid
         javascript doesn't send them to use, we just use MATCH_PARENT instead*/


        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        newParent.addView(adContainer, params);
    }

    public void handleOrientation(OrientationProperties orientationProperties) {
        if (!orientationProperties.isAllowOrientationChange()) {
            if ("none".equals(orientationProperties.getForceOrientation())) {
                switch (getOrientation()) {
                    case Configuration.ORIENTATION_PORTRAIT:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    default:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }
            } else if ("portrait".equals(orientationProperties.getForceOrientation())) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void finish() {
        overridePendingTransition(0,0);
        super.finish();
    }

    @Override
    public void onCloseEvent(Object source, CloseEvent event) {
        finish();
        KwankoViewHolder.setBaseWebViewContainer(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(contentController != null){
            contentController.unSubscribeForCloseEvent(CloseEvents.GENERAL,this);
        }
    }
}
