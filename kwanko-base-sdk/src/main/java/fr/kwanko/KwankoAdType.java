package fr.kwanko;

import android.content.Context;

import fr.kwanko.internal.containers.BaseContainer;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public interface KwankoAdType {

    Context getContext();

    void integrateAdContainer(BaseContainer container);

    void hide();
    int getAdWidth();
    int getAdHeight();
}
