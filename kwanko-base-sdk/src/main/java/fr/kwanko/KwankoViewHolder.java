package fr.kwanko;

import java.lang.ref.WeakReference;

import fr.kwanko.internal.containers.BaseContainer;

/**
 * A solution proposed here: http://guides.codepath.com/android/shared-element-activity-transition
 * will not work because the container for the ad is a customView with complex content management
 * Created by vfatu on 20.01.2017.
 */

public class KwankoViewHolder {

    private static WeakReference<BaseContainer> baseWebViewContainer;

    static BaseContainer getBaseWebViewContainer() {
        return baseWebViewContainer!=null?baseWebViewContainer.get():null;
    }

    public static void setBaseWebViewContainer(BaseContainer baseWebViewContainer) {
        KwankoViewHolder.baseWebViewContainer = new WeakReference<>(baseWebViewContainer);
    }
}
