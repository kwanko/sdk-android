package fr.kwanko.internal;

import android.content.Context;
import android.view.ViewGroup;

import fr.kwanko.common.ViewUtils;

/**
 * SourceCode
 * Created by erusu on 3/30/2017.
 */

public class ActivityDelegate {

    private ViewGroup rootView;
    private Context context;
    public ActivityDelegate(Context context){
        this.context = context;
    }

    public ViewGroup getRoot() {
        if (rootView == null) {
            rootView = ViewUtils.getRootView(context);
        }
        return rootView;
    }
}
