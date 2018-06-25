package fr.kwanko.internal.content;

import android.view.View;
import android.view.ViewGroup;

import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.containers.OverlyContainer;
import fr.kwanko.internal.model.CloseButtonMetadata;
import fr.kwanko.model.KwankoAdState;
import fr.kwanko.model.KwankoPlacementType;

/**
 * SourceCode
 * Created by erusu on 6/14/2017.
 */

public class OverlyMraidContentController extends MraidContentController {

    public OverlyMraidContentController(BaseContainer container,
                                        CloseButtonMetadata metadata,
                                        KwankoPlacementType placementType) {
        super(container, metadata, placementType);
    }

    private boolean closeOnResume = false;

    @Override
    public void onCloseButtonClicked(View source) {
        OnCloseListener.CloseEvent event = getCloseEventForAdState(adContext.getState());
        collapse();
        if(event != null) {
            notifyCloseListener(this, event);
            if(source instanceof OverlyContainer){
                if(event == CloseEvents.EXPAND) {
                    closeOnResume = true;
                }else if (event == CloseEvents.RESIZE){
                    notifyCloseListener(this,CloseEvents.GENERAL);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(closeOnResume){
            notifyCloseListener(this,CloseEvents.GENERAL);
        }
    }

    @Override
    public void collapse() {
        if (adContext.getState() == KwankoAdState.RESIZED
                || adContext.getState() == KwankoAdState.EXPANDED) {
            ViewGroup currentParent = (ViewGroup) container().getParent();
            currentParent.removeView(container());
            defaultParent.addView(container(), defaultLayoutParams);
            updateProperties();
            if(adContext.getState() == KwankoAdState.RESIZED) {
                showClosableRegion(false);
            }
            adContext.setState(KwankoAdState.DEFAULT);
        } else {
            //this check is to allow overly ads to close without rising an error
            if(!(container() instanceof OverlyContainer) &&
                    !(adContext.getState() == KwankoAdState.DEFAULT)) {
                webViewBridge.notifyError("close", "Ad is not resized or expanded");
            }
        }
    }

    @Override
    public void resize(KwankoAdState newState, String customClosePosition, boolean allowOffscreen) {
        super.resize(newState,customClosePosition,allowOffscreen);
        clearCloseButtonBackground();
    }
}
