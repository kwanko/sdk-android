package fr.kwanko.internal.containers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.common.ViewUtils;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/29/2017.
 */

@SuppressLint("ViewConstructor")
public class OverlyContainer extends BaseResizeContainer implements OnCloseListener {

    public OverlyContainer(Context context, BaseResizeParams params) {
        super(context, params);
    }

    @Override
    protected void onSetContentController() {
        getContentController().showClosableRegion(false);
        getContentController().subscribeForCloseEvent(CloseEvents.GENERAL, this);
    }

    @Override
    public void onCloseEvent(Object source, CloseEvent event) {
        removeAdViewFromItsParent();
    }

    private void removeAdViewFromItsParent(){
        ViewGroup adParent = (ViewGroup) getParent().getParent();
        adParent.removeView((View)getParent());
    }

}
